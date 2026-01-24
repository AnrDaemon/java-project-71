package hexlet.code;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.BeforeAll;

public class FileReadingTest {

    private static ClassLoader loader;

    @BeforeAll
    static void init() {
        loader = DifferTest.class.getClassLoader();
    }

    protected File getResourceFile(String resource) {
        return new File(loader.getResource(resource).getFile());
    }

    protected String readFixture(String resource) throws IOException {
        var file = getResourceFile(resource).toPath();
        return Files.readString(file);
    }

}
