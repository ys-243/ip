import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/* Format:
    Todo_task: type,isdone, description,
    Deadline_task: type, isdone, description, by
    Event_task: type, isdone, description, from, to
 */

/** Loads and saves tasks using a line-based text file. */
public class Storage {
    private final Path filePath;

    public Storage(Path filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("Storage path cannot be null.");
        }
        this.filePath = filePath;
    }

    /**
     * Loads all valid tasks. Blank or malformed lines are ignored so that one
     * damaged record does not prevent the remaining tasks from loading.
     *
     * @return tasks read from the file
     * @throws IOException if the file exists but cannot be read
     */
    public ArrayList<Task> load() throws IOException {
        //read file
        //convert line to Task object
        //return ArrayList
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }

        if (!Files.isRegularFile(filePath)) {
            throw new IOException("Storage path is not a regular file: " + filePath);
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        if (lines.isEmpty()) {
            return tasks;
        }

        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }

            try {
                List<String> parts = parseLine(line);
                Task task = createTask(parts);
                if (task == null) {
                    continue;
                }
                if (parts.get(1).equals("1")) {
                    task.markAsDone();
                }
                tasks.add(task);
            } catch (IllegalArgumentException | DateTimeParseException exception) {
                // Ignore malformed records and continue loading the usable ones.
            }
        }
        return tasks;
    }

    /**
     * Saves all tasks, creating a missing parent directory when necessary.
     *
     * @param tasks tasks to save
     * @throws IOException if the file cannot be written
     */
    public void save(TaskList tasks) throws IOException {
        if (tasks == null) {
            throw new IllegalArgumentException("Task list cannot be null.");
        }

        //convert each Task object to comma separated Strings
        //write String to file
        ArrayList<String> lines = new ArrayList<>();

        for (Task task : tasks) {
            if (task == null) {
                continue;
            }
            lines.add(task.toFileString());
        }

        Path parent = filePath.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.write(filePath, lines, StandardCharsets.UTF_8);
    }

    private Task createTask(List<String> parts) {
        if (parts.size() < 3 || (!parts.get(1).equals("0") && !parts.get(1).equals("1"))) {
            return null;
        }

        return switch (parts.get(0)) {
            case "[T]" -> parts.size() == 3 ? new Todo(parts.get(2)) : null;
            case "[D]" -> parts.size() == 4
                    ? new Deadline(new String[] {parts.get(2), parts.get(3)}) : null;
            case "[E]" -> parts.size() == 5
                    ? new Event(new String[] {parts.get(2), parts.get(3), parts.get(4)}) : null;
            default -> null;
        };
    }

    /** Parses fields while supporting escaped commas, slashes, and line breaks. */
    private List<String> parseLine(String line) {
        ArrayList<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean escaped = false;

        for (char character : line.toCharArray()) {
            if (escaped) {
                switch (character) {
                    case 'n' -> field.append('\n');
                    case 'r' -> field.append('\r');
                    default -> field.append(character);
                }
                escaped = false;
            } else if (character == '\\') {
                escaped = true;
            } else if (character == ',') {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(character);
            }
        }

        if (escaped) {
            throw new IllegalArgumentException("Save record ends with an incomplete escape.");
        }
        fields.add(field.toString());
        return fields;
    }
}
