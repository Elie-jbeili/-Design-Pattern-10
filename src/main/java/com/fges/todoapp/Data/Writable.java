package com.fges.todoapp.Data;

import java.io.IOException;
import java.util.List;

public interface Writable<T> {
    void writeData(List<T> data) throws IOException;
}
