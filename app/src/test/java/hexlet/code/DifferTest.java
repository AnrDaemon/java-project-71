package hexlet.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.rootdir.hexlet.java.m2k.NodeStatus;
import org.rootdir.hexlet.java.m2k.NonRecursiveFlattener;
import org.rootdir.hexlet.java.m2k.TreeDiffer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DifferTest extends FileReadingTest {

    @Test
    void diffAddedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{}");
        var right = mapper.readTree("{\"added\":\"yes\"}");
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var expected = new NodeStatus("added", null, right.get("added"), NodeStatus.ADDED);
        assertEquals(expected, diff.get(0));
    }

    @Test
    void diffUpdatedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"updated\":\"no\"}");
        var right = mapper.readTree("{\"updated\":\"yes\"}");
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var expected = new NodeStatus("updated", left.get("updated"), right.get("updated"), NodeStatus.UPDATED);
        assertEquals(expected, diff.get(0));
    }

    @Test
    void diffRemovedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"removed\":\"yes\"}");
        var right = mapper.readTree("{}");
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var expected = new NodeStatus("removed", left.get("removed"), null, NodeStatus.REMOVED);
        assertEquals(expected, diff.get(0));
    }

    @Test
    void diffGeneration2PathsTest() throws Exception {
        var file1 = getResourceFile("file1-flat.json").getAbsolutePath();
        var file2 = getResourceFile("file2-flat.json").getAbsolutePath();
        var diff = Differ.generate(file1, file2);
        var expected = readFixture("expected-flat.stylish.txt");
        assertEquals(expected, diff);
    }

    @Test
    void diffGeneration2PathsYamlTest() throws Exception {
        var file1 = getResourceFile("file1-flat.yaml").getAbsolutePath();
        var file2 = getResourceFile("file2-flat.yaml").getAbsolutePath();
        var diff = Differ.generate(file1, file2);
        var expected = readFixture("expected-flat.stylish.txt");
        assertEquals(expected, diff);
    }

    @Test
    void diffGeneration2PathsYmlTest() throws Exception {
        var file1 = getResourceFile("file1-flat.yml").getAbsolutePath();
        var file2 = getResourceFile("file2-flat.yml").getAbsolutePath();
        var diff = Differ.generate(file1, file2);
        var expected = readFixture("expected-flat.stylish.txt");
        assertEquals(expected, diff);
    }

    @Test
    void diffGenerationFormatMismatchTest() {
        var file1 = getResourceFile("file1-flat.json").getAbsolutePath();
        var file2 = getResourceFile("file2-flat.yaml").getAbsolutePath();

        assertThrows(Exception.class, () -> {
            Differ.generate(file1, file2);
        });
    }

    static Stream<Arguments> diffGenerationFormatSource() {
        return Stream.of(Arguments.of("file1-flat.json", "file2-flat.json", "stylish", "expected-flat.stylish.txt"), //
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
