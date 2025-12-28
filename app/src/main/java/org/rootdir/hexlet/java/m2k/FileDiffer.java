package org.rootdir.hexlet.java.m2k;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileDiffer {

    private final Path left;
    private final Path right;

    private Map<String, JsonNode> parsedLeft;
    private Map<String, JsonNode> parsedRight;

    public FileDiffer(String left, String right) {
        this.left = Paths.get(left).toAbsolutePath().normalize();
        this.right = Paths.get(right).toAbsolutePath().normalize();
    }

    public FileDiffer read() throws Exception {
        var jsonMapper = new ObjectMapper();
        parsedLeft = flatten(jsonMapper.readTree(left.toFile()), "");
        parsedRight = flatten(jsonMapper.readTree(right.toFile()), "");

        return this;
    }

    public List<NodeStatus> diff() {
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

    public Map<String, JsonNode> flatten(JsonNode obj, String path) {
        var result = new HashMap<String, JsonNode>();

        if (obj.isObject()) {
            for (var o : obj.properties()) {
                result.putAll(flatten(o.getValue(), /* path + "/" + */ o.getKey()));
            }
        } else {
            result.put(path, obj);
        }

        return result;
    }
}
