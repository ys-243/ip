import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/** Owns and provides operations on Friday's collection of tasks. */
public class TaskList implements Iterable<Task> {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing tasks loaded from storage.
     *
     * @param tasks initial tasks
     */
    public TaskList(List<Task> tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("Initial task list cannot be null.");
        }
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null.");
        }
        tasks.add(task);
    }

    /** Removes and returns the task at the given zero-based index. */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /** Returns the task at the given zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }

    /** Returns whether the list contains no tasks. */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /** Allows storage and other clients to visit tasks without exposing the list. */
    @Override
    public Iterator<Task> iterator() {
        return Collections.unmodifiableList(tasks).iterator();
    }
}
