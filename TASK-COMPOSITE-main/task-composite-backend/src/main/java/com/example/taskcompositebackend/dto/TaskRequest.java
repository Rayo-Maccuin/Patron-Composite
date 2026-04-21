package com.example.taskcompositebackend.dto;

import lombok.Data;

@Data
public class TaskRequest {
    private String title;
    private String type; // "TASK" o "GROUP"
    private int progress; // 0-100, solo si type=TASK
    private Long parentId; // null si es raíz
}