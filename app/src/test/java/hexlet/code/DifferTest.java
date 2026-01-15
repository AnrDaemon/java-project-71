package hexlet.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        var diff = Differ.generate(file2, file1);
        System.err.println(diff);
        var expected = "{\n" + " + follow: false\n" + "   host: hexlet.io\n" + " + proxy: 123.234.53.22\n"
                + " - timeout: 20\n" + " + timeout: 50\n" + " - verbose: true\n" + "}\n";
        assertEquals(expected, diff);
    }

    @Test
    void diffGeneration2PathsYamlTest() throws Exception {
        var cl = DifferTest.class.getClassLoader();
        var file1 = new File(cl.getResource("file1-flat.yaml").getFile()).getAbsolutePath();
        var file2 = new File(cl.getResource("file2-flat.yaml").getFile()).getAbsolutePath();
        var diff = Differ.generate(file2, file1);
        System.err.println(diff);
        var expected = "{\n" + " + follow: false\n" + "   host: hexlet.io\n" + " + proxy: 123.234.53.22\n"
                + " - timeout: 20\n" + " + timeout: 50\n" + " - verbose: true\n" + "}\n";
        assertEquals(expected, diff);
    }

    @Test
    void diffGenerationStylishTest() throws Exception {
        var cl = DifferTest.class.getClassLoader();
        var file1 = new File(cl.getResource("file1-flat.json").getFile()).getAbsolutePath();
        var file2 = new File(cl.getResource("file2-flat.json").getFile()).getAbsolutePath();
        var diff = Differ.generate(file2, file1, "stylish");
        System.err.println(diff);
        var expected = "{\n" + " + follow: false\n" + "   host: hexlet.io\n" + " + proxy: 123.234.53.22\n"
                + " - timeout: 20\n" + " + timeout: 50\n" + " - verbose: true\n" + "}\n";
        assertEquals(expected, diff);
    }

    @Test
    void diffGenerationPlainTest() throws Exception {
        var cl = DifferTest.class.getClassLoader();
        var file1 = new File(cl.getResource("file1-flat.json").getFile()).getAbsolutePath();
        var file2 = new File(cl.getResource("file2-flat.json").getFile()).getAbsolutePath();
        var diff = Differ.generate(file2, file1, "plain");
        System.err.println(diff);
        var expected = "Property 'follow' was added with value: false\n"
                + "Property 'proxy' was added with value: '123.234.53.22'\n"
                + "Property 'timeout' was updated from 20 to 50\n" + "Property 'verbose' was removed\n";
        assertEquals(expected, diff);
    }

    @Test
    void diffGenerationJsonTest() throws Exception {
        var cl = DifferTest.class.getClassLoader();
        var file1 = new File(cl.getResource("file1-flat.json").getFile()).getAbsolutePath();
        var file2 = new File(cl.getResource("file2-flat.json").getFile()).getAbsolutePath();
        var diff = Differ.generate(file2, file1, "json");
        System.err.println(diff);
        var expected = "{\n" + "  \"follow\" : {\n" + "    \"changeType\" : \"added\",\n" + "    \"newValue\" : false\n"
                + "  },\n" + "  \"host\" : {\n" + "    \"changeType\" : \"unchanged\",\n"
                + "    \"oldValue\" : \"hexlet.io\"\n" + "  },\n" + "  \"proxy\" : {\n"
                + "    \"changeType\" : \"added\",\n" + "    \"newValue\" : \"123.234.53.22\"\n" + "  },\n"
                + "  \"timeout\" : {\n" + "    \"changeType\" : \"updated\",\n" + "    \"oldValue\" : 20,\n"
                + "    \"newValue\" : 50\n" + "  },\n" + "  \"verbose\" : {\n" + "    \"changeType\" : \"removed\",\n"
                + "    \"oldValue\" : true\n" + "  }\n" + "}";
        assertEquals(expected, diff);
    }
}
