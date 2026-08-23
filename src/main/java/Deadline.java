/** Represents a task that must be completed by a specified time. */
public class Deadline extends Task {
    protected String end;

    public Deadline(String[] Deadline) {
        super(requireField(Deadline, 0, "description"), "[D]");
        String endValue = requireField(Deadline, 1, "deadline");
        end = endValue.startsWith("by ") ? endValue.substring(3).trim() : endValue;
        if (end.isBlank()) {
            throw new IllegalArgumentException("Deadline cannot be empty.");
        }
    }

    @Override
    public String toString() {
        return super.toString()
                + " (by: " + end + ")";
    }

    @Override
    public String toFileString() {
        return type + "," + (isDone ? "1" : "0")
                    + "," + escapeFileField(description)
                    + "," + escapeFileField(end);
    }

    private static String requireField(String[] fields, int index, String name) {
        if (fields == null || fields.length <= index || fields[index] == null
                || fields[index].isBlank()) {
            throw new IllegalArgumentException("Deadline " + name + " cannot be empty.");
        }
        return fields[index];
    }
}
