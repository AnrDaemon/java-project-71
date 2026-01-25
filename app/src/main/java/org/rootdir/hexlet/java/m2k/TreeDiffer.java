package org.rootdir.hexlet.java.m2k;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;

public class TreeDiffer {

    /**
     * Compare nodes and build the list of changes.
     *
     * @return The list of changes.
     */
    public static List<NodeStatus> diff(Map<String, JsonNode> parsedLeft, Map<String, JsonNode> parsedRight) {
        var result = new ArrayList<NodeStatus>();

        for (var l : parsedLeft.entrySet()) {
            if (!parsedRight.containsKey(l.getKey())) {
                result.add(new NodeStatus(l.getKey(), l.getValue(), null, NodeStatus.REMOVED));
            } else if (!l.getValue().toString().equals(parsedRight.get(l.getKey()).toString())) {
                result.add(new NodeStatus(l.getKey(), l.getValue(), parsedRight.get(l.getKey()), NodeStatus.UPDATED));
            } else {
                result.add(new NodeStatus(l.getKey(), l.getValue(), null, NodeStatus.UNCHANGED));
            }
        }

        for (var r : parsedRight.entrySet()) {
            if (!parsedLeft.containsKey(r.getKey())) {
                result.add(new NodeStatus(r.getKey(), null, r.getValue(), NodeStatus.ADDED));
            }
        }

        result.sort(Comparator.comparing(NodeStatus::getName));

        return result;
    }

}
