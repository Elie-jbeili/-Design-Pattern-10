package com.fges.todoapp;

import java.io.IOException;
import java.nio.file.Path;

public class CsvFileHandler implements FileHandler {
    private final Path filePath;
    private final FileAccess fileAccess;

    public CsvFileHandler(Path filePath, FileAccess fileAccess) {
        this.filePath = filePath;
        this.fileAccess = fileAccess;
    }

    @Override
    public void insert(Todo todo) throws IOException {
        // Implement CSV insertion logic
    }

    @Override
    public void list(boolean onlyDone) throws IOException {
        // Implement CSV listing logic
    }
}
