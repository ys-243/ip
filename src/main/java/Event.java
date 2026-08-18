public class Event extends Task{
    protected String start;
    protected String end;

    public Event(String[] event) {
        super(event[0], "[E]");
        start = event[1].startsWith("from ") ? event[1].substring(5).trim() : event[1];
        end = event[2].startsWith("to ") ? event[2].substring(3).trim() : event[2];
    }

    @Override
    public String toString() {
        return super.toString()
                + "(from: " + start + " to: " + end + ")";
    }
}
