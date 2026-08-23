import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
// comment to allow for merge commit
/**
 * Starts the Friday chatbot application.
 */

public class Friday {
    public static void main(String[] args) {
        Ui ui = new Ui();
        Path storagePath = Path.of("data", "tasks.txt");
        Storage storage = new Storage(storagePath);
        ArrayList<Task> tasks;
        try {
            tasks = storage.load();
        } catch (IOException exception) {
            ui.showLine("Cannot load tasks leh: " + exception.getMessage());
            tasks = new ArrayList<>();
        }

        ui.showWelcome();

        String input = ui.readCommand();
        while (!input.equals("bye")) {
            ui.showSeparator();
            try {
                if (input.isBlank()) {
                    throw new FridayException("Please enter a command.");
                } else if (input.equals("list")) {
                    ui.showLine("Here are your tasks:");
                    if (tasks.isEmpty()) {
                        ui.showLine("No tasks yet.");
                    } else {
                        for (int i = 0; i < tasks.size(); i++) {
                            ui.showLine((i + 1) + "." + tasks.get(i));
                        }
                    }
                } else if (input.equals("mark") || input.startsWith("mark ")) {
                    int taskNumber = getTaskNumber(input, "mark", tasks.size());
                    int taskIndex = taskNumber - 1;
                    tasks.get(taskIndex).markAsDone();

                    ui.showLine("Good! This task done liao: ");
                    ui.showLine(tasks.get(taskIndex).toString());

                } else if (input.equals("unmark") || input.startsWith("unmark ")) {
                    int taskNumber = getTaskNumber(input, "unmark", tasks.size());
                    int taskIndex = taskNumber - 1;
                    tasks.get(taskIndex).markAsUndone();

                    ui.showLine("Never mind! Can do later: ");
                    ui.showLine(tasks.get(taskIndex).toString());

                } else if (input.equals("delete") || input.startsWith("delete ")) {
                    int taskNumber = getTaskNumber(input, "delete", tasks.size());
                    int taskIndex = taskNumber - 1;
                    Task deletedTask = tasks.remove(taskIndex);

                    ui.showLine("Okay, I removed this task:");
                    ui.showLine(deletedTask.toString());
                    ui.showLine("you have " + tasks.size() + " tasks lah.");

                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    String task = input.substring("todo".length()).trim();
                    if (task.isEmpty()) {
                        throw new FridayException("todo need description leh.");
                    }
                    ui.showLine("okay okay, i add " + task + " to the list lor.");
                    tasks.add(new Todo(task));
                    ui.showLine("you have " + tasks.size() + " tasks lah.");

                } else if (input.equals("event") || input.startsWith("event ")) {
                    String task = input.substring("event".length()).trim();
                    if (task.isEmpty()) {
                        throw new FridayException("what event ah?");
                    }
                    int fromIndex = task.indexOf("/from ");
                    int toIndex = task.indexOf("/to ", fromIndex + 6);
                    if (fromIndex < 0 || toIndex < 0) {
                        throw new FridayException("ARE YOU DONE?! write like this lah: event DESCRIPTION /from START /to END");
                    }
                    String description = task.substring(0, fromIndex).trim();
                    String start = task.substring(fromIndex + 6, toIndex).trim();
                    String end = task.substring(toIndex + 4).trim();
                    if (description.isEmpty() || start.isEmpty() || end.isEmpty()) {
                        throw new FridayException("Tolong, an event's description, start, and end cannot be empty lei.");
                    }
                    String[] eventFields = {description, start, end};
                    ui.showLine("orh, don't forget to attend ah: ");
                    Task event = new Event(eventFields);
                    tasks.add(event);
                    ui.showLine(event.toString());
                    ui.showLine("you have " + tasks.size() + " tasks lah.");

                } else if (input.equals("on") || input.startsWith("on ")) {
                    String dateText = input.substring("on".length()).trim();
                    if (dateText.isEmpty()) {
                        throw new FridayException(
                                "Your date must write like yyyy-mm-dd, e.g. 2019-10-15.");
                    }

                    LocalDate requestedDate = LocalDate.parse(dateText);
                    boolean foundDeadline = false;
                    ui.showLine("Your deadlines on " + requestedDate + " ah:");
                    for (int i = 0; i < tasks.size(); i++) {
                        Task task = tasks.get(i);
                        if (task instanceof Deadline deadline
                                && deadline.getDeadlineDate().equals(requestedDate)) {
                            ui.showLine((i + 1) + "." + deadline);
                            foundDeadline = true;
                        }
                    }
                    if (!foundDeadline) {
                        ui.showLine("Got nothing due. Heng ah!");
                    }

                } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                    String task = input.substring("deadline".length()).trim();
                    if (task.isEmpty()) {
                        throw new FridayException("What thing got deadline ah?");
                    }
                    int byIndex = task.indexOf("/by ");
                    if (byIndex < 0) {
                        throw new FridayException("Tolong, write this format: deadline DESCRIPTION /by DATE");
                    }
                    String description = task.substring(0, byIndex).trim();
                    String date = task.substring(byIndex + 4).trim();
                    if (description.isEmpty() || date.isEmpty()) {
                        throw new FridayException("Deadline's description and date must fill up lei.");
                    }
                    String[] deadlineFields = {description, date};
                    Task deadline = new Deadline(deadlineFields);
                    tasks.add(deadline);
                    ui.showLine("Remember to finish hor: ");
                    ui.showLine(deadline.toString());
                    ui.showLine("you have " + tasks.size() + " tasks lah.");

                } else {
                    throw new FridayException("Eh? Sorry i don't understand that bro :-(");
                }
            } catch (DateTimeParseException exception) {
                ui.showLine("SIALA!!! Please enter dates as yyyy-mm-dd, e.g. 2019-10-15.");
            } catch (FridayException exception) {
                ui.showLine("SIALA!!! " + exception.getMessage());
            }

            ui.showSeparator();
            input = ui.readCommand();
        }
        ui.close();

        ui.showGoodbye();

        try {
            storage.save(tasks);
        } catch (IOException exception) {
            ui.showLine("Could not save tasks: " + exception.getMessage());
        }
    }

    /**
     * Extracts and validates the task number supplied to a numbered command.
     *
     * @param input full command entered by the user
     * @param command command name, such as mark, unmark, or delete
     * @param taskCount number of tasks currently stored
     * @return the one-based task number
     * @throws FridayException if the number is missing, invalid, or out of range
     */
    private static int getTaskNumber(String input, String command, int taskCount)
            throws FridayException {
        String numberText = input.substring(command.length()).trim();
        if (numberText.isEmpty()) {
            throw new FridayException("Please specify a task number.");
        }

        try {
            int taskNumber = Integer.parseInt(numberText);
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new FridayException("That task number does not exist.");
            }
            return taskNumber;
        } catch (NumberFormatException exception) {
            throw new FridayException("The task number must be a whole number.");
        }
    }
}
