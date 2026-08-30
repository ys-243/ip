package friday;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests the Friday application entry point. */
class FridayTest {

    @Test
    void getResponse_addThenListTask_returnsTaskAndPersistsIt(@TempDir Path temporaryDirectory) {
        Path storagePath = temporaryDirectory.resolve("tasks.txt");
        Friday friday = new Friday(storagePath);

        friday.getResponse("todo read book");
        String response = friday.getResponse("list");

        assertTrue(response.contains("[T][ ] read book"));
        Friday reloadedFriday = new Friday(storagePath);
        assertTrue(reloadedFriday.getResponse("list").contains("[T][ ] read book"));
    }

    @Test
    void getResponse_byeCommand_returnsGoodbye(@TempDir Path temporaryDirectory) {
        Friday friday = new Friday(temporaryDirectory.resolve("tasks.txt"));

        assertEquals("Bye. See you next time lah!", friday.getResponse("bye"));
    }

    @Test
    void main_byeCommand_exitsSuccessfully(@TempDir Path temporaryDirectory) throws Exception {
        String javaExecutable = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        Process process = new ProcessBuilder(javaExecutable, "-cp",
                System.getProperty("java.class.path"), Friday.class.getName())
                .directory(temporaryDirectory.toFile())
                .redirectErrorStream(true)
                .start();

        process.getOutputStream().write("bye\n".getBytes(StandardCharsets.UTF_8));
        process.getOutputStream().close();
        process.getInputStream().readAllBytes();

        assertEquals(0, process.waitFor());
    }
}
