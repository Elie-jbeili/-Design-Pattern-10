package com.fges.todoapp;

import java.io.IOException;
import java.util.List;

public interface Command {
    void execute(List<Task> tasks, List<String> commandArgs) throws IOException;
    String getName();
}
