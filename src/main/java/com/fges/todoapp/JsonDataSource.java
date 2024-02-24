package com.fges.todoapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDataSource implements DataSource {
    private final ObjectMapper objectMapper;
    private final Path filePath;
    private boolean transactionInProgress;
    private List<Task> transactionBackup;

    public JsonDataSource(String filePath) {
        this.objectMapper = new ObjectMapper();
        this.filePath = Path.of(filePath);
        this.transactionInProgress = false;
        this.transactionBackup = new ArrayList<>();
    }

    @Override
    public List<Task> readTasks() throws IOException {
        List<Task> tasks = new ArrayList<>();
        if (Files.exists(filePath)) {
            tasks = objectMapper.readValue(filePath.toFile(), new TypeReference<List<Task>>() {});
        }
        return tasks;
    }

    @Override
    public void writeTasks(List<Task> tasks) throws IOException {
        objectMapper.writeValue(filePath.toFile(), tasks);
    }

    @Override
    public List<Task> readData() throws IOException {
        // Here you can implement logic to read other types of data if needed
        return readTasks();
    }

    @Override
    public void writeData(List<Task> tasks) throws IOException {
        // Transaction management for writing data
        beginTransaction();
        try {
            writeTasks(tasks);
            commitTransaction();
        } catch (IOException e) {
            rollbackTransaction();
            throw e;
        }
    }

    @Override
    public void beginTransaction() throws IOException {
        if (transactionInProgress) {
            throw new IllegalStateException("Transaction is already in progress.");
        }
        // Begin transaction logic for JSON data source
        // For simplicity, let's assume a transaction is started immediately
        System.out.println("Transaction started for JSON data source.");
        transactionInProgress = true;
        // Backup current tasks for rollback
        transactionBackup = new ArrayList<>(readTasks());
    }

    @Override
    public void commitTransaction() throws IOException {
        if (!transactionInProgress) {
            throw new IllegalStateException("No transaction in progress to commit.");
        }
        // Commit transaction logic for JSON data source
        // For simplicity, let's assume a transaction is committed immediately
        System.out.println("Transaction committed for JSON data source.");
        transactionInProgress = false;
        // Clear transaction backup
        transactionBackup.clear();
    }

    @Override
    public void rollbackTransaction() throws IOException {
        if (!transactionInProgress) {
            throw new IllegalStateException("No transaction in progress to rollback.");
        }
        // Rollback transaction logic for JSON data source
        // Restore tasks from backup
        writeTasks(transactionBackup);
        System.out.println("Transaction rolled back for JSON data source.");
        transactionInProgress = false;
        // Clear transaction backup
        transactionBackup.clear();
    }
}
