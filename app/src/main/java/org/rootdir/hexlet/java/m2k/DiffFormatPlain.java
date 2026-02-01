package org.rootdir.hexlet.java.m2k;

import java.util.ArrayList;
import java.util.List;
import org.rootdir.hexlet.java.m2k.filediffer.NodeStatus;
import com.fasterxml.jackson.databind.JsonNode;

public class DiffFormatPlain {

    private static String line(String name, String cause) {
        return String.format("Property '%s' was %s", name, cause);
    }

    private static String printAdded(String key, JsonNode node) {
        return line(key, String.format("added with value: %s", formatValue(node)));
    }

    private static String printUpdated(String key, JsonNode oldNode, JsonNode newNode) {
        var oldValue = formatValue(oldNode);
        var newValue = formatValue(newNode);
        return line(key, String.format("updated. From %s to %s", oldValue, newValue));
    }

    private static String printRemoved(String key) {
        return line(key, "removed");
    }

    private static String formatValue(JsonNode node) {
        if (!node.isValueNode()) {
            return "[complex value]";
        }

        if (node.isTextual()) {
            return "'" + node.asText() + "'";
        }

        return node.asText();
    }

    public static String format(List<NodeStatus> diff) {
        var result = new ArrayList<String>();
        for (var e : diff) {
            switch (e.getStatus()) {
                case NodeStatus.ADDED:
                    result.add(printAdded(e.getName(), (JsonNode) e.getNewValue()));
                    break;

                case NodeStatus.UPDATED:
                    result.add(printUpdated(e.getName(), (JsonNode) e.getOldValue(), (JsonNode) e.getNewValue()));
                    break;

                case NodeStatus.REMOVED:
                    result.add(printRemoved(e.getName()));
                    break;

                default:
                    break;
            }
        }

        return String.join("\n", result);
    }
}
