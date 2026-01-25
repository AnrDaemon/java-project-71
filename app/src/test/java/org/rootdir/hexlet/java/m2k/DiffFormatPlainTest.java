package org.rootdir.hexlet.java.m2k;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DiffFormatPlainTest {

    @Test
    void formatAddedStringValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{}");
        var right = mapper.readTree("{\"added\":\"yes\"}");
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var result = DiffFormatPlain.format(diff);
        var expected = "Property 'added' was added with value: 'yes'";
        assertEquals(expected, result);
    }

    @Test
    void formatAddedComplexValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{}");
        var right = mapper.readTree("{\"added\":[]}");
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var result = DiffFormatPlain.format(diff);
        var expected = "Property 'added' was added with value: [complex value]";
        assertEquals(expected, result);
    }

    @Test
    void formatUpdatedStringValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"updated\":\"no\"}");
        var right = mapper.readTree("{\"updated\":\"yes\"}");
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var result = DiffFormatPlain.format(diff);
        var expected = "Property 'updated' was updated. From 'no' to 'yes'";
        assertEquals(expected, result);
    }

    @Test
    void formatUpdatedBooleanValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"updated\":false}");
        var right = mapper.readTree("{\"updated\":true}");
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var result = DiffFormatPlain.format(diff);
        var expected = "Property 'updated' was updated. From false to true";
        assertEquals(expected, result);
    }

    @Test
    void formatUpdatedNullToNumericValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"updated\":null}");
        var right = mapper.readTree("{\"updated\":123}");
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var result = DiffFormatPlain.format(diff);
        var expected = "Property 'updated' was updated. From null to 123";
        assertEquals(expected, result);
    }

    @Test
    void formatRemovedValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"removed\":\"yes\"}");
        var right = mapper.readTree("{}");
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var result = DiffFormatPlain.format(diff);
        var expected = "Property 'removed' was removed";
        assertEquals(expected, result);
    }

    @Test
    void formatUnchangedValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"unchanged\":\"yes\"}");
        var right = mapper.readTree("{\"unchanged\":\"yes\"}");
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var result = DiffFormatPlain.format(diff);
        var expected = "";
        assertEquals(expected, result);
    }
}
