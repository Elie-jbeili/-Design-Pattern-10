package com.fges.todoapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Path;

public class JsonFileHandler implements FileHandler {
    private final Path filePath;
    private final FileAccess fileAccess;

    public JsonFileHandler(Path filePath, FileAccess fileAccess) {
        this.filePath = filePath;
        this.fileAccess = fileAccess;
    }

    @Override
    public void insert(Todo todo) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String fileContent = fileAccess.readFileContent(filePath);
        JsonNode actualObj = mapper.readTree(fileContent);
        if (actualObj.isMissingNode()) {
            actualObj = JsonNodeFactory.instance.arrayNode();
        }

        if (actualObj instanceof ArrayNode) {
            ObjectNode todoNode = JsonNodeFactory.instance.objectNode();
            todoNode.put("name", todo.getName());
            todoNode.put("done", todo.isDone());
            ((ArrayNode) actualObj).add(todoNode);
        }

        fileAccess.writeFileContent(filePath, actualObj.toString());
    }

    @Override
    public void list(boolean onlyDone) throws IOException {
        // Implement logic to list todos from the file
    }
}
