package com.fges.todoapp.Commands;

import com.fges.todoapp.Data.DataSourceFactory;
import com.fges.todoapp.Data.Readable;
import com.fges.todoapp.Data.Transactional;
import com.fges.todoapp.Data.Writable;
import com.fges.todoapp.Model.Task;
import com.fges.todoapp.Utilities.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MigrateCommand implements Command {
    private final Logger logger;

    public MigrateCommand(DataSourceFactory dataSourceFactory, Logger logger) {
        this.logger = logger;
    }

    @Override
    public void execute(List<Task> tasks, List<String> commandArgs) {
        Map<String, String[]> parsedArgs = parseArguments(commandArgs);

        String[] sourceArgs = parsedArgs.get("--source");
        String[] outputArgs = parsedArgs.get("--output");

        if (sourceArgs == null || outputArgs == null || sourceArgs.length < 2 || outputArgs.length < 2) {
            logger.logError("Usage: migrate -s <sourcePath> -o <outputPath>");
            return;
        }

        String sourceType = sourceArgs[0];
        String sourcePath = sourceArgs[1];
        String targetType = outputArgs[0];
        String targetPath = outputArgs[1];

        try {
            Readable<Task> source = DataSourceFactory.getReadableDataSource(sourceType, sourcePath);
            Writable<Task> target = DataSourceFactory.getWritableDataSource(targetType, targetPath);

            List<Task> sourceTasks = source.readData();

            if (target instanceof Transactional) {
                ((Transactional) target).beginTransaction();
            }

            target.writeData(sourceTasks);

            if (target instanceof Transactional) {
                ((Transactional) target).commitTransaction();
            }

            logger.log("Migration completed successfully.");
        } catch (IOException e) {
            logger.logError("Migration failed: " + e.getMessage());
            try {
                Writable<Task> output = DataSourceFactory.getWritableDataSource(targetType, targetPath);
                if (output instanceof Transactional) {
                    ((Transactional) output).rollbackTransaction();
                }
            } catch (IOException ex) {
                logger.logError("Failed to rollback transaction: " + ex.getMessage());
            }
        }
    }

    private Map<String, String[]> parseArguments(List<String> args) {
        Map<String, String[]> parsedArgs = new HashMap<>();
        for (int i = 0; i < args.size(); i++) {
            String arg = args.get(i);
            switch (arg) {
                case "--source", "-s", "--output", "-o" -> {
                    if (i + 2 < args.size()) {
                        String key = arg.startsWith("--") ? arg : "--" + arg.substring(1); // Convert short to long form
                        parsedArgs.put(key, new String[]{args.get(i + 1), args.get(i + 2)});
                        i += 2; // Increment to skip over processed args
                    }
                }
            }
        }
        return parsedArgs;
    }

    @Override
    public String getName() {
        return "migrate";
    }
}
