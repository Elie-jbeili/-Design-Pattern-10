package com.fges.todoapp.Data;

import com.fges.todoapp.Model.Task;
import java.util.Optional;

public class DataSourceFactory {

    // Example API URL - replace this with your actual API URL
    private static final String API_URL = "http://your.api.url/endpoint";

    public static Object createDataSource(String dataSourceType, String filePath) {
        // Ensure filePath is not null for file-based data sources
        if (("json".equals(dataSourceType) || "csv".equals(dataSourceType)) && (filePath == null || filePath.isBlank())) {
            throw new IllegalArgumentException("File path must be provided for json and csv data sources");
        }

        return switch (dataSourceType) {
            case "json" -> new JsonDataSource(filePath);
            case "csv" -> new CsvDataSource(filePath);
            case "api" -> new ApiDataSource(API_URL); // API data source does not need a filePath
            default -> throw new IllegalArgumentException("Unsupported data source type: " + dataSourceType);
        };
    }

    @SuppressWarnings("unchecked")
    public static Readable<Task> getReadableDataSource(String dataSourceType, String filePath) {
        Object dataSource = createDataSource(dataSourceType, filePath);
        if (dataSource instanceof Readable) {
            return (Readable<Task>) dataSource;
        } else {
            throw new IllegalArgumentException("Data source is not readable: " + dataSourceType);
        }
    }

    @SuppressWarnings("unchecked")
    public static Writable<Task> getWritableDataSource(String dataSourceType, String filePath) {
        Object dataSource = createDataSource(dataSourceType, filePath);
        if (dataSource instanceof Writable) {
            return (Writable<Task>) dataSource;
        } else {
            throw new IllegalArgumentException("Data source is not writable: " + dataSourceType);
        }
    }
}
