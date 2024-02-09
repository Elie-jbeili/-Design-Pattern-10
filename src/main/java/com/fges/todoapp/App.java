package com.fges.todoapp;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class App {

    public static void main(String[] args) throws Exception {
        System.exit(exec(args));
    }

    public static int exec(String[] args) throws IOException {
        Options cliOptions = new Options();
        cliOptions.addRequiredOption("s", "source", true, "File containing the todos");
        cliOptions.addOption("d", "done", false, "Mark the todo as done");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException ex) {
            System.err.println("Fail to parse arguments: " + ex.getMessage());
            return 1;
        }

        String fileName = cmd.getOptionValue("s");
        boolean isDone = cmd.hasOption("done");
        List<String> positionalArgs = cmd.getArgList();
        if (positionalArgs.isEmpty()) {
            System.err.println("Missing Command");
            return 1;
        }

        FileAccess fileAccess = new SimpleFileAccess();
        FileHandler fileHandler = FileHandlerFactory.getFileHandler(fileName, fileAccess);

        String command = positionalArgs.get(0);
        switch (command) {
            case "insert":
                if (positionalArgs.size() < 2) {
                    System.err.println("Missing TODO name");
                    return 1;
                }
                String todoName = positionalArgs.get(1);
                Todo todo = new Todo(todoName, isDone);
                fileHandler.insert(todo);
                break;
            case "list":
                fileHandler.list(isDone);
                break;
            default:
                System.err.println("Unknown Command");
                return 1;
        }

        System.err.println("Done.");
        return 0;
    }
}
