package com.fges.todoapp;

import java.io.IOException;
import java.nio.file.Path;

public interface FileAccess {
    String readFileContent(Path path) throws IOException;
    void writeFileContent(Path path, String content) throws IOException;
}
