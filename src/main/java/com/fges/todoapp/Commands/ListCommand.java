package com.fges.todoapp.Commands;

import com.fges.todoapp.Data.DataSourceFactory;
import com.fges.todoapp.Data.Readable;
import com.fges.todoapp.Model.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListCommand implements Command {
    @Override
    public void execute(List<Task> tasks, List<String> commandArgs) throws IOException {
        String sourcePath = "";
        boolean onlyDone = false;

        for (int i = 0; i < commandArgs.size(); i++) {
            String arg = commandArgs.get(i);
            if (("--source".equals(arg) || "-s".equals(arg)) && i + 1 < commandArgs.size()) {
                sourcePath = commandArgs.get(i + 1);
            } else if ("--done".equals(arg) || "-d".equals(arg)) {
                onlyDone = true;
            }
        }

        if (sourcePath.isEmpty()) {
            System.err.println("Source file not specified.");
            return;
        }

        String sourceType = sourcePath.endsWith(".json") ? "json" : "csv";
        Readable<Task> dataSource = DataSourceFactory.getReadableDataSource(sourceType, sourcePath);
        List<Task> sourceTasks = dataSource.readData();

        if (onlyDone) {
            listDoneTasks(sourceTasks);
        } else {
            listAllTasks(sourceTasks);
        }
    }

    private void listAllTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        System.out.println("All Tasks:");
        tasks.forEach(task -> System.out.println("- " + (task.getStatus().equals("DONE") ? "Done: " : "Not Done: ") + task.getContent()));
    }

    private void listDoneTasks(List<Task> tasks) {
        List<Task> doneTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getStatus().equals("DONE")) {
                doneTasks.add(task);
            }
        }

        if (doneTasks.isEmpty()) {
            System.out.println("No tasks marked as done.");
            return;
        }

        System.out.println("Done Tasks:");
        doneTasks.forEach(task -> System.out.println("- Done: " + task.getContent()));
    }

    @Override
    public String getName() {
        return "list";
    }
}
