package com.fges.todoapp.Data;

import java.io.IOException;

public interface Transactional {
    void beginTransaction() throws IOException;
    void commitTransaction() throws IOException;
    void rollbackTransaction() throws IOException;
}
