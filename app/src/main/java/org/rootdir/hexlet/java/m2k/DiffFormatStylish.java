package org.rootdir.hexlet.java.m2k;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class DiffFormatStylish {

    private static String printf(String c, String key, JsonNode node) {
        String value;
        if (node.isValueNode()) {
            value = node.asText();
        } else {
            value = node.toString();
        }
        return String.format(" %s %s: %s\n", c, key, value);
    }

    public static String format(List<NodeStatus> diff) {
        var result = new StringBuilder();
        result.append("{\n");
        for (var e : diff) {
            switch (e.getStatus()) {
                case NodeStatus.ADDED:
                    result.append(printf("+", e.getName(), (JsonNode) e.getNewValue()));
                    break;

                case NodeStatus.UPDATED:
                    result.append(printf("-", e.getName(), (JsonNode) e.getOldValue()));
                    result.append(printf("+", e.getName(), (JsonNode) e.getNewValue()));
                    break;

                case NodeStatus.REMOVED:
                    result.append(printf("-", e.getName(), (JsonNode) e.getOldValue()));
                    break;

                default:
                    result.append(printf(" ", e.getName(), (JsonNode) e.getOldValue()));
                    break;
            }
        }
        result.append("}\n");

        return result.toString();
    }
}
