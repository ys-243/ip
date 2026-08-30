package friday;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import friday.command.Command;
import friday.parser.Parser;
import friday.storage.Storage;
import friday.task.TaskList;
import friday.ui.Ui;

/**
 * Coordinates Friday's commands, tasks, storage, and user interfaces.
 */
public class Friday {
    private static final Path DEFAULT_STORAGE_PATH = Path.of("data", "tasks.txt");

    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates Friday using the default task storage file.
     */
    public Friday() {
        this(DEFAULT_STORAGE_PATH);
    }

    /**
     * Creates Friday using the specified task storage file.
     *
     * @param storagePath Path used to load and save tasks.
     */
    public Friday(Path storagePath) {
        storage = new Storage(storagePath);
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (IOException exception) {
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    /**
     * Executes one user command and returns Friday's response.
     *
     * @param input Command entered by the user.
     * @return Friday's response to the command.
     */
    public String getResponse(String input) {
        ByteArrayOutputStream responseBytes = new ByteArrayOutputStream();
        try (PrintStream responseOutput = new PrintStream(responseBytes, true, StandardCharsets.UTF_8)) {
            Ui responseUi = new Ui(responseOutput);
            Command command = Parser.parse(input.trim());
            if (command.isExit()) {
                responseUi.showLine("Bye. See you next time lah!");
            } else {
                command.execute(tasks, responseUi);
                saveTasks(responseUi);
            }
        }
        return responseBytes.toString(StandardCharsets.UTF_8).stripTrailing();
    }

    /**
     * Starts Friday's original console interface.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        Friday friday = new Friday();
        friday.runConsole();
    }

    private void runConsole() {
        Ui ui = new Ui();
        ui.showWelcome();
        Command command = Parser.parse(ui.readCommand());
        while (!command.isExit()) {
            ui.showSeparator();
            command.execute(tasks, ui);
            ui.showSeparator();
            command = Parser.parse(ui.readCommand());
        }
        ui.close();
        ui.showGoodbye();
        saveTasks(ui);
    }

    private void saveTasks(Ui ui) {
        try {
            storage.save(tasks);
        } catch (IOException exception) {
            ui.showLine("Could not save tasks: " + exception.getMessage());
        }
    }
}
