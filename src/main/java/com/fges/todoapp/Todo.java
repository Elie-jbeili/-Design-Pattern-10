package com.fges.todoapp;
public class Todo {
    private String name;
    private String author;
    private String color;
    private boolean done;

    public Todo() {
    }

    public Todo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", color='" + color + '\'' +
                ", done=" + done +
                '}';
    }
}
