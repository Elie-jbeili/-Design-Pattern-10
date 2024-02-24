package com.fges.todoapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public List<Todo> readTodosFromFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Todo.class);
        List<Todo> todos = new ArrayList<>();

        if (Files.exists(filePath) && Files.size(filePath) > 0) {
            todos = mapper.readValue(filePath.toFile(), listType);
        }

        return todos;
    }

    public void writeTodosToFile(List<Todo> todos) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(filePath.toFile(), todos);
    }
}