import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Represents a task that must be completed by a specified date. */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    protected LocalDate end;

    public Deadline(String[] Deadline) {
        super(requireField(Deadline, 0, "description"), "[D]");
        String endValue = requireField(Deadline, 1, "deadline");
        String dateText = endValue.startsWith("by ") ? endValue.substring(3).trim() : endValue;
        end = LocalDate.parse(dateText);
    }

    @Override
    public String toString() {
        return super.toString()
                + " (by: " + end.format(DISPLAY_FORMAT) + ")";
    }

    @Override
    public String toFileString() {
        return type + "," + (isDone ? "1" : "0")
                    + "," + escapeFileField(description)
                    + "," + escapeFileField(end.toString());
    }

    /**
     * Returns the date on which this task is due.
     *
     * @return deadline date
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
