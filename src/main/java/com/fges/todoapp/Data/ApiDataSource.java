package com.fges.todoapp.Data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fges.todoapp.Model.Task;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiDataSource implements Readable<Task>, Writable<Task> {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl;

    public ApiDataSource(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public List<Task> readData() throws IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/tasks"))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                // Parse the JSON response body into a list of Task objects
                return objectMapper.readValue(response.body(), new TypeReference<>() {
                });
            } else {
                throw new IOException("Unexpected response status: " + response.statusCode());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }

    @Override
    public void writeData(List<Task> tasks) throws IOException {
        String json = objectMapper.writeValueAsString(tasks);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println("Tasks successfully written to the API.");
            } else {
                throw new IOException("Failed to write tasks, response status: " + response.statusCode());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }
}
