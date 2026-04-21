package com.example.taskcompositebackend.composite;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TaskGroup implements TaskComponent {

    private final String title;
    private final List<TaskComponent> children = new ArrayList<>();

    public TaskGroup(String title) {
        this.title = title;
    }

    public void add(TaskComponent component) {
        children.add(component);
    }

    public void remove(TaskComponent component) {
        children.remove(component);
    }

    // Progreso = promedio del progreso de todos los hijos
    @Override
    public int getProgress() {
        if (children.isEmpty())
            return 0;
        return (int) children.stream()
                .mapToInt(TaskComponent::getProgress)
                .average()
                .orElse(0);
    }

    @Override
    public String getType() {
        return "GROUP";
    }

    @Override
    public void display(int depth) {
        System.out.println("  ".repeat(depth) + "+ [GROUP] " + title
                + " (" + getProgress() + "%)");
        children.forEach(c -> c.display(depth + 1));
    }
}