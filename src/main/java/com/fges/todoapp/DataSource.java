package com.fges.todoapp;
import java.io.IOException;
import java.util.List;

public interface DataSource {
    List<Task> readTasks() throws IOException;
    void writeTasks(List<Task> tasks) throws IOException;

    List<Task> readData() throws IOException;
    void writeData(List<Task> tasks) throws IOException;

    void beginTransaction() throws IOException;
    void commitTransaction() throws IOException;
    void rollbackTransaction() throws IOException;
}
