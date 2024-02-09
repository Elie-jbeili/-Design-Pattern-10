package com.fges.todoapp;

import java.io.IOException;
import java.nio.file.Path;

public class FileHandlerFactory {
    public static FileHandler getFileHandler(String fileName, FileAccess fileAccess) {
        Path filePath = Path.of(fileName);
        if (fileName.endsWith(".json")) {
            return new JsonFileHandler(filePath, fileAccess);
        } else if (fileName.endsWith(".csv")) {
            return new CsvFileHandler(filePath, fileAccess);
        } else {
            throw new IllegalArgumentException("Unsupported file format");
        }
    }
}
