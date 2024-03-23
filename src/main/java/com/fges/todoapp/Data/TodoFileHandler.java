package com.fges.todoapp.Data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fges.todoapp.Model.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class TodoFileHandler {
    private final Path filePath;

    public TodoFileHandler(Path filePath) {
        this.filePath = filePath;
    }

    public boolean fileExists() {
        return Files.exists(filePath);
    }

    // Ensures the file exists before trying to read from it
    public List<Task> readTasksFromFile() throws IOException {
        if (!fileExists()) {
            // If file doesn't exist, throw IOException
            throw new IOException("File does not exist.");
        }

        // Check if the file is empty
        if (Files.size(filePath) == 0) {
            return new ArrayList<>(); // Return empty list if file is empty
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Files.newInputStream(filePath), new TypeReference<>() {});
    }

    // Ensures the file exists before trying to write to it
    public void writeTasksToFile(List<Task> tasks) throws IOException {
        // Create parent directories if they don't exist
        if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }

        // Using StandardOpenOption.CREATE will create the file if it doesn't exist
        ObjectMapper mapper = new ObjectMapper();
        Files.write(filePath, mapper.writeValueAsBytes(tasks), StandardOpenOption.CREATE);
    }
}
