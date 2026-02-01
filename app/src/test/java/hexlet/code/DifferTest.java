package hexlet.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class DifferTest extends FileReadingTest {

    /**
     * Test source data generator.
     *
     * @return Test arguments.
     */
    static Stream<Arguments> diffGeneration2PathsSource() {
        return Stream.of(//
                Arguments.of("file1-flat.json", "file2-flat.json", "expected-flat.stylish.txt"), //
                Arguments.of("file1-flat.yaml", "file2-flat.yaml", "expected-flat.stylish.txt"), //
                Arguments.of("file1-flat.yml", "file2-flat.yml", "expected-flat.stylish.txt") //
        );
    }

    /**
     * Test default generation with 2 paths.
     *
     * @param path1 Left file path.
     * @param path2 Right file path.
     * @param fixture Fixture file name.
     */
    @ParameterizedTest
    @MethodSource("diffGeneration2PathsSource")
    void diffGeneration2PathsTest(String path1, String path2, String fixture) throws Exception {
        var file1 = getResourceFile(path1).getAbsolutePath();
        var file2 = getResourceFile(path2).getAbsolutePath();
        var diff = Differ.generate(file1, file2);
        var expected = readFixture(fixture);
        assertEquals(expected, diff);
    }

    /**
     * Test source data generator.
     *
     * @return Test arguments.
     */
    static Stream<Arguments> diffGenerationFormatSource() {
        return Stream.of(//
                Arguments.of("file1-flat.json", "file2-flat.json", "stylish", "expected-flat.stylish.txt"), //
                Arguments.of("file1-flat.json", "file2-flat.json", "plain", "expected-flat.plain.txt"), //
                Arguments.of("file1-flat.json", "file2-flat.json", "json", "expected-flat.json.txt") //
        );
    }

    /**
     * Test different output formats.
     *
     * @param path1 Left file path.
     * @param path2 Right file path.
     * @param format Output format.
     * @param fixture Fixture file name.
     */
    @ParameterizedTest
    @MethodSource("diffGenerationFormatSource")
    void diffGenerationFormattedTest(String path1, String path2, String format, String fixture) throws Exception {
        var file1 = getResourceFile(path1).getAbsolutePath();
        var file2 = getResourceFile(path2).getAbsolutePath();
        var diff = Differ.generate(file1, file2, format);
        var expected = readFixture(fixture);
        assertEquals(expected, diff);
    }
}
