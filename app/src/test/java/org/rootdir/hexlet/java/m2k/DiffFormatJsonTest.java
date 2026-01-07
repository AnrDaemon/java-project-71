package org.rootdir.hexlet.java.m2k;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DiffFormatJsonTest {

    @Test
    void formatAddedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{}");
        var right = mapper.readTree("{\"added\":\"yes\"}");
        var diff = FileDiffer.fromParsed(left, right).parse().diff();
        var result = DiffFormatJson.format(diff);
        var expected =
                "{\r\n  \"added\" : {\r\n    \"changeType\" : \"added\",\r\n    \"newValue\" : \"yes\"\r\n  }\r\n}";
        assertEquals(expected, result);
    }

    @Test
    void formatUpdatedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"updated\":\"no\"}");
        var right = mapper.readTree("{\"updated\":\"yes\"}");
        var diff = FileDiffer.fromParsed(left, right).parse().diff();
        var result = DiffFormatJson.format(diff);
        var expected = "{\r\n  \"updated\" : {\r\n    \"changeType\" : \"updated\",\r\n"
                + "    \"oldValue\" : \"no\",\r\n    \"newValue\" : \"yes\"\r\n  }\r\n}";
        assertEquals(expected, result);
    }

    @Test
    void formatRemovedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"removed\":\"yes\"}");
        var right = mapper.readTree("{}");
        var diff = FileDiffer.fromParsed(left, right).parse().diff();
        var result = DiffFormatJson.format(diff);
        var expected = "{\r\n  \"removed\" : {\r\n    \"changeType\" : \"removed\",\r\n"
                + "    \"oldValue\" : \"yes\"\r\n  }\r\n}";
        assertEquals(expected, result);
    }

    @Test
    void formatUnchangedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"unchanged\":\"yes\"}");
        var right = mapper.readTree("{\"unchanged\":\"yes\"}");
        var diff = FileDiffer.fromParsed(left, right).parse().diff();
        var result = DiffFormatJson.format(diff);
        var expected = "{\r\n  \"unchanged\" : {\r\n    \"changeType\" : \"unchanged\",\r\n"
                + "    \"oldValue\" : \"yes\"\r\n  }\r\n}";
        assertEquals(expected, result);
    }
}
