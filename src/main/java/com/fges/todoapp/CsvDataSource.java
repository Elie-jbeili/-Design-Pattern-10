package com.fges.todoapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvDataSource implements DataSource {
    private final Path filePath;
    private List<String> transactionBackup;

    public CsvDataSource(String filePath) {
        this.filePath = Paths.get(filePath);
        this.transactionBackup = new ArrayList<>();
    }

    @Override
    public List<Task> readTasks() throws IOException {
        return readData(); // Utilizing readData() method as CSV-specific logic is similar
    }

    @Override
    public void writeTasks(List<Task> tasks) throws IOException {
        writeData(tasks); // Utilizing writeData() method as CSV-specific logic is similar
    }

    @Override
    public List<Task> readData() throws IOException {
        List<Task> tasks = new ArrayList<>();
        if (Files.exists(filePath)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        Task task = new Task(parts[0], Boolean.parseBoolean(parts[1]));
                        tasks.add(task);
                    }
                }
            }
        }
        return tasks;
    }

    @Override
    public void writeData(List<Task> tasks) throws IOException {
        if (!Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            for (Task task : tasks) {
                writer.write(task.getName() + "," + task.isDone());
                writer.newLine();
            }
        }
    }

    @Override
    public void beginTransaction() throws IOException {
        if (transactionBackup.isEmpty()) {
            // Backup the current contents of the CSV file for rollback
            transactionBackup = Files.readAllLines(filePath);
        }
    }

    @Override
    public void commitTransaction() throws IOException {
        // Clear the transaction backup as the transaction is successfully committed
        transactionBackup.clear();
    }

    @Override
    public void rollbackTransaction() throws IOException {
        if (!transactionBackup.isEmpty()) {
            // Restore the contents of the CSV file from the backup
            Files.write(filePath, transactionBackup);
            // Clear the transaction backup after rollback
            transactionBackup.clear();
        }
    }
}
