package com.example.taskcompositebackend.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String type;
    private int progress;
    private Long parentId;
    private List<TaskResponse> children; // árbol recursivo
}