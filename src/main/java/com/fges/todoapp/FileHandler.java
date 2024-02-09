package com.fges.todoapp;

import java.io.IOException;

public interface FileHandler {
    void insert(Todo todo) throws IOException;
    void list(boolean onlyDone) throws IOException;
}
