package friday.parser;

import friday.command.Command;
import friday.command.ExitCommand;

/** Converts raw user input into a recognized command. */
public class Parser {
    /** Temporary concrete command used while individual commands are extracted. */
    private static class ParsedCommand extends Command {
        ParsedCommand(Type type, String input) {
            super(type, input);
        }
    }

    /**
     * Identifies the command word while preserving the complete input for
     * command-specific argument validation.
     *
     * @param input normalized input read from the user
     * @return command representing the input
     */
    public static Command parse(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Command input cannot be null.");
        }
        if (input.isBlank()) {
            return new ParsedCommand(Command.Type.EMPTY, input);
        }

        int firstSpace = input.indexOf(' ');
        String commandWord = firstSpace < 0 ? input : input.substring(0, firstSpace);
        Command.Type type = switch (commandWord) {
            case "bye" -> input.equals("bye") ? Command.Type.BYE : Command.Type.UNKNOWN;
            case "list" -> input.equals("list") ? Command.Type.LIST : Command.Type.UNKNOWN;
            case "mark" -> Command.Type.MARK;
            case "unmark" -> Command.Type.UNMARK;
            case "delete" -> Command.Type.DELETE;
            case "todo" -> Command.Type.TODO;
            case "event" -> Command.Type.EVENT;
            case "on" -> Command.Type.ON_DATE;
            case "deadline" -> Command.Type.DEADLINE;
            default -> Command.Type.UNKNOWN;
        };
        return type == Command.Type.BYE
                ? new ExitCommand()
                : new ParsedCommand(type, input);
    }
}
