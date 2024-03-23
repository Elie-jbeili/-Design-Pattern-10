package com.fges.todoapp.Commands;

import com.fges.todoapp.Data.DataSourceFactory;
import com.fges.todoapp.Utilities.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CommandFactory {
    private static final Map<String, Supplier<Command>> commandMap = new HashMap<>();
    private static DataSourceFactory dataSourceFactory;
    private static Logger logger;

    static {
        // Register existing commands
        commandMap.put("insert", InsertCommand::new);
        commandMap.put("delete", DeleteCommand::new);
        commandMap.put("list", ListCommand::new);
        // Assuming MigrateCommand needs DataSourceFactory and Logger
        commandMap.put("migrate", () -> new MigrateCommand(dataSourceFactory, logger));

        // Register the new WebCommand
        commandMap.put("web", WebCommand::new); // This line registers the WebCommand
    }

    public static void initializeDependencies(DataSourceFactory dsFactory, Logger log) {
        dataSourceFactory = dsFactory;
        logger = log;
    }

    public static Command createCommand(String commandName) {
        Supplier<Command> commandSupplier = commandMap.get(commandName.toLowerCase());
        if (commandSupplier == null) {
            throw new IllegalArgumentException("Unknown command: " + commandName);
        }
        return commandSupplier.get();
    }
}
