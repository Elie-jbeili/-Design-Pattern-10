package com.fges.todoapp.Data;

import com.fges.todoapp.Model.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvDataSource implements Readable<Task>, Writable<Task>, Transactional {
    private final Path filePath;
    private List<String> transactionBackup;

    public CsvDataSource(String filePath) {
        this.filePath = Paths.get(filePath);
        this.transactionBackup = new ArrayList<>();
    }

    @Override
    public List<Task> readData() {
        List<Task> tasks = new ArrayList<>();
        if (Files.exists(filePath)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    // Assuming CSV format is: content,status
                    if (parts.length >= 2) {
                        String content = parts[0];
                        String status = parts[1];
                        Task task = new Task(content, status);
                        tasks.add(task);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
            }
        }
        return tasks;
    }

    @Override
    public void writeData(List<Task> tasks) {
        if (!Files.exists(filePath.getParent())) {
            try {
                Files.createDirectories(filePath.getParent());
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            for (Task task : tasks) {
                writer.write(task.getContent() + "," + task.getStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }

    @Override
    public void beginTransaction() {
        if (transactionBackup.isEmpty()) {
            try {
                transactionBackup = Files.readAllLines(filePath);
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
            }
        }
    }

    @Override
    public void commitTransaction() {
        transactionBackup.clear();
    }

    @Override
    public void rollbackTransaction() {
        if (!transactionBackup.isEmpty()) {
            try {
                Files.write(filePath, transactionBackup);
                transactionBackup.clear();
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
            }
        }
    }
}
