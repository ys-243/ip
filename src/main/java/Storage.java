import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* Format:
    Todo_task: type,isdone, description,
    Deadline_task: type, isdone, description, by
    Event_task: type, isdone, description, from, to
 */

public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    public ArrayList<Task> load() throws IOException {
        //read file
        //convert line to Task object
        //return ArrayList
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(filePath);
        if (lines.isEmpty()) {
            return tasks;
        }

        for (String line : lines) {
            String[] parts = line.split(",");
            Task task;
            switch (parts[0]) {
                case "[T]" -> task = new Todo(parts[2]);
                case "[D]" -> task = new Deadline(Arrays.copyOfRange(parts, 2, parts.length));
                case "[E]" -> task = new Event(Arrays.copyOfRange(parts, 2, parts.length));
                default -> {
                    continue;
                }
            }

            if (parts[1].equals("1")) {
                task.markAsDone();
            }
            tasks.add(task);
        }
        return tasks;
    }

    public void save(ArrayList<Task> tasks) throws IOException {
        //convert each Task object to comma separated Strings
        //write String to file
        ArrayList<String> lines = new ArrayList<>();

        for (Task task : tasks) {
            lines.add(task.toFileString());
        }

        Files.write(filePath, lines);
    }
}
