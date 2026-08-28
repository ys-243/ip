package friday.task;

/**
 * Represents a task occurring between a start and end time.
 */
public class Event extends Task {
    /** Start value displayed for this event. */
    protected String start;
    /** End value displayed for this event. */
    protected String end;

    /**
     * Creates an event from its description, start, and end values.
     *
     * @param event Fields containing the description, start, and end values.
     * @throws IllegalArgumentException If a required field is absent or blank.
     */
    public Event(String[] event) {
        super(requireField(event, 0, "description"), "[E]");
        String startValue = requireField(event, 1, "start");
        String endValue = requireField(event, 2, "end");
        start = startValue.startsWith("from ") ? startValue.substring(5).trim() : startValue;
        end = endValue.startsWith("to ") ? endValue.substring(3).trim() : endValue;
        if (start.isBlank() || end.isBlank()) {
            throw new IllegalArgumentException("Event start and end cannot be empty.");
        }
    }

    /**
     * Returns the display representation of this event.
     *
     * @return Display representation including the start and end values.
     */
    @Override
    public String toString() {
        return super.toString()
                + " (from: " + start + " to: " + end + ")";
    }

    /**
     * Returns the representation stored in the task data file.
     *
     * @return Escaped, comma-separated representation of the event.
     */
    @Override
    public String toFileString() {
        return type + "," + (isDone ? "1" : "0")
                    + "," + escapeFileField(description)
                    + "," + escapeFileField(start)
                    + "," + escapeFileField(end);
    }

    private static String requireField(String[] fields, int index, String name) {
        if (fields == null || fields.length <= index || fields[index] == null
                || fields[index].isBlank()) {
            throw new IllegalArgumentException("Event " + name + " cannot be empty.");
        }
        return fields[index];
    }
}
