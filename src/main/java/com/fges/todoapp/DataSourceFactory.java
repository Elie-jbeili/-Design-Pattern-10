package com.fges.todoapp;

import java.io.IOException;
import java.util.List;

public class DataSourceFactory {
    public DataSource createDataSource(String dataSourceName, String fileName) {
        switch (dataSourceName) {
            case "json":
                return new JsonDataSource(fileName) {
                    @Override
                    public List<Task> readTasks() throws IOException {
                        return null;
                    }

                    @Override
                    public void writeTasks(List<Task> tasks) throws IOException {

                    }
                };
            case "csv":
                return new CsvDataSource(fileName) {
                    @Override
                    public List<Task> readTasks() throws IOException {
                        return null;
                    }

                    @Override
                    public void writeTasks(List<Task> tasks) throws IOException {

                    }
                };
            case "api":
                return new ApiDataSource();
            default:
                throw new IllegalArgumentException("Unknown data source: " + dataSourceName);
        }
    }
}
