package hexlet.code;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.BeforeAll;

public abstract class FileReadingTest {

    private static ClassLoader loader;

    @BeforeAll
    static void init() {
        loader = FileReadingTest.class.getClassLoader();
    }

    /**
     * Get a path to a resource file in the src/test/resources directory.
     *
     * @param resource Resource name.
     * @return File Resource file.
     */
    protected File getResourceFile(String resource) {
        return new File(loader.getResource(resource).getFile());
    }

    /**
     * Read a resource file from the src/test/resources directory.
     *
     * @param resource Resource name.
     * @return String Fixture contents.
     */
    protected String readFixture(String resource) throws IOException {
        var file = getResourceFile(resource).toPath();
        return Files.readString(file);
    }

}
