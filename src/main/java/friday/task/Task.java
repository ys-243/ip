package friday.task;

/** Represents a task stored by the Friday application. */
public class Task {
    protected String description;
    protected boolean isDone;
    protected String type;

    public Task(String description, String type) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Task description cannot be empty.");
        }
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsUndone() {
        isDone = false;
    }

    /** Returns whether this task's description contains the given keyword. */
    public boolean descriptionContains(String keyword) {
        if (keyword == null) {
            throw new IllegalArgumentException("Search keyword cannot be null.");
        }
        return description.contains(keyword);
    }

    public String toString() {
        return type + (isDone ? "[X] " : "[ ] ") + description;
    }

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
