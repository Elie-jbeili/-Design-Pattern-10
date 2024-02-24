package com.fges.todoapp;

import java.io.IOException;
import java.util.List;

public class ListCommand implements Command {
    @Override
    public void execute(List<Task> tasks, List<String> commandArgs) throws IOException {
        if (commandArgs.contains("--done")) {
            listDoneTasks(tasks);
        } else {
            listAllTasks(tasks);
        }
    }

    private void listAllTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            System.out.println("All Tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                System.out.println((i + 1) + ". " + task.getName());
            }
        }
    }

    private void listDoneTasks(List<Task> tasks) {
        boolean foundDoneTask = false;
        System.out.println("Done Tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.isDone()) {
                System.out.println((i + 1) + ". " + task.getName());
                foundDoneTask = true;
            }
        }
        if (!foundDoneTask) {
            System.out.println("No tasks marked as done.");
        }
    }

    @Override
    public String getName() {
        return "list";
    }
}
