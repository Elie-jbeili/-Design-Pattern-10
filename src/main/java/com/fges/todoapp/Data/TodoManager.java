package com.fges.todoapp.Data;

import com.fges.todoapp.Commands.Command;
import com.fges.todoapp.Commands.CommandFactory;
import com.fges.todoapp.Model.Task;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class TodoManager {
    private final TodoFileHandler fileHandler;

    public TodoManager(Path filePath) {
        this.fileHandler = new TodoFileHandler(filePath);
    }

    public int executeCommand(String commandName, List<String> positionalArgs) {
        try {
            List<Task> tasks = fileHandler.fileExists() ? fileHandler.readTasksFromFile() : new java.util.ArrayList<>();

            Command command = CommandFactory.createCommand(commandName);
            command.execute(tasks, positionalArgs);

            fileHandler.writeTasksToFile(tasks);
            return 0;
        } catch (IOException e) {
            System.err.println("Error executing command: " + e.getMessage());
            return 1;
        }
    }
}
