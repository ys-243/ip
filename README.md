# Friday

Friday is a command-line task manager written in Java. It keeps track of todos,
deadlines, and events through a conversational interface.

## Current features

- Add todos, deadlines, and events.
- List all tasks and their completion status.
- Mark tasks as done or not done.
- Delete tasks.
- Find deadlines due on a specific date.
- Save tasks to `data/tasks.txt` when the application exits and load them again
  the next time it starts.

## Requirements

- JDK 25
- IntelliJ IDEA (recommended) or a terminal that can run Gradle

## Running Friday

### IntelliJ IDEA

1. Open this project directory in IntelliJ IDEA.
2. Configure the project SDK as **JDK 25** and leave the project language level
   as **SDK default**. See IntelliJ's
   [JDK setup guide](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk) if
   needed.
3. Open `src/main/java/friday/Friday.java`, right-click the file, and select
   **Run 'Friday.main()'**.

### Terminal

On macOS with SDKMAN, select the required Java version first:

```shell
sdk use java 25.0.3.fx-zulu
./gradlew run
```

Enter `bye` to exit cleanly and save the current task list.

## Commands

| Command | Description | Example |
| --- | --- | --- |
| `todo DESCRIPTION` | Add a todo | `todo read chapter 3` |
| `deadline DESCRIPTION /by YYYY-MM-DD` | Add a deadline | `deadline submit report /by 2026-08-30` |
| `event DESCRIPTION /from START /to END` | Add an event | `event project meeting /from 2pm /to 3pm` |
| `list` | Show every task | `list` |
| `mark NUMBER` | Mark a task as done | `mark 1` |
| `unmark NUMBER` | Mark a task as not done | `unmark 1` |
| `delete NUMBER` | Delete a task | `delete 1` |
| `on YYYY-MM-DD` | Show deadlines due on a date | `on 2026-08-30` |
| `bye` | Save tasks and exit | `bye` |

Task numbers are the one-based numbers shown by `list`. Deadline dates must use
the `YYYY-MM-DD` format. Event start and end values are free-form text.

## Testing and building

Run the automated tests with Java 25:

```shell
sdk use java 25.0.3.fx-zulu
./gradlew test
```

Create the executable JAR with:

```shell
./gradlew shadowJar
java -jar build/libs/friday.jar
```

The project uses Gradle, JUnit 5, and the Shadow plugin. Java source files remain
under `src/main/java`, while tests are under `src/test/java`.
