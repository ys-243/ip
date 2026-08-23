import java.io.IOException;
import java.nio.file.Path;

/** Starts and coordinates the Friday chatbot application. */
public class Friday {
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage(Path.of("data", "tasks.txt"));
        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException exception) {
            ui.showLine("Cannot load tasks leh: " + exception.getMessage());
            tasks = new TaskList();
        }

        ui.showWelcome();
        Command command = new Command(ui.readCommand());
        while (!command.isExit()) {
            ui.showSeparator();
            command.execute(tasks, ui);
            ui.showSeparator();
            command = new Command(ui.readCommand());
        }
        ui.close();
        ui.showGoodbye();

        try {
            storage.save(tasks);
        } catch (IOException exception) {
            ui.showLine("Could not save tasks: " + exception.getMessage());
        }
    }
}
