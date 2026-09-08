package friday.task;

/**
 * Represents a task occurring between a start and end time.
 */
public class Event extends Task {
    private static final String START_PREFIX = "from ";
    private static final String END_PREFIX = "to ";

    private final String start;
    private final String end;

    /**
     * Creates an event from its description, start, and end values.
     *
     * @param event Fields containing the description, start, and end values.
     * @throws IllegalArgumentException If a required field is absent or blank.
     */
    public Event(String[] event) {
        super(requireField(event, 0, "Event", "description"), "[E]");
        String startValue = requireField(event, 1, "Event", "start");
        String endValue = requireField(event, 2, "Event", "end");
        start = startValue.startsWith(START_PREFIX)
                ? startValue.substring(START_PREFIX.length()).trim()
                : startValue;
        end = endValue.startsWith(END_PREFIX)
                ? endValue.substring(END_PREFIX.length()).trim()
                : endValue;
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
        return super.toFileString()
                + "," + escapeFileField(start)
                + "," + escapeFileField(end);
    }

}
