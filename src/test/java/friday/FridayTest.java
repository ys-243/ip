package friday;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests the Friday application entry point. */
class FridayTest {

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
