package com.fges.todoapp;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TodoManager {
    private final Path filePath;

    public TodoManager(Path filePath) {
        this.filePath = filePath;
    }

    public int executeCommand(String command, List<String> positionalArgs) throws IOException {
        List<Todo> todos = new ArrayList<>();
        TodoFileHandler fileHandler = new TodoFileHandler(filePath);

        if (fileHandler.fileExists())
            todos = fileHandler.readTodosFromFile();

        CommandExecutor commandExecutor = new CommandExecutor(fileHandler);

        switch (command) {
            case "insert":
                return commandExecutor.insertCommand(positionalArgs, todos);
            case "list":
                return commandExecutor.listCommand(todos);
            default:
                System.err.println("Unknown command: " + command);
                return 1;
        }
    }
}