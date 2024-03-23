package com.fges.todoapp;

import com.fges.todoapp.Data.TodoManager;
import com.fges.todoapp.Commands.CommandFactory;
import com.fges.todoapp.Data.DataSourceFactory;
import com.fges.todoapp.Utilities.ConsoleLogger;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        try {
            System.exit(exec(args));
        } catch (IOException e) {
            System.err.println("An I/O error occurred: " + e.getMessage());
            System.exit(1); // Exit with error status
        } catch (ParseException e) {
            System.err.println("Failed to parse command line arguments: " + e.getMessage());
            System.exit(1); // Exit with error status
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            System.exit(1); // Exit with error status
        }
    }

    public static int exec(String[] args) throws IOException, ParseException {
        Options cliOptions = new Options();
        CommandLineParser parser = new DefaultParser();

        cliOptions.addOption("s", "source", true, "File containing the todos");

        CommandLine cmd = parser.parse(cliOptions, args);

        // Initialize dependencies required by CommandFactory
        DataSourceFactory dsFactory = new DataSourceFactory();
        ConsoleLogger logger = new ConsoleLogger();
        CommandFactory.initializeDependencies(dsFactory, logger);

        String fileName = cmd.getOptionValue("s", "todos.json");

        List<String> positionalArgs = new ArrayList<>(Arrays.asList(cmd.getArgs()));
        if (positionalArgs.isEmpty()) {
            System.err.println("Missing Command");
            return 1;
        }

        String command = positionalArgs.remove(0);
        positionalArgs.add(0, fileName);

        TodoManager todoManager = new TodoManager(Paths.get(fileName));

        return todoManager.executeCommand(command, positionalArgs);
    }
}
