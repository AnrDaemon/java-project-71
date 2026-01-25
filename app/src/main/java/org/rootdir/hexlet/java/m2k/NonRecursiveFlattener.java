package org.rootdir.hexlet.java.m2k;

import java.util.HashMap;
import java.util.Map;
import org.rootdir.hexlet.java.m2k.interfaces.TreeParserInterface;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.val;

public class NonRecursiveFlattener implements TreeParserInterface {

    @Override
    public Map<String, JsonNode> parse(JsonNode tree) {
        return flatten(tree);
    }

    /**
     * Converts the structure to a path-node pairs map.
     *
     * This is a flat version of the full function.
     *
     * @param obj The list of nodes (list root node).
     * @return A map of node paths and values.
     */
    public static Map<String, JsonNode> flatten(JsonNode obj) {
        val path = "";
        var result = new HashMap<String, JsonNode>();

        if (obj.isObject()) {
            for (var o : obj.properties()) {
                result.put(o.getKey(), o.getValue());
            }
        } else {
            result.put(path, obj);
        }

        return result;
    }

}
