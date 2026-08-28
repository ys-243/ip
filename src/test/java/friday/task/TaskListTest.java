package friday.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Tests operations on the application's task collection. */
class TaskListTest {

    @Test
    void add_validTask_increasesSize() {
        TaskList tasks = new TaskList();

        tasks.add(new Todo("read book"));

        assertEquals(1, tasks.size());
    }

    @Test
    void delete_existingTask_removesAndReturnsTask() {
        TaskList tasks = new TaskList();
        Todo first = new Todo("first");
        tasks.add(first);
        tasks.add(new Todo("second"));

        Task deleted = tasks.delete(0);

        assertSame(first, deleted);
        assertEquals("[T][ ] second", tasks.get(0).toString());
        assertEquals(1, tasks.size());
    }

    @Test
    void add_nullTask_exceptionThrown() {
        TaskList tasks = new TaskList();

        assertThrows(IllegalArgumentException.class, () -> tasks.add(null));
    }

    @Test
    void iterator_removeAttempt_unsupportedOperationExceptionThrown() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        var iterator = tasks.iterator();
        iterator.next();

        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    void find_keywordInMultipleDescriptions_returnsAllMatchesInListOrder() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("buy groceries"));
        tasks.add(new Todo("return book"));

        var matches = tasks.find("book");

        assertEquals(2, matches.size());
        assertEquals("[T][ ] read book", matches.get(0).toString());
        assertEquals("[T][ ] return book", matches.get(1).toString());
    }

    @Test
    void find_missingKeyword_returnsEmptyList() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        assertEquals(0, tasks.find("movie").size());
    }
}
