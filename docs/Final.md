The application allows users to perform a variety of operations on todo tasks,
such as adding, deleting, listing, migrating tasks between different data sources, 
and serving tasks over a simple web server.
It's structured in a modular way, with clear separation between commands, data handling,
and model representation, making it extensible and maintainable.
Below is an overview of the key components and functionalities of the project:

### Package Structure
- **Commands**: Contains classes for different operations (commands) that can be performed by the application, such as `InsertCommand`, `DeleteCommand`, `ListCommand`, `MigrateCommand`, and `WebCommand`. These commands implement a common interface `Command` that defines the `execute` method.
- **Data**: Handles data persistence and retrieval. It includes classes for working with different data sources (`CsvDataSource`, `JsonDataSource`, `ApiDataSource`), a factory class `DataSourceFactory` to create data source instances, and interfaces `Readable`, `Writable`, and `Transactional` for defining common data operation methods.
- **Model**: Contains the `Task` class, which represents a todo task with properties for content and status.
- **Utilities**: Provides utility classes like `Logger` interface and its implementation `ConsoleLogger` for logging messages and errors.

### Core Classes and Interfaces
- **`Command` interface**: Defines the structure for command classes with methods to execute a command and get the command's name.
- **`CommandExecutor`**: Not part of the initial design, but it seems to resemble a class that would execute commands directly on a list of tasks and a todo file handler.
- **`CommandFactory`**: Responsible for initializing and providing instances of commands based on a command name. It uses a static block to register commands and a map to link command names to their constructors.
- **`DataSourceFactory`**: Factory class for creating data source objects that can read or write task data to/from different formats (CSV, JSON, API).
- **`Task` class**: Represents a todo task with properties for the task content and status. It uses annotations for JSON serialization and deserialization.
- **`TodoFileHandler`**: Handles reading from and writing to the todo task file. It uses `ObjectMapper` from Jackson library for JSON processing.

### Commands Functionality
- **Insert**: Adds a new task to the task list and persists it to the specified data source.
- **Delete**: Removes a task from the task list by index.
- **List**: Displays all tasks or filtered tasks based on the command arguments (e.g., only tasks marked as done).
- **Migrate**: Moves tasks between different data sources (e.g., from CSV to JSON).
- **Web**: Starts a simple HTTP server that provides endpoints to add and list tasks through HTTP requests.

### Web Command and HTTP Server
- The `WebCommand` class sets up an HTTP server that listens for GET and POST requests. GET requests fetch a list of tasks, while POST requests allow adding a new task. This functionality demonstrates integrating a simple web interface with the backend task management system.

### Entry Point (`App.java`)
- The main class `App` parses command-line arguments to determine the operation to perform and the source file for tasks. It initializes dependencies and executes the requested command using `TodoManager`, which acts as the bridge between commands and data handling.

This project is well-structured and modular, which facilitates the addition of new features and components. Here's how a newcomer can make various additions to the project:

### How to Add a New Command

1. **Implement the Command Interface**: Create a new class in the `com.fges.todoapp.Commands` package that implements the `Command` interface. This class should implement the `execute` method, defining the logic for the new command, and the `getName` method, returning the command name.

2. **Register the Command**: In the `CommandFactory` class, add a new entry to the `commandMap` static block. This entry should map a string (the command name) to a lambda expression or method reference that instantiates the new command class.

   ```java
   static {
       // Existing command registrations
       commandMap.put("newCommand", NewCommandClass::new); // Register the new command
   }
   ```

3. **Implement Logic**: Inside your new command class, implement the necessary logic in the `execute` method using the tasks list and command arguments provided.

### How to Add a New File-Based DataSource

1. **Implement Readable and Writable Interfaces**: Create a new class in the `com.fges.todoapp.Data` package that implements both `Readable<T>` and `Writable<T>` interfaces, with `T` being `Task`. If transactions are needed, also implement the `Transactional` interface.

2. **Implement Data Operations**: Implement the methods required by these interfaces to handle reading from and writing to the new file format, including any transaction-related methods if applicable.

3. **Register the DataSource**: Modify the `DataSourceFactory` class to recognize the new data source type. In the `createDataSource` method, add a new case to the switch statement that returns an instance of the new data source class when the specified type matches.

   ```java
   case "newFileType" -> new NewFileTypeDataSource(filePath);
   ```

### How to Add a New Non-File-Based DataSource

1. **Implement the Interfaces**: Similar to the file-based data source, create a class that implements `Readable<T>` and `Writable<T>`, tailored to the non-file-based storage mechanism (e.g., an API or database). Implement the necessary logic for reading from and writing to the data source.

2. **Handle Connection Details**: Ensure your data source class handles any connection or communication specifics, such as API endpoints or database connection strings.

3. **Modify DataSourceFactory**: Update `DataSourceFactory` to include your new data source type, as demonstrated for file-based sources.

### How to Add a New Attribute to a Todo

1. **Modify the Task Class**: Add the new attribute to the `Task` class with appropriate data type. For instance, to add a priority attribute:

   ```java
   private String priority; // Example attribute
   ```

2. **Update Constructor and Methods**: Modify the constructor of the `Task` class to accept the new attribute. Also, update getters and setters accordingly.

3. **Adjust JSON Annotations**: If the attribute needs to be serialized/deserialized, update the `@JsonCreator` annotation and add `@JsonProperty` or `@JsonAlias` annotations as needed.

4. **Handle Data Sources**: Ensure all data source classes (`JsonDataSource`, `CsvDataSource`, etc.) properly handle the new attribute during read/write operations.

### How to Add a New Interface to the Project

1. **Define the Interface**: In the appropriate package, define a new interface. For example, to add an interface that defines a new type of behavior (e.g., notification sending), you might create it in the `com.fges.todoapp.Utilities` package.

2. **Implement the Interface**: Create classes that implement this new interface, providing concrete implementations for its methods.

3. **Integration**: Depending on the interface's purpose, integrate its use into relevant parts of the application. For example, if adding a notification interface, you might call it in command classes after performing an action.

![Tp finale.svg](..%2F..%2F..%2F..%2F..%2F..%2FDownloads%2FTp%20finale.svg)
![image](https://github.com/Elie-jbeili/-Design-Pattern-10/assets/117976718/a8cf6690-b7f5-4102-ae19-5efe7ae697de)


It's my first year at this university, and the program at my previous institution was vastly different from the one here. Consequently, I'm not very proficient in coding with Java, which made it quite challenging for me. Unfortunately, I didn't receive much assistance with this project, so I had to rely on my own efforts. Despite my best attempts, the project didn't yield successful results, but I gave it my all.
