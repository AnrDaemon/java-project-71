package hexlet.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.File;
import org.junit.jupiter.api.Test;
import org.rootdir.hexlet.java.m2k.NodeStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DifferTest {

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
        var cl = DifferTest.class.getClassLoader();
        var file1 = new File(cl.getResource("file1-flat.json").getFile()).getAbsolutePath();
        var file2 = new File(cl.getResource("file2-flat.json").getFile()).getAbsolutePath();
        var diff = Differ.generate(file1, file2);
        var expected = "{\n" + //
                "  - follow: false\n" + //
                "    host: hexlet.io\n" + //
                "  - proxy: 123.234.53.22\n" + //
                "  - timeout: 50\n" + //
                "  + timeout: 20\n" + //
                "  + verbose: true\n" + //
                "}";
        assertEquals(expected, diff);
    }

    @Test
    void diffGeneration2PathsYamlTest() throws Exception {
        var cl = DifferTest.class.getClassLoader();
        var file1 = new File(cl.getResource("file1-flat.yaml").getFile()).getAbsolutePath();
        var file2 = new File(cl.getResource("file2-flat.yaml").getFile()).getAbsolutePath();
        var diff = Differ.generate(file1, file2);
        var expected = "{\n" + //
                "  - follow: false\n" + //
                "    host: hexlet.io\n" + //
                "  - proxy: 123.234.53.22\n" + //
                "  - timeout: 50\n" + //
                "  + timeout: 20\n" + //
                "  + verbose: true\n" + //
                "}";
        assertEquals(expected, diff);
    }

    @Test
    void diffGeneration2PathsYmlTest() throws Exception {
        var cl = DifferTest.class.getClassLoader();
        var file1 = new File(cl.getResource("file1-flat.yml").getFile()).getAbsolutePath();
        var file2 = new File(cl.getResource("file2-flat.yml").getFile()).getAbsolutePath();
        var diff = Differ.generate(file1, file2);
        var expected = "{\n" + //
                "  - follow: false\n" + //
                "    host: hexlet.io\n" + //
                "  - proxy: 123.234.53.22\n" + //
                "  - timeout: 50\n" + //
                "  + timeout: 20\n" + //
                "  + verbose: true\n" + //
                "}";
        assertEquals(expected, diff);
    }

    @Test
    void diffGenerationFormatMismatchTest() {
        var cl = DifferTest.class.getClassLoader();
        var file1 = new File(cl.getResource("file1-flat.json").getFile()).getAbsolutePath();
        var file2 = new File(cl.getResource("file2-flat.yaml").getFile()).getAbsolutePath();

        assertThrows(Exception.class, () -> {
            Differ.generate(file1, file2);
        });
    }

    @Test
    void diffGenerationStylishTest() throws Exception {
        var cl = DifferTest.class.getClassLoader();
        var file1 = new File(cl.getResource("file1-flat.json").getFile()).getAbsolutePath();
        var file2 = new File(cl.getResource("file2-flat.json").getFile()).getAbsolutePath();
        var diff = Differ.generate(file1, file2, "stylish");
        var expected = "{\n" + //
                "  - follow: false\n" + //
                "    host: hexlet.io\n" + //
                "  - proxy: 123.234.53.22\n" + //
                "  - timeout: 50\n" + //
                "  + timeout: 20\n" + //
                "  + verbose: true\n" + //
                "}";
        assertEquals(expected, diff);
    }

    @Test
    void diffGenerationPlainTest() throws Exception {
        var cl = DifferTest.class.getClassLoader();
        var file1 = new File(cl.getResource("file1-flat.json").getFile()).getAbsolutePath();
        var file2 = new File(cl.getResource("file2-flat.json").getFile()).getAbsolutePath();
        var diff = Differ.generate(file1, file2, "plain");
        var expected = "Property 'follow' was removed\n" + //
                "Property 'proxy' was removed\n" + //
                "Property 'timeout' was updated from 50 to 20\n" + //
                "Property 'verbose' was added with value: true\n";
        assertEquals(expected, diff);
    }

    @Test
    void diffGenerationJsonTest() throws Exception {
        var cl = DifferTest.class.getClassLoader();
        var file1 = new File(cl.getResource("file1-flat.json").getFile()).getAbsolutePath();
        var file2 = new File(cl.getResource("file2-flat.json").getFile()).getAbsolutePath();
        var diff = Differ.generate(file1, file2, "json");
        var expected = "{\n" + "  \"follow\" : {\n" + "    \"changeType\" : \"removed\",\n"
                + "    \"oldValue\" : false\n" + "  },\n" + "  \"host\" : {\n" + "    \"changeType\" : \"unchanged\",\n"
                + "    \"oldValue\" : \"hexlet.io\"\n" + "  },\n" + "  \"proxy\" : {\n"
                + "    \"changeType\" : \"removed\",\n" + "    \"oldValue\" : \"123.234.53.22\"\n" + "  },\n"
                + "  \"timeout\" : {\n" + "    \"changeType\" : \"updated\",\n" + "    \"oldValue\" : 50,\n"
                + "    \"newValue\" : 20\n" + "  },\n" + "  \"verbose\" : {\n" + "    \"changeType\" : \"added\",\n"
                + "    \"newValue\" : true\n" + "  }\n" + "}";
        assertEquals(expected, diff);
    }
}
