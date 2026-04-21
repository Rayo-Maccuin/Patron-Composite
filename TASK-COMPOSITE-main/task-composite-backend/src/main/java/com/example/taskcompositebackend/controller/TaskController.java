package com.example.taskcompositebackend.controller;

import com.example.taskcompositebackend.dto.TaskRequest;
import com.example.taskcompositebackend.dto.TaskResponse;
import com.example.taskcompositebackend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @PostMapping
    public ResponseEntity<TaskResponse> create(
            @RequestBody TaskRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTree() {
        return ResponseEntity.ok(service.getTree());
    }

    @PatchMapping("/{id}/progress")
    public ResponseEntity<TaskResponse> updateProgress(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(
                service.updateProgress(id, body.get("progress")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}