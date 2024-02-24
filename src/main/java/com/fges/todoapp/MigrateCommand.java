package com.fges.todoapp;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MigrateCommand implements Command {
    private final DataSourceFactory dataSourceFactory;
    private final Logger logger;

    public MigrateCommand(DataSourceFactory dataSourceFactory, Logger logger) {
        this.dataSourceFactory = dataSourceFactory;
        this.logger = logger;
    }

    @Override
    public void execute(List<Task> tasks, List<String> commandArgs) {
        // Validate command arguments
        if (commandArgs.size() < 4 || !commandArgs.get(0).equals("--source") || !commandArgs.contains("--output")) {
            logger.logError("Usage: migrate --source <sourceDataSource> --output <outputDataSource>");
            return;
        }

        String sourceDataSource = null;
        String sourceFilePath = null;
        String outputDataSource = null;
        String outputFilePath = null;

        try {
            // Extract source and output data sources
            for (int i = 0; i < commandArgs.size() - 1; i++) {
                if (commandArgs.get(i).equals("--source")) {
                    sourceDataSource = commandArgs.get(i + 1);
                    sourceFilePath = commandArgs.get(i + 2);
                } else if (commandArgs.get(i).equals("--output")) {
                    outputDataSource = commandArgs.get(i + 1);
                    outputFilePath = commandArgs.get(i + 2);
                }
            }

            // Create data source instances
            DataSource source = dataSourceFactory.createDataSource(sourceDataSource, sourceFilePath);
            DataSource output = dataSourceFactory.createDataSource(outputDataSource, outputFilePath);

            // Read tasks from source data source
            List<Task> sourceTasks = source.readData();

            // Start transaction
            output.beginTransaction();

            // Migrate tasks
            for (Task task : sourceTasks) {
                output.writeData(Collections.singletonList(task));
            }

            // Commit transaction
            output.commitTransaction();

            logger.log("Migration completed successfully.");
        } catch (IOException e) {
            logger.logError("Error occurred during migration: " + e.getMessage());
            // Rollback transaction in case of error
            if (outputDataSource != null) {
                try {
                    DataSource output = dataSourceFactory.createDataSource(outputDataSource, outputFilePath);
                    output.rollbackTransaction();
                } catch (IOException ex) {
                    logger.logError("Failed to rollback transaction: " + ex.getMessage());
                }
            }
        }
    }

    @Override
    public String getName() {
        return "migrate";
    }
}
