package com.fges.todoapp.Utilities;

public class ConsoleLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.println("LOG: " + message);
    }

    @Override
    public void logError(String errorMessage) {
        System.err.println("ERROR: " + errorMessage);
    }
}
