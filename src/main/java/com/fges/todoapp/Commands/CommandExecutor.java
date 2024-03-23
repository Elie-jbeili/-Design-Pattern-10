package com.fges.todoapp.Commands;

import com.fges.todoapp.Data.TodoFileHandler;
import com.fges.todoapp.Model.Task;
import java.io.IOException;
import java.util.List;

public class CommandExecutor {
    private final TodoFileHandler fileHandler;

    public CommandExecutor(TodoFileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    // File: CommandExecutor.java
    public int insertCommand(List<String> positionalArgs, List<Task> todos) {
        try {
            if (positionalArgs.size() < 2) {
                System.err.println("Missing TODO content");
                return 1;
            }
            String todoContent = positionalArgs.get(1);
            Task newTodo = new Task(todoContent, "NOT_DONE");
            todos.add(newTodo);
            fileHandler.writeTasksToFile(todos);
            System.out.println("Todo added successfully.");
            return 0;
        } catch (IOException e) {
            System.err.println("Error when trying to write todo to file: " + e.getMessage());
            return 1;
        }
    }


    // No changes in listCommand
    public int listCommand(List<Task> todos) {
        System.out.println("Listing Todos:");
        for (Task todo : todos) {
            System.out.println(todo);
        }
        return 0;
    }
}
