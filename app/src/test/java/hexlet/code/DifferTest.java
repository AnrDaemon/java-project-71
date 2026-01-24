package hexlet.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.rootdir.hexlet.java.m2k.NodeStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DifferTest extends FileReadingTest {

    @Test
    void diffAddedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{}");
        var right = mapper.readTree("{\"added\":\"yes\"}");
        var diff = Differ.fromParsed(left, right).parse().diff();
        var expected = new NodeStatus("added", null, right.get("added"), NodeStatus.ADDED);
        assertEquals(expected, diff.get(0));
    }

    @Test
    void diffUpdatedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"updated\":\"no\"}");
        var right = mapper.readTree("{\"updated\":\"yes\"}");
        var diff = Differ.fromParsed(left, right).parse().diff();
        var expected = new NodeStatus("updated", left.get("updated"), right.get("updated"), NodeStatus.UPDATED);
        assertEquals(expected, diff.get(0));
    }

    @Test
    void diffRemovedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"removed\":\"yes\"}");
        var right = mapper.readTree("{}");
        var diff = Differ.fromParsed(left, right).parse().diff();
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

    @Test
    void diffGenerationStylishTest() throws Exception {
        var file1 = getResourceFile("file1-flat.json").getAbsolutePath();
        var file2 = getResourceFile("file2-flat.json").getAbsolutePath();
        var diff = Differ.generate(file1, file2, "stylish");
        var expected = readFixture("expected-flat.stylish.txt");
        assertEquals(expected, diff);
    }

    @Test
    void diffGenerationPlainTest() throws Exception {
        var file1 = getResourceFile("file1-flat.json").getAbsolutePath();
        var file2 = getResourceFile("file2-flat.json").getAbsolutePath();
        var diff = Differ.generate(file1, file2, "plain");
        var expected = readFixture("expected-flat.plain.txt");
        assertEquals(expected, diff);
    }

    @Test
    void diffGenerationJsonTest() throws Exception {
        var file1 = getResourceFile("file1-flat.json").getAbsolutePath();
        var file2 = getResourceFile("file2-flat.json").getAbsolutePath();
        var diff = Differ.generate(file1, file2, "json");
        var expected = readFixture("expected-flat.json.txt");
        assertEquals(expected, diff);
    }
}
