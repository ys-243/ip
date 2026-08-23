/** Represents the command that ends the Friday application. */
public class ExitCommand extends Command {
    /** Creates an exit command. */
    public ExitCommand() {
        super(Type.BYE, "bye");
    }

    /** Exit commands do not modify the task list or display a response. */
    @Override
    public void execute(TaskList tasks, Ui ui) {
        // Friday displays the farewell after observing isExit().
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
