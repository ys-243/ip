public class Deadline extends Task {
    protected String end;

    public Deadline(String[] Deadline) {
        super(Deadline[0], "[D]");
        end = Deadline[1].startsWith("by ") ? Deadline[1].substring(3).trim()
                                            : Deadline[1];
    }

    @Override
    public String toString() {
        return super.toString()
                + "(by: " + end + ")";
    }
}
