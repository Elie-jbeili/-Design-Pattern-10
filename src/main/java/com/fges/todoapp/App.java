package com.fges.todoapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

interface FileAccess {
    String readFileContent(Path path) throws IOException;
    void writeFileContent(Path path, String content) throws IOException;
}

class SimpleFileAccess implements FileAccess {
    @Override
    public String readFileContent(Path path) throws IOException {
        return java.nio.file.Files.readString(path);
    }

    @Override
    public void writeFileContent(Path path, String content) throws IOException {
        java.nio.file.Files.writeString(path, content);
    }
}

interface FileHandler {
    void insert(String todo, boolean isDone) throws IOException;
    void list(boolean onlyDone) throws IOException;
}

class JsonFileHandler implements FileHandler {
    private Path filePath;
    private FileAccess fileAccess;

    public JsonFileHandler(Path filePath, FileAccess fileAccess) {
        this.filePath = filePath;
        this.fileAccess = fileAccess;
    }

    @Override
    public void insert(String todo, boolean isDone) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String fileContent = fileAccess.readFileContent(filePath);
        JsonNode actualObj = mapper.readTree(fileContent);
        if (actualObj instanceof MissingNode) {
            actualObj = JsonNodeFactory.instance.arrayNode();
        }

        if (actualObj instanceof ArrayNode) {
            ObjectNode todoNode = JsonNodeFactory.instance.objectNode();
            todoNode.put("todo", todo);
            todoNode.put("done", isDone);
            ((ArrayNode) actualObj).add(todoNode);
        }

        fileAccess.writeFileContent(filePath, actualObj.toString());
    }

    @Override
    public void list(boolean onlyDone) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String fileContent = fileAccess.readFileContent(filePath);
        JsonNode actualObj = mapper.readTree(fileContent);
        if (actualObj instanceof MissingNode) {
            actualObj = JsonNodeFactory.instance.arrayNode();
        }

        if (actualObj instanceof ArrayNode) {
            ((ArrayNode) actualObj).forEach(node -> {
                boolean isDone = node.get("done").asBoolean();
                if (!onlyDone || isDone) {
                    String prefix = isDone ? "Done: " : "";
                    System.out.println(prefix + node.get("todo").asText());
                }
            });
        }
    }
}

class CsvFileHandler implements FileHandler {
    private Path filePath;
    private FileAccess fileAccess;

    public CsvFileHandler(Path filePath, FileAccess fileAccess) {
        this.filePath = filePath;
        this.fileAccess = fileAccess;
    }

    @Override
    public void insert(String todo, boolean isDone) throws IOException {
        String fileContent = fileAccess.readFileContent(filePath);
        if (!fileContent.endsWith("\n") && !fileContent.isEmpty()) {
            fileContent += "\n";
        }
        fileContent += todo + "," + isDone;

        fileAccess.writeFileContent(filePath, fileContent);
    }

    @Override
    public void list(boolean onlyDone) throws IOException {
        String fileContent = fileAccess.readFileContent(filePath);
        Arrays.stream(fileContent.split("\n"))
                .filter(line -> !onlyDone || line.endsWith("true"))
                .forEach(line -> {
                    String[] parts = line.split(",");
                    String prefix = parts.length > 1 && Boolean.parseBoolean(parts[1]) ? "Done: " : "";
                    System.out.println(prefix + parts[0]);
                });
    }
}

class FileHandlerFactory {
    public static FileHandler getFileHandler(String fileName, FileAccess fileAccess) {
        Path filePath = Paths.get(fileName);
        if (fileName.endsWith(".json")) {
            return new JsonFileHandler(filePath, fileAccess);
        } else if (fileName.endsWith(".csv")) {
            return new CsvFileHandler(filePath, fileAccess);
        } else {
            throw new IllegalArgumentException("Unsupported file format");
        }
    }
}

public class App {

    public static void main(String[] args) throws Exception {
        System.exit(exec(args));
    }

    public static int exec(String[] args) throws IOException {
        Options cliOptions = new Options();
        cliOptions.addRequiredOption("s", "source", true, "File containing the todos");
        cliOptions.addOption("d", "done", false, "Mark the todo as done");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException ex) {
            System.err.println("Fail to parse arguments: " + ex.getMessage());
            return 1;
        }

        String fileName = cmd.getOptionValue("s");
        boolean isDone = cmd.hasOption("done");
        List<String> positionalArgs = cmd.getArgList();
        if (positionalArgs.isEmpty()) {
            System.err.println("Missing Command");
            return 1;
        }

        FileAccess fileAccess = new SimpleFileAccess();
        FileHandler fileHandler = FileHandlerFactory.getFileHandler(fileName, fileAccess);

        String command = positionalArgs.get(0);
        switch (command) {
            case "insert":
                if (positionalArgs.size() < 2) {
                    System.err.println("Missing TODO name");
                    return 1;
                }
                fileHandler.insert(positionalArgs.get(1), isDone);
                break;
            case "list":
                fileHandler.list(isDone);
                break;
            default:
                System.err.println("Unknown Command");
                return 1;
        }

        System.err.println("Done.");
        return 0;
    }
}
