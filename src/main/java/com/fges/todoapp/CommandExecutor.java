package com.fges.todoapp;

import java.io.IOException;
import java.util.List;

public class CommandExecutor {
    private final TodoFileHandler fileHandler;

    public CommandExecutor(TodoFileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public int insertCommand(List<String> positionalArgs, List<Todo> todos) throws IOException {
        if (positionalArgs.size() < 2) {
            System.err.println("Missing TODO name");
            return 1;
        }

        String todoName = positionalArgs.get(1);
        Todo newTodo = new Todo(todoName);
        todos.add(newTodo);

        fileHandler.writeTodosToFile(todos);
        System.out.println("Todo added successfully.");
        return 0;
    }

    public int listCommand(List<Todo> todos) {
        System.out.println("Listing Todos:");
        for (Todo todo : todos) {
            System.out.println(todo);
        }
        return 0;
    }
}
