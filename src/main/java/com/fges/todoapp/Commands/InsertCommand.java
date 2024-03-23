package com.fges.todoapp.Commands;

import com.fges.todoapp.Data.DataSourceFactory;
import com.fges.todoapp.Model.Task;
import com.fges.todoapp.Data.Writable;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertCommand implements Command {
    @Override
    public void execute(List<Task> tasks, List<String> commandArgs) throws IOException {
        if (commandArgs.isEmpty()) {
            System.err.println("Error: No arguments provided.");
            return;
        }

        // First argument is the file path, subsequent arguments are task details
        String sourcePath = commandArgs.get(0);
        Map<String, String> parsedArgs = parseArguments(commandArgs.subList(1, commandArgs.size()));

        String todoContent = parsedArgs.getOrDefault("todoName", "No Content"); // Use "No Content" as default
        String status = parsedArgs.containsKey("-d") ? "DONE" : "NOT_DONE";

        // Create and add the new task
        Task newTask = new Task(todoContent, status);
        tasks.add(newTask);

        // Determine the data source type from sourcePath
        String sourceType = sourcePath.endsWith(".json") ? "json" : "csv";
        Writable<Task> dataSource = DataSourceFactory.getWritableDataSource(sourceType, sourcePath);

        // Write the updated list of tasks back to the source
        dataSource.writeData(tasks);
        System.out.println("Task inserted successfully.");
    }

    @Override
    public String getName() {
        return "insert";
    }

    private Map<String, String> parseArguments(List<String> args) {
        Map<String, String> parsedArgs = new HashMap<>();
        List<String> flags = List.of("-s", "-d");

        String key = "todoName"; // Initialize the key to accumulate unnamed arguments (task content)
        StringBuilder todoNameBuilder = new StringBuilder();

        for (String arg : args) {
            if (flags.contains(arg)) {
                if (arg.equals("-d")) {
                    // Directly put the flag indicating the task should be marked as done
                    parsedArgs.put(arg, "true");
                }
                key = arg; // Prepare for the next value (in case of future flags requiring values)
            } else {
                // Accumulate task content or handle future flag values
                if ("todoName".equals(key)) {
                    todoNameBuilder.append(arg).append(" ");
                } else {
                    parsedArgs.put(key, arg);
                    key = "todoName"; // Reset key after handling flag value
                }
            }
        }

        // Put the accumulated content into the map
        if (todoNameBuilder.length() > 0) {
            parsedArgs.put("todoName", todoNameBuilder.toString().trim());
        }

        return parsedArgs;
    }
}
