package friday.task;

/**
 * Represents a task stored by the Friday application.
 */
public class Task {
    /** Human-readable description of this task. */
    protected String description;
    /** Whether this task has been completed. */
    protected boolean isDone;
    /** Marker identifying this task's type. */
    protected String type;

    /**
     * Creates a task with the specified description and type marker.
     *
     * @param description Description of the task.
     * @param type Marker identifying the task type.
     * @throws IllegalArgumentException If description is {@code null} or blank.
     */
    public Task(String description, String type) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Task description cannot be empty.");
        }
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsUndone() {
        isDone = false;
    }

    /**
     * Returns the display representation of this task.
     *
     * @return Type, completion status, and description of the task.
     */
    @Override
    public String toString() {
        return type + (isDone ? "[X] " : "[ ] ") + description;
    }

    /**
     * Returns the representation stored in the task data file.
     *
     * @return Escaped, comma-separated representation of the task.
     */
    public String toFileString() {
        return type + "," + (isDone ? "1" : "0")
                    + "," + escapeFileField(description);
    }

    /**
     * Escapes characters that would otherwise damage the line-based save format.
     *
     * @param value field to store
     * @return escaped field suitable for the save file
     */
    protected static String escapeFileField(String value) {
        return value.replace("\\", "\\\\")
                .replace(",", "\\,")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
