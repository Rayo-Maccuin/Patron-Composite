package com.example.taskcompositebackend.composite;

import lombok.Getter;

@Getter
public class TaskLeaf implements TaskComponent {

    private final String title;
    private final int progress;

    public TaskLeaf(String title, int progress) {
        this.title = title;
        this.progress = progress;
    }

    @Override
    public String getType() {
        return "TASK";
    }

    @Override
    public void display(int depth) {
        System.out.println("  ".repeat(depth) + "- [TASK] " + title
                + " (" + progress + "%)");
    }
}
