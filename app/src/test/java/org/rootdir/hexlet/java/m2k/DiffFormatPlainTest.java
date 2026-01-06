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
        var diff = FileDiffer.fromParsed(left, right).parse().diff();
        var result = DiffFormatPlain.format(diff);
        var expected = "Property 'added' was added with value: 'yes'\n";
        assertEquals(expected, result);
    }

    @Test
    void formatAddedComplexValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{}");
        var right = mapper.readTree("{\"added\":[]}");
        var diff = FileDiffer.fromParsed(left, right).parse().diff();
        var result = DiffFormatPlain.format(diff);
        var expected = "Property 'added' was added with value: [complex value]\n";
        assertEquals(expected, result);
    }

    @Test
    void formatUpdatedStringValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"updated\":\"no\"}");
        var right = mapper.readTree("{\"updated\":\"yes\"}");
        var diff = FileDiffer.fromParsed(left, right).parse().diff();
        var result = DiffFormatPlain.format(diff);
        var expected = "Property 'updated' was updated from 'no' to 'yes'\n";
        assertEquals(expected, result);
    }

    @Test
    void formatRemovedValueTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"removed\":\"yes\"}");
        var right = mapper.readTree("{}");
        var diff = FileDiffer.fromParsed(left, right).parse().diff();
        var result = DiffFormatPlain.format(diff);
        var expected = "Property 'removed' was removed\n";
        assertEquals(expected, result);
    }
}
