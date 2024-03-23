package com.fges.todoapp.Commands;

import com.fges.todoapp.Model.Task;

import java.util.List;

public class DeleteCommand implements Command {
    @Override
    public void execute(List<Task> tasks, List<String> commandArgs) {
        if (commandArgs.isEmpty()) {
            throw new IllegalArgumentException("Usage: delete <taskIndex>");
        }

        int taskIndex = Integer.parseInt(commandArgs.get(0));
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new IndexOutOfBoundsException("Task index out of bounds");
        }

        tasks.remove(taskIndex);
        System.out.println("Task deleted successfully.");
    }

    @Override
    public String getName() {
        return "delete";
    }
}
