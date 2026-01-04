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
        var diff = FileDiffer.fromParsed(left, right).diff();
        var result = DiffFormatStylish.format(diff);
        var expected = "{\n + added: yes\n}\n";
        assertEquals(expected, result);
    }

    @Test
    void formatUpdatedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"updated\":\"no\"}");
        var right = mapper.readTree("{\"updated\":\"yes\"}");
        var diff = FileDiffer.fromParsed(left, right).diff();
        var result = DiffFormatStylish.format(diff);
        var expected = "{\n - updated: no\n + updated: yes\n}\n";
        assertEquals(expected, result);
    }

    @Test
    void formatRemovedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"removed\":\"yes\"}");
        var right = mapper.readTree("{}");
        var diff = FileDiffer.fromParsed(left, right).diff();
        var result = DiffFormatStylish.format(diff);
        var expected = "{\n - removed: yes\n}\n";
        assertEquals(expected, result);
    }

}
