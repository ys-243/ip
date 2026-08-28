package friday.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be completed by a specified date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    /** Due date of this deadline. */
    protected LocalDate end;

    /**
     * Creates a deadline task from its description and due date.
     *
     * @param deadline Fields containing the description and an ISO-8601 due date.
     * @throws IllegalArgumentException If a required field is absent or blank.
     * @throws java.time.format.DateTimeParseException If the due date is invalid.
     */
    public Deadline(String[] deadline) {
        super(requireField(deadline, 0, "description"), "[D]");
        String endValue = requireField(deadline, 1, "deadline");
        String dateText = endValue.startsWith("by ") ? endValue.substring(3).trim() : endValue;
        end = LocalDate.parse(dateText);
    }

    /**
     * Returns the display representation of this deadline.
     *
     * @return Display representation including the formatted due date.
     */
    @Override
    public String toString() {
        return super.toString()
                + " (by: " + end.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Returns the representation stored in the task data file.
     *
     * @return Escaped, comma-separated representation of the deadline.
     */
    @Override
    public String toFileString() {
        return type + "," + (isDone ? "1" : "0")
                    + "," + escapeFileField(description)
                    + "," + escapeFileField(end.toString());
    }

    /**
     * Returns the date on which this task is due.
     *
     * @return Deadline date.
     */
    public LocalDate getDeadlineDate() {
        return end;
    }

    private static String requireField(String[] fields, int index, String name) {
        if (fields == null || fields.length <= index || fields[index] == null
                || fields[index].isBlank()) {
            throw new IllegalArgumentException("Deadline " + name + " cannot be empty.");
        }
        return fields[index];
    }
}
