package com.fges.todoapp;

import java.io.IOException;
import java.util.List;

public class InsertCommand implements Command {
    @Override
    public void execute(List<Task> tasks, List<String> commandArgs) throws IOException {
        if (commandArgs.size() < 2) {
            throw new IllegalArgumentException("Usage: insert <taskName> <done>");
        }

        String taskName = commandArgs.get(0);
        boolean isDone = Boolean.parseBoolean(commandArgs.get(1));

        Task newTask = new Task(taskName, isDone);
        tasks.add(newTask);
    }

    @Override
    public String getName() {
        return "insert";
    }
}
