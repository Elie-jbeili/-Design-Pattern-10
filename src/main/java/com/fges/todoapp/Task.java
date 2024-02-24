package com.fges.todoapp;
public class Task {
    private String name;
    private boolean done;

    // Default constructor
    public Task() {
    }

    // Constructor with parameters
    public Task(String name, boolean done) {
        this.name = name;
        this.done = done;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}

