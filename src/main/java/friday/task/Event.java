package friday.task;

/** Represents a task occurring between a start and end time. */
public class Event extends Task {
    protected String start;
    protected String end;

    /**
     * Creates an event task from its description, start time, and end time.
     *
     * @param event description, start time, and end time
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

    @Override
    public String toString() {
        return super.toString()
                + " (from: " + start + " to: " + end + ")";
    }

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
