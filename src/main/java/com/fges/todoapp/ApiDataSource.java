package com.fges.todoapp;

import java.io.IOException;
import java.util.List;

public class ApiDataSource implements DataSource {

    @Override
    public List<Task> readTasks() throws IOException {
        // Fetch tasks from API
        throw new UnsupportedOperationException("Reading tasks from API is not supported yet");
    }

    @Override
    public void writeTasks(List<Task> tasks) throws IOException {
        // Not supported for API data source
        throw new UnsupportedOperationException("Write operation not supported for API data source");
    }

    @Override
    public List<Task> readData() throws IOException {
        // Not implemented for API data source
        throw new UnsupportedOperationException("Reading data not supported for API data source");
    }

    @Override
    public void writeData(List<Task> tasks) throws IOException {
        // Not implemented for API data source
        throw new UnsupportedOperationException("Writing data not supported for API data source");
    }

    @Override
    public void beginTransaction() throws IOException {
        // Begin transaction logic for API data source
        throw new UnsupportedOperationException("Transaction management not supported for API data source");
    }

    @Override
    public void commitTransaction() throws IOException {
        // Commit transaction logic for API data source
        throw new UnsupportedOperationException("Transaction management not supported for API data source");
    }

    @Override
    public void rollbackTransaction() throws IOException {
        // Rollback transaction logic for API data source
        throw new UnsupportedOperationException("Transaction management not supported for API data source");
    }
}
