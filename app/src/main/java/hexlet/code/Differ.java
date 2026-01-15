package hexlet.code;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.rootdir.hexlet.java.m2k.NodeStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.Getter;
import lombok.val;

public class Differ {

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


    public static Differ fromPaths(String left, String right) throws Exception {
        return Differ.fromPaths(Paths.get(left), Paths.get(right));
    }


    public static Differ fromPaths(Path left, Path right) throws Exception {
        var lExt = Differ.getFileExtension(left.getFileName().toString()).toLowerCase();
        var rExt = Differ.getFileExtension(right.getFileName().toString()).toLowerCase();
        if (lExt == "yml") {
            lExt = "yaml";
        }
        if (rExt == "yml") {
            rExt = "yaml";
        }
        if (!lExt.equals(rExt)) {
            throw new InvalidParameterException("Compared files must be of the same type");
        }

        var result = new Differ(left, right, switch (lExt) {
            case "yaml" -> Differ.FORMAT_YAML;
            default -> Differ.FORMAT_JSON;
        });

        return result.read();
    }


    public static Differ fromParsed(JsonNode left, JsonNode right) {
        var result = new Differ(left, right);

        return result;
    }


    public Differ(Path left, Path right) {
        this(left, right, Differ.FORMAT_JSON);
    }


    public Differ(Path left, Path right, String fileFormat) {
        this.leftPath = left.toAbsolutePath().normalize();
        this.rightPath = right.toAbsolutePath().normalize();
        this.format = fileFormat;
    }


    public Differ(JsonNode left, JsonNode right) {
        this.leftRead = left;
        this.rightRead = right;
    }

    /**
     * Read and parse the files provided by constructor.
     *
     * @return Object chaining reference.
     * @throws Exception
     */
    private Differ read() throws Exception {
        var mapper = Differ.FORMAT_YAML.equals(this.format) ? new YAMLMapper() : new ObjectMapper();
        this.leftRead = mapper.readTree(this.leftPath.toFile());
        this.rightRead = mapper.readTree(this.rightPath.toFile());

        return this;
    }


    /**
     * Read and parse the files provided by constructor.
     *
     * @return Object chaining reference.
     */
    public Differ parse() {
        this.parsedLeft = flatten(this.leftRead);
        this.parsedRight = flatten(this.rightRead);

        return this;
    }


    /**
     * Read and parse the files provided by constructor.
     *
     * @return Object chaining reference.
     */
    public Differ parseRecursive() {
        this.parsedLeft = flatten(this.leftRead, "$", ".");
        this.parsedRight = flatten(this.rightRead, "$", ".");

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
