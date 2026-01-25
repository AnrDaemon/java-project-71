package org.rootdir.hexlet.java.m2k;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DiffFormatStylishTest {

    @Test
    void formatAddedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{}");
        var right = mapper.readTree("{\"added\":\"yes\"}");
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var result = DiffFormatStylish.format(diff);
        var expected = "{\n  + added: yes\n}";
        assertEquals(expected, result);
    }

    @Test
    void formatUpdatedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"updated\":\"no\"}");
        var right = mapper.readTree("{\"updated\":\"yes\"}");
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var result = DiffFormatStylish.format(diff);
        var expected = "{\n  - updated: no\n  + updated: yes\n}";
        assertEquals(expected, result);
    }

    @Test
    void formatRemovedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"removed\":\"yes\"}");
        var right = mapper.readTree("{}");
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var result = DiffFormatStylish.format(diff);
        var expected = "{\n  - removed: yes\n}";
        assertEquals(expected, result);
    }

    @Test
    void formatUnchangedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"value\":\"unchanged\"}");
        var right = mapper.readTree("{\"value\":\"unchanged\"}");
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var result = DiffFormatStylish.format(diff);
        var expected = "{\n    value: unchanged\n}";
        assertEquals(expected, result);
    }

    @Test
    void formatRemovedNestedObjectTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"removed\":{\"nested\":\"yes\"}}");
        var right = mapper.readTree("{}");
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var result = DiffFormatStylish.format(diff);
        var expected = "{\n  - removed: {nested=yes}\n}";
        assertEquals(expected, result);
    }

    @Test
    void formatRemovedNestedArrayTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"removed\": [1,2,5]}");
        var right = mapper.readTree("{}");
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var result = DiffFormatStylish.format(diff);
        var expected = "{\n  - removed: [1, 2, 5]\n}";
        assertEquals(expected, result);
    }
}
