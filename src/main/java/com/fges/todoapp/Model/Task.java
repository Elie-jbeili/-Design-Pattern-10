package com.fges.todoapp.Model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Add the @JsonIgnoreProperties annotation to ignore unknown fields
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {
    private String content;
    private String status; // "DONE" or "NOT_DONE"

    @JsonCreator
    public Task(@JsonProperty("content") @JsonAlias("name") String content,
                @JsonProperty("status") String status) {
        this.content = content;
        // Default to "NOT_DONE" if status is null or any other value besides "DONE"
        this.status = (status != null && status.equals("DONE")) ? "DONE" : "NOT_DONE";
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        // Ensure status is either "DONE" or "NOT_DONE"
        this.status = (status != null && status.equals("DONE")) ? "DONE" : "NOT_DONE";
    }
}
