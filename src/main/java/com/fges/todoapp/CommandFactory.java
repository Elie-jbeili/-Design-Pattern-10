package com.fges.todoapp;

public abstract class CommandFactory {
    public static Command createCommand(String commandName) {
        String className = "com.fges.todoapp." + capitalize(commandName) + "Command";
        try {
            Class<?> clazz = Class.forName(className);
            return (Command) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown command: " + commandName, e);
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
