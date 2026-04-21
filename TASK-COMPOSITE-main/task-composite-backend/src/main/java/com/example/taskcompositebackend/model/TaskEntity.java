package com.example.taskcompositebackend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String type; // "TASK" o "GROUP"

    private int progress; // solo aplica a TASK

    // Si tiene parentId → es hijo de ese grupo
    @Column(name = "parent_id")
    private Long parentId;
}