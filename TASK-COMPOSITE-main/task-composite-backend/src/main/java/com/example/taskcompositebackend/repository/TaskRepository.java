package com.example.taskcompositebackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskcompositebackend.model.TaskEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    // Trae solo las tareas raíz (sin padre)
    List<TaskEntity> findByParentIdIsNull();

    // Trae los hijos directos de un grupo
    List<TaskEntity> findByParentId(Long parentId);
}