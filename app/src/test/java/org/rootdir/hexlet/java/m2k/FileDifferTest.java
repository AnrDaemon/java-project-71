package org.rootdir.hexlet.java.m2k;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileDifferTest {

    @Test
    void diffAddedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{}");
        var right = mapper.readTree("{\"added\":\"yes\"}");
        var diff = FileDiffer.fromParsed(left, right).diff();
        var expected = new NodeStatus("added", null, right.get("added"), NodeStatus.ADDED);
        assertEquals(expected, diff.get(0));
    }

    @Test
    void diffUpdatedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"updated\":\"no\"}");
        var right = mapper.readTree("{\"updated\":\"yes\"}");
        var diff = FileDiffer.fromParsed(left, right).diff();
        var expected = new NodeStatus("updated", left.get("updated"), right.get("updated"),
                NodeStatus.UPDATED);
        assertEquals(expected, diff.get(0));
    }

    @Test
    void diffRemovedTest() throws Exception {
        var mapper = new ObjectMapper();
        var left = mapper.readTree("{\"removed\":\"yes\"}");
        var right = mapper.readTree("{}");
        var diff = FileDiffer.fromParsed(left, right).diff();
        var expected = new NodeStatus("removed", left.get("removed"), null, NodeStatus.REMOVED);
        assertEquals(expected, diff.get(0));
    }
}
