package com.example.taskcompositebackend.composite;

public interface TaskComponent {
    String getTitle();

    String getType(); // "TASK" o "GROUP"

    int getProgress(); // 0-100

    void display(int depth); // muestra el árbol en consola
}