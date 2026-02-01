package org.rootdir.hexlet.java.m2k.filediffer.formatter;

import java.util.List;
import org.rootdir.hexlet.java.m2k.filediffer.NodeStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DiffFormatJson implements FormatterInterface {

    /**
     * Format node changes to a string.
     *
     * @param diff List of tree nodes' changes.
     * @return Formatted changes representation.
     */
    @Override
    public String format(List<NodeStatus> diff) {
        var mapper = new ObjectMapper();
        var result = mapper.createObjectNode();
        for (var e : diff) {
            var changeNode = mapper.createObjectNode();
            changeNode.put("changeType", e.getStatus());
            switch (e.getStatus()) {
                case NodeStatus.ADDED:
                    changeNode.set("newValue", (JsonNode) e.getNewValue());
                    break;

                case NodeStatus.UPDATED:
                    changeNode.set("oldValue", (JsonNode) e.getOldValue());
                    changeNode.set("newValue", (JsonNode) e.getNewValue());

                case NodeStatus.REMOVED:
                default:
                    changeNode.set("oldValue", (JsonNode) e.getOldValue());
                    break;
            }
            result.set(e.getName(), changeNode);
        }

        DefaultIndenter lfIndenter = new DefaultIndenter("  ", "\n");
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.indentObjectsWith(lfIndenter);
        printer.indentArraysWith(lfIndenter);
        ObjectWriter writer = mapper.writer(printer);

        try {
            return writer.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
