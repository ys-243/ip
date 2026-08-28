package friday.task;

/** Represents a task stored by the Friday application. */
public class Task {
    protected String description;
    protected boolean isDone;
    protected String type;

    /** Creates an undone task with the specified description and type marker. */
    public Task(String description, String type) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Task description cannot be empty.");
        }
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsUndone() {
        isDone = false;
    }

    /** Returns a display-friendly representation of this task. */
    @Override
    public String toString() {
        return type + (isDone ? "[X] " : "[ ] ") + description;
    }

    /** Returns this task encoded for the line-based save file. */
    public String toFileString() {
        return type + "," + (isDone ? "1" : "0")
                + "," + escapeFileField(description);
    }

    /**
     * Escapes characters that would otherwise damage the line-based save format.
     *
     * @param value Field to store.
     * @return Escaped field suitable for the save file.
     */
    protected static String escapeFileField(String value) {
        return value.replace("\\", "\\\\")
                .replace(",", "\\,")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
