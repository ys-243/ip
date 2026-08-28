package friday.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import friday.task.Deadline;
import friday.task.Event;
import friday.task.Task;
import friday.task.TaskList;
import friday.task.Todo;

/** Tests loading and saving tasks. */
class StorageTest {

    @Test
    void load_missingFile_returnsEmptyList(@TempDir Path temporaryDirectory) throws Exception {
        Storage storage = new Storage(temporaryDirectory.resolve("missing.txt"));

        assertTrue(storage.load().isEmpty());
    }

    @Test
    void saveAndLoad_multipleTaskTypes_preservesTasks(@TempDir Path temporaryDirectory)
            throws Exception {
        Storage storage = new Storage(temporaryDirectory.resolve("data/tasks.txt"));
        TaskList originalTasks = new TaskList();
        Todo todo = new Todo("buy milk, eggs");
        todo.markAsDone();
        originalTasks.add(todo);
        originalTasks.add(new Deadline(new String[] {"submit report", "2026-08-30"}));
        originalTasks.add(new Event(new String[] {"meeting", "2pm", "3pm"}));

        storage.save(originalTasks);
        List<String> loadedTasks = storage.load().stream().map(Task::toString).toList();

        assertEquals(List.of(
                "[T][X] buy milk, eggs",
                "[D][ ] submit report (by: Aug 30 2026)",
                "[E][ ] meeting (from: 2pm to: 3pm)"), loadedTasks);
    }

    @Test
    void load_malformedAndValidRecords_ignoresMalformedRecords(@TempDir Path temporaryDirectory)
            throws Exception {
        Path file = temporaryDirectory.resolve("tasks.txt");
        Files.writeString(file, "broken record\n[T],0,valid todo\n[D],0,bad date,soon\n",
                StandardCharsets.UTF_8);

        List<Task> tasks = new Storage(file).load();

        assertEquals(1, tasks.size());
        assertEquals("[T][ ] valid todo", tasks.get(0).toString());
    }
}
