package org.rootdir.hexlet.java.m2k;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.Differ;

public class DiffFormatPlainTest {

    @Test
    void formatAddedStringValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{}");
        var right = mapper.readTree("{\"added\":\"yes\"}");
        var diff = Differ.fromParsed(left, right).parse().diff();
        var result = DiffFormatPlain.format(diff);
        var expected = "Property 'added' was added with value: 'yes'\n";
        assertEquals(expected, result);
    }

    @Test
    void formatAddedComplexValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{}");
        var right = mapper.readTree("{\"added\":[]}");
        var diff = Differ.fromParsed(left, right).parse().diff();
        var result = DiffFormatPlain.format(diff);
        var expected = "Property 'added' was added with value: [complex value]\n";
        assertEquals(expected, result);
    }

    @Test
    void formatUpdatedStringValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"updated\":\"no\"}");
        var right = mapper.readTree("{\"updated\":\"yes\"}");
        var diff = Differ.fromParsed(left, right).parse().diff();
        var result = DiffFormatPlain.format(diff);
        var expected = "Property 'updated' was updated from 'no' to 'yes'\n";
        assertEquals(expected, result);
    }

    @Test
    void formatUpdatedBooleanValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"updated\":false}");
        var right = mapper.readTree("{\"updated\":true}");
        var diff = Differ.fromParsed(left, right).parse().diff();
        var result = DiffFormatPlain.format(diff);
        var expected = "Property 'updated' was updated from false to true\n";
        assertEquals(expected, result);
    }

    @Test
    void formatUpdatedNullToNumericValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"updated\":null}");
        var right = mapper.readTree("{\"updated\":123}");
        var diff = Differ.fromParsed(left, right).parse().diff();
        var result = DiffFormatPlain.format(diff);
        var expected = "Property 'updated' was updated from null to 123\n";
        assertEquals(expected, result);
    }

    @Test
    void formatRemovedValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"removed\":\"yes\"}");
        var right = mapper.readTree("{}");
        var diff = Differ.fromParsed(left, right).parse().diff();
        var result = DiffFormatPlain.format(diff);
        var expected = "Property 'removed' was removed\n";
        assertEquals(expected, result);
    }

    @Test
    void formatUnchangedValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"unchanged\":\"yes\"}");
        var right = mapper.readTree("{\"unchanged\":\"yes\"}");
        var diff = Differ.fromParsed(left, right).parse().diff();
        var result = DiffFormatPlain.format(diff);
        var expected = "";
        assertEquals(expected, result);
    }
}
