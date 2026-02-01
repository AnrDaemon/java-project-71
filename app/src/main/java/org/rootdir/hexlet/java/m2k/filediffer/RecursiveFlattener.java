package org.rootdir.hexlet.java.m2k.filediffer;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;

public class RecursiveFlattener implements TreeParserInterface {

    /**
     * Flattens the tree.
     *
     * @param tree Node tree.
     * @return Flat map of the tree.
     */
    @Override
    public Map<String, JsonNode> parse(JsonNode tree) {
        return flatten(tree, "$", ".");
    }

    /**
     * Flattens the structure to a path-node pairs map.
     *
     * This is a recursive version of a function.
     *
     * @param obj The list of nodes (list root node).
     * @param path The list (root node) path name. Supply "$" to get traditional JSON notation of the
     *        full paths (like, `$.path.other.etc`).
     * @param sep Path names separator.
     * @return A map of full node paths and values.
     */
    public static Map<String, JsonNode> flatten(JsonNode obj, String path, String sep) {
        var result = new HashMap<String, JsonNode>();

        if (obj.isObject()) {
            for (var o : obj.properties()) {
                result.putAll(flatten(o.getValue(), path + sep + o.getKey(), sep));
            }
        } else {
            result.put(path, obj);
        }

        return result;
    }

}
