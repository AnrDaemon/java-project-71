package org.rootdir.hexlet.java.m2k.filediffer.formatter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.rootdir.hexlet.java.m2k.filediffer.NodeStatus;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DiffFormatStylish implements FormatterInterface {

    private static String printf(String c, String key, JsonNode node) {
        var value = formatValue(node);
        return String.format("  %s %s: %s\n", c, key, value);
    }

    private static String formatValue(JsonNode node) {
        return node.isValueNode() ? node.asText() : formatObject(node);
    }

    private static String formatObject(JsonNode node) {
        var result = new StringBuilder();

        if (!node.isContainerNode()) {
            throw new IllegalArgumentException("Given value is not a valid renderable node");
        }

        result.append(node.isArray() ? "[" : "{");
        Stream<String> stream;
        if (node.isArray()) {
            stream = node.valueStream().map((n) -> n.isValueNode() ? n.asText() : formatObject(n));
        } else {
            stream = node.propertyStream().map((n) -> (node.isObject() ? n.getKey() + "=" : "")
                    + (n.getValue().isValueNode() ? n.getValue().asText() : formatObject(n.getValue())));
        }
        result.append(stream.collect(Collectors.joining(", ")));

        return result.append(node.isArray() ? "]" : "}").toString();
    }

    /**
     * Format node changes to a string.
     *
     * @param diff List of tree nodes' changes.
     * @return Formatted changes representation.
     */
    @Override
    public String format(List<NodeStatus> diff) {
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
        result.append("}");

        return result.toString();
    }
}
