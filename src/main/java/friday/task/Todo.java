package friday.task;

/**
 * Represents a task without a date or time.
 */
public class Todo extends Task {

    /**
     * Creates a to-do task with the specified description.
     *
     * @param description Description of the task.
     * @throws IllegalArgumentException If description is {@code null} or blank.
     */
    public Todo(String description) {
        super(description, "[T]");
    }

    /**
     * Returns the display representation of this to-do task.
     *
     * @return Display representation of the task.
     */
    @Override
    public String toString() {
        return super.toString();
    }

}
