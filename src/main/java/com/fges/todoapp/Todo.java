package com.fges.todoapp;

public class Todo {
    private String name;
    private boolean done;

    public Todo(String name, boolean done) {
        this.name = name;
        this.done = done;
    }

    public String getName() {
        return name;
    }

    public boolean isDone() {
        return done;
    }
}
