package com.fges.todoapp.Commands;

import com.fges.todoapp.Data.DataSourceFactory;
import com.fges.todoapp.Data.Readable;
import com.fges.todoapp.Data.Writable;
import com.fges.todoapp.Model.Task;
import com.sun.net.httpserver.HttpServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class WebCommand implements Command {
    @Override
    public void execute(List<Task> tasks, List<String> commandArgs) throws IOException {
        String sourcePath = commandArgs.stream()
                .skip(commandArgs.indexOf("-s") + 1)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Source path not provided"));

        String sourceType = sourcePath.endsWith(".json") ? "json" : "csv";
        Writable<Task> dataSourceWritable = DataSourceFactory.getWritableDataSource(sourceType, sourcePath);
        Readable<Task> dataSourceReadable = DataSourceFactory.getReadableDataSource(sourceType, sourcePath);

        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);
        ObjectMapper objectMapper = new ObjectMapper();

        server.createContext("/todos", exchange -> {
            try {
                if ("POST".equals(exchange.getRequestMethod())) {
                    Task newTask = objectMapper.readValue(exchange.getRequestBody(), Task.class);
                    List<Task> updatedTasks = new ArrayList<>(dataSourceReadable.readData());
                    updatedTasks.add(newTask);
                    dataSourceWritable.writeData(updatedTasks);
                    String response = "{\"message\": \"inserted\"}";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else if ("GET".equals(exchange.getRequestMethod())) {
                    List<Task> existingTasks = dataSourceReadable.readData();
                    String response = objectMapper.writeValueAsString(existingTasks);
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            } catch (Exception e) {
                String response = "{\"error\": \"" + e.getMessage() + "\"}";
                exchange.sendResponseHeaders(500, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        System.out.println("Server started on port 8080");
        server.start();
    }

    @Override
    public String getName() {
        return "web";
    }
}
