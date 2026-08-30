package friday.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import friday.parser.Parser;
import friday.task.TaskList;
import friday.task.Todo;
import friday.ui.Ui;

/** Tests the default behavior shared by commands. */
class CommandTest {

    /** UI test double that records output instead of printing it. */
    private static class RecordingUi extends Ui {
        private final List<String> messages = new ArrayList<>();

        @Override
        public void showLine(String message) {
            messages.add(message);
        }
    }

    @Test
    void isExit_regularCommand_returnsFalse() {
        Command command = new Command(Command.Type.LIST, "list") { };

        assertFalse(command.isExit());
    }

    @Test
    void execute_todoCommand_addsTodo() {
        TaskList tasks = new TaskList();

        Parser.parse("todo buy milk").execute(tasks, new RecordingUi());

        assertEquals(1, tasks.size());
        assertEquals("[T][ ] buy milk", tasks.get(0).toString());
    }

    @Test
    void execute_markCommand_marksSelectedTask() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("buy milk"));

        Parser.parse("mark 1").execute(tasks, new RecordingUi());

        assertEquals("[T][X] buy milk", tasks.get(0).toString());
    }

    @Test
    void execute_invalidTaskNumber_reportsErrorWithoutChangingTask() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("buy milk"));
        RecordingUi ui = new RecordingUi();

        Parser.parse("mark 2").execute(tasks, ui);

        assertEquals("[T][ ] buy milk", tasks.get(0).toString());
        assertTrue(ui.messages.get(0).contains("does not exist"));
    }

    @Test
    void execute_deadlineCommand_addsFormattedDeadline() {
        TaskList tasks = new TaskList();

        Parser.parse("deadline submit report /by 2026-08-30")
                .execute(tasks, new RecordingUi());

        assertEquals("[D][ ] submit report (by: Aug 30 2026)", tasks.get(0).toString());
    }

    @Test
    void execute_findCommand_displaysAllMatchingTasks() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("buy groceries"));
        tasks.add(new Todo("return book"));
        RecordingUi ui = new RecordingUi();

        Parser.parse("find book").execute(tasks, ui);

        assertEquals(List.of(
                "Okay here's the task ah:",
                "1.[T][ ] read book",
                "2.[T][ ] return book"), ui.messages);
    }

    @Test
    void execute_findWithoutKeyword_reportsError() {
        RecordingUi ui = new RecordingUi();

        Parser.parse("find").execute(new TaskList(), ui);

        assertTrue(ui.messages.get(0).contains("specify a keyword"));
    }
}
