import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Represents and executes one command entered by the user. */
public class Command {
    /** Command kinds recognized by the parser. */
    enum Type {
        EMPTY, BYE, LIST, MARK, UNMARK, DELETE, TODO, EVENT, ON_DATE, DEADLINE, UNKNOWN
    }

    private final Type type;
    private final String input;

    /** Creates a typed command from input recognized by the parser. */
    Command(Type type, String input) {
        if (type == null || input == null) {
            throw new IllegalArgumentException("Command type and input cannot be null.");
        }
        this.type = type;
        this.input = input;
    }

    /** Returns whether this command asks Friday to exit. */
    public boolean isExit() {
        return type == Type.BYE;
    }

    /** Executes this command and reports its result without leaking command errors. */
    public void execute(TaskList tasks, Ui ui) {
        try {
            executeCommand(tasks, ui);
        } catch (DateTimeParseException exception) {
            ui.showLine("SIALA!!! Please enter dates as yyyy-mm-dd, e.g. 2019-10-15.");
        } catch (FridayException exception) {
            ui.showLine("SIALA!!! " + exception.getMessage());
        }
    }

    private void executeCommand(TaskList tasks, Ui ui) throws FridayException {
        switch (type) {
        case EMPTY -> throw new FridayException("Please enter a command.");
        case LIST -> showTasks(tasks, ui);
        case MARK -> markTask(tasks, ui);
        case UNMARK -> unmarkTask(tasks, ui);
        case DELETE -> deleteTask(tasks, ui);
        case TODO -> addTodo(tasks, ui);
        case EVENT -> addEvent(tasks, ui);
        case ON_DATE -> showDeadlinesOnDate(tasks, ui);
        case DEADLINE -> addDeadline(tasks, ui);
        case UNKNOWN -> throw new FridayException("Eh? Sorry i don't understand that bro :-(");
        case BYE -> {
            // Exit commands are handled by Friday before execution.
        }
        }
    }

    private void showTasks(TaskList tasks, Ui ui) {
        ui.showLine("Here are your tasks:");
        if (tasks.isEmpty()) {
            ui.showLine("No tasks yet.");
            return;
        }
        for (int i = 0; i < tasks.size(); i++) {
            ui.showLine((i + 1) + "." + tasks.get(i));
        }
    }

    private void markTask(TaskList tasks, Ui ui) throws FridayException {
        int taskIndex = getTaskNumber("mark", tasks.size()) - 1;
        tasks.get(taskIndex).markAsDone();
        ui.showLine("Good! This task done liao: ");
        ui.showLine(tasks.get(taskIndex).toString());
    }

    private void unmarkTask(TaskList tasks, Ui ui) throws FridayException {
        int taskIndex = getTaskNumber("unmark", tasks.size()) - 1;
        tasks.get(taskIndex).markAsUndone();
        ui.showLine("Never mind! Can do later: ");
        ui.showLine(tasks.get(taskIndex).toString());
    }

    private void deleteTask(TaskList tasks, Ui ui) throws FridayException {
        int taskIndex = getTaskNumber("delete", tasks.size()) - 1;
        Task deletedTask = tasks.delete(taskIndex);
        ui.showLine("Okay, I removed this task:");
        ui.showLine(deletedTask.toString());
        ui.showLine("you have " + tasks.size() + " tasks lah.");
    }

    private void addTodo(TaskList tasks, Ui ui) throws FridayException {
        String description = input.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new FridayException("todo need description leh.");
        }
        ui.showLine("okay okay, i add " + description + " to the list lor.");
        tasks.add(new Todo(description));
        ui.showLine("you have " + tasks.size() + " tasks lah.");
    }

    private void addEvent(TaskList tasks, Ui ui) throws FridayException {
        String arguments = input.substring("event".length()).trim();
        if (arguments.isEmpty()) {
            throw new FridayException("what event ah?");
        }
        int fromIndex = arguments.indexOf("/from ");
        int toIndex = arguments.indexOf("/to ", fromIndex + 6);
        if (fromIndex < 0 || toIndex < 0) {
            throw new FridayException(
                    "ARE YOU DONE?! write like this lah: event DESCRIPTION /from START /to END");
        }
        String description = arguments.substring(0, fromIndex).trim();
        String start = arguments.substring(fromIndex + 6, toIndex).trim();
        String end = arguments.substring(toIndex + 4).trim();
        if (description.isEmpty() || start.isEmpty() || end.isEmpty()) {
            throw new FridayException(
                    "Tolong, an event's description, start, and end cannot be empty lei.");
        }
        Task event = new Event(new String[] {description, start, end});
        tasks.add(event);
        ui.showLine("orh, don't forget to attend ah: ");
        ui.showLine(event.toString());
        ui.showLine("you have " + tasks.size() + " tasks lah.");
    }

    private void showDeadlinesOnDate(TaskList tasks, Ui ui) throws FridayException {
        String dateText = input.substring("on".length()).trim();
        if (dateText.isEmpty()) {
            throw new FridayException("Your date must write like yyyy-mm-dd, e.g. 2019-10-15.");
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
    }

    private void addDeadline(TaskList tasks, Ui ui) throws FridayException {
        String arguments = input.substring("deadline".length()).trim();
        if (arguments.isEmpty()) {
            throw new FridayException("What thing got deadline ah?");
        }
        int byIndex = arguments.indexOf("/by ");
        if (byIndex < 0) {
            throw new FridayException("Tolong, write this format: deadline DESCRIPTION /by DATE");
        }
        String description = arguments.substring(0, byIndex).trim();
        String date = arguments.substring(byIndex + 4).trim();
        if (description.isEmpty() || date.isEmpty()) {
            throw new FridayException("Deadline's description and date must fill up lei.");
        }
        Task deadline = new Deadline(new String[] {description, date});
        tasks.add(deadline);
        ui.showLine("Remember to finish hor: ");
        ui.showLine(deadline.toString());
        ui.showLine("you have " + tasks.size() + " tasks lah.");
    }

    private int getTaskNumber(String command, int taskCount) throws FridayException {
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
