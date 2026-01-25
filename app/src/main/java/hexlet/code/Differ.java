package hexlet.code;

import java.nio.file.Paths;
import java.security.InvalidParameterException;
import org.rootdir.hexlet.java.m2k.DiffFormatJson;
import org.rootdir.hexlet.java.m2k.DiffFormatPlain;
import org.rootdir.hexlet.java.m2k.DiffFormatStylish;
import org.rootdir.hexlet.java.m2k.NonRecursiveFlattener;
import org.rootdir.hexlet.java.m2k.RecursiveFlattener;
import org.rootdir.hexlet.java.m2k.TreeDiffer;
import org.rootdir.hexlet.java.m2k.interfaces.SourceFileReader;

public class Differ {

    public static final String FORMAT_STYLISH = "stylish";
    public static final String FORMAT_PLAIN = "plain";
    public static final String FORMAT_JSON = "json";


    private static String getFileExtension(String fileName) {
        int extPos = fileName.lastIndexOf('.');
        return (extPos > 0 && extPos < fileName.length() - 1) ? fileName.substring(extPos + 1) : "";
    }


    public static String generate(String path1, String path2) throws Exception {
        return generate(path1, path2, FORMAT_STYLISH);
    }


    public static String generate(String path1, String path2, String format) throws Exception {
        return generate(path1, path2, format, false);
    }


    public static String generate(String path1, String path2, String format, Boolean recursive) throws Exception {
        var lExt = Differ.getFileExtension(path1).toLowerCase();
        var rExt = Differ.getFileExtension(path2).toLowerCase();
        if (("yml").equals(lExt)) {
            lExt = "yaml";
        }
        if (("yml").equals(rExt)) {
            rExt = "yaml";
        }
        if (!lExt.equals(rExt)) {
            throw new InvalidParameterException("Compared files must be of the same type");
        }

        var srcFormat = ("yaml").equals(lExt) ? SourceFileReader.FORMAT_YAML : SourceFileReader.FORMAT_JSON;

        var leftRead = SourceFileReader.readFile(Paths.get(path1), srcFormat);
        var rightRead = SourceFileReader.readFile(Paths.get(path2), srcFormat);

        var parser = recursive ? new RecursiveFlattener() : new NonRecursiveFlattener();
        var leftParsed = parser.parse(leftRead);
        var rightParsed = parser.parse(rightRead);

        var diff = TreeDiffer.diff(leftParsed, rightParsed);
        return switch (format) {
            case FORMAT_JSON -> DiffFormatJson.format(diff);
            case FORMAT_PLAIN -> DiffFormatPlain.format(diff);
            default -> DiffFormatStylish.format(diff);
        };
    }

}
