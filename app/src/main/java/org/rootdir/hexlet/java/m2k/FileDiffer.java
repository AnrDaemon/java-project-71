package org.rootdir.hexlet.java.m2k;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.Getter;

public class FileDiffer {

    public static final String FORMAT_JSON = "JSON";
    public static final String FORMAT_YAML = "YAML";

    @Getter
    private String format = "JSON";

    private Path leftPath;
    private Path rightPath;

    private JsonNode leftRead;
    private JsonNode rightRead;

    private Map<String, JsonNode> parsedLeft;
    private Map<String, JsonNode> parsedRight;


    private static String getFileExtension(String fileName) {
        int extPos = fileName.lastIndexOf('.');
        return (extPos > 0 && extPos < fileName.length() - 1) ? fileName.substring(extPos + 1) : "";
    }


    public static FileDiffer fromPaths(String left, String right) throws Exception {
        return FileDiffer.fromPaths(Paths.get(left), Paths.get(right));
    }


    public static FileDiffer fromPaths(Path left, Path right) throws Exception {
        var lExt = FileDiffer.getFileExtension(left.getFileName().toString()).toLowerCase();
        var rExt = FileDiffer.getFileExtension(right.getFileName().toString()).toLowerCase();
        if (lExt == "yml") {
            lExt = "yaml";
        }
        if (rExt == "yml") {
            rExt = "yaml";
        }
        if (!lExt.equals(rExt)) {
            throw new InvalidParameterException("Compared files must be of the same type");
        }

        var result = new FileDiffer(left, right, switch (lExt) {
            case "yaml" -> FileDiffer.FORMAT_YAML;
            default -> FileDiffer.FORMAT_JSON;
        });

        return result.read().parse();
    }


    public static FileDiffer fromParsed(JsonNode left, JsonNode right) {
        var result = new FileDiffer(left, right);

        return result.parse();
    }


    public FileDiffer(Path left, Path right) {
        this(left, right, FileDiffer.FORMAT_JSON);
    }


    public FileDiffer(Path left, Path right, String fileFormat) {
        this.leftPath = left.toAbsolutePath().normalize();
        this.rightPath = right.toAbsolutePath().normalize();
        this.format = fileFormat;
    }


    public FileDiffer(JsonNode left, JsonNode right) {
        this.leftRead = left;
        this.rightRead = right;
    }

    /**
     * Read and parse the files provided by constructor.
     *
     * @return Object chaining reference.
     * @throws Exception
     */
    private FileDiffer read() throws Exception {
        var mapper = FileDiffer.FORMAT_YAML.equals(this.format) ? new YAMLMapper() : new ObjectMapper();
        this.leftRead = mapper.readTree(this.leftPath.toFile());
        this.rightRead = mapper.readTree(this.rightPath.toFile());

        return this;
    }


    /**
     * Read and parse the files provided by constructor.
     *
     * @return Object chaining reference.
     */
    private FileDiffer parse() {
        this.parsedLeft = flatten(this.leftRead, "");
        this.parsedRight = flatten(this.rightRead, "");

        return this;
    }

    /**
     * Compare nodes and build the list of changes.
     *
     * @return The list of changes.
     */
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

    /**
     * Flattens the structure to a map path-node pairs.
     *
     * @param obj The list of nodes (list root node).
     * @param path The list (root node) path name.
     * @return A map of full node paths and values.
     */
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
