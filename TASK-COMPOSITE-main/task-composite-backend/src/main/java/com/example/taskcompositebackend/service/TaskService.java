package com.example.taskcompositebackend.service;

import com.example.taskcompositebackend.composite.*;
import com.example.taskcompositebackend.dto.TaskRequest;
import com.example.taskcompositebackend.dto.TaskResponse;
import com.example.taskcompositebackend.model.TaskEntity;
import com.example.taskcompositebackend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    // ── Crear tarea o grupo ──────────────────────────
    public TaskResponse create(TaskRequest request) {
        TaskEntity entity = TaskEntity.builder()
                .title(request.getTitle())
                .type(request.getType().toUpperCase())
                .progress(request.getType().equalsIgnoreCase("GROUP")
                        ? 0
                        : request.getProgress())
                .parentId(request.getParentId())
                .build();

        TaskEntity saved = repository.save(entity);
        return toResponse(saved);
    }

    // ── Actualizar progreso de una tarea ─────────────
    public TaskResponse updateProgress(Long id, int progress) {
        TaskEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        entity.setProgress(progress);
        return toResponse(repository.save(entity));
    }

    // ── Eliminar tarea ───────────────────────────────
    public void delete(Long id) {
        // Eliminar hijos primero
        repository.findByParentId(id)
                .forEach(child -> delete(child.getId()));
        repository.deleteById(id);
    }

    // ── Obtener árbol completo usando Composite ──────
    public List<TaskResponse> getTree() {
        // 1. Obtener raíces (sin padre)
        List<TaskEntity> roots = repository.findByParentIdIsNull();

        // 2. Construir el árbol Composite y mostrarlo en consola
        roots.forEach(root -> buildComposite(root).display(0));

        // 3. Devolver el árbol como respuesta JSON
        return roots.stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    // ── Construye el árbol Composite en memoria ──────
    private TaskComponent buildComposite(TaskEntity entity) {
        if (entity.getType().equals("TASK")) {
            return new TaskLeaf(entity.getTitle(), entity.getProgress());
        }

        TaskGroup group = new TaskGroup(entity.getTitle());
        repository.findByParentId(entity.getId())
                .forEach(child -> group.add(buildComposite(child)));
        return group;
    }

    // ── Construye respuesta JSON recursiva ───────────
    private TaskResponse buildResponse(TaskEntity entity) {
        List<TaskResponse> children = repository
                .findByParentId(entity.getId())
                .stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());

        // Si es grupo, recalcula progreso con el Composite
        int progress = entity.getType().equals("GROUP")
                ? ((TaskGroup) buildComposite(entity)).getProgress()
                : entity.getProgress();

        return TaskResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .type(entity.getType())
                .progress(progress)
                .parentId(entity.getParentId())
                .children(children)
                .build();
    }

    private TaskResponse toResponse(TaskEntity entity) {
        return TaskResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .type(entity.getType())
                .progress(entity.getProgress())
                .parentId(entity.getParentId())
                .children(List.of())
                .build();
    }
}