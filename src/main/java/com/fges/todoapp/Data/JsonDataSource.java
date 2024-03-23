package com.fges.todoapp.Data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fges.todoapp.Model.Task;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonDataSource implements Readable<Task>, Writable<Task>, Transactional {
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
    public List<Task> readData() {
        try {
            if (Files.exists(filePath)) {
                return objectMapper.readValue(filePath.toFile(), new TypeReference<>() {});
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception appropriately
            return new ArrayList<>();
        }
    }

    @Override
    public void writeData(List<Task> tasks) {
        try {
            objectMapper.writeValue(filePath.toFile(), tasks);
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }

    @Override
    public void beginTransaction() {
        try {
            if (!transactionInProgress) {
                transactionInProgress = true;
                transactionBackup = readData();
            } else {
                throw new IllegalStateException("Transaction already in progress.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }

    @Override
    public void commitTransaction() {
        try {
            if (transactionInProgress) {
                transactionBackup.clear();
                transactionInProgress = false;
            } else {
                throw new IllegalStateException("No transaction in progress to commit.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }

    @Override
    public void rollbackTransaction() {
        try {
            if (transactionInProgress) {
                writeData(transactionBackup);
                transactionInProgress = false;
                transactionBackup.clear();
            } else {
                throw new IllegalStateException("No transaction in progress to rollback.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }
}
