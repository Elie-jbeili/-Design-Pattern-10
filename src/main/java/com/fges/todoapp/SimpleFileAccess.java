package com.fges.todoapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SimpleFileAccess implements FileAccess {
    @Override
    public String readFileContent(Path path) throws IOException {
        return Files.readString(path);
    }

    @Override
    public void writeFileContent(Path path, String content) throws IOException {
        Files.writeString(path, content);
    }
}
