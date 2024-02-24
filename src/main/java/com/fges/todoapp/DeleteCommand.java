package com.fges.todoapp;

import java.io.IOException;
import java.util.List;

public class DeleteCommand implements Command {
    @Override
    public void execute(List<Task> tasks, List<String> commandArgs) throws IOException {
        if (commandArgs.isEmpty()) {
            throw new IllegalArgumentException("Usage: delete <taskIndex>");
        }

        int taskIndex = Integer.parseInt(commandArgs.get(0));
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new IndexOutOfBoundsException("Task index out of bounds");
        }

        tasks.remove(taskIndex);
    }

    @Override
    public String getName() {
        return "delete";
    }
}
