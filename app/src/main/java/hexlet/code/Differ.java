package hexlet.code;

import java.nio.file.Paths;
import org.rootdir.hexlet.java.m2k.DiffFormatJson;
import org.rootdir.hexlet.java.m2k.DiffFormatPlain;
import org.rootdir.hexlet.java.m2k.DiffFormatStylish;
import org.rootdir.hexlet.java.m2k.NonRecursiveFlattener;
import org.rootdir.hexlet.java.m2k.RecursiveFlattener;
import org.rootdir.hexlet.java.m2k.TreeDiffer;
import org.rootdir.hexlet.java.m2k.filereader.SourceFileReader;

public class Differ {

    public static final String FORMAT_STYLISH = "stylish";
    public static final String FORMAT_PLAIN = "plain";
    public static final String FORMAT_JSON = "json";


    public static String generate(String path1, String path2) throws Exception {
        return generate(path1, path2, FORMAT_STYLISH);
    }


    public static String generate(String path1, String path2, String format) throws Exception {
        return generate(path1, path2, format, false);
    }


    public static String generate(String path1, String path2, String format, Boolean recursive) throws Exception {
        var left = new SourceFileReader(Paths.get(path1));
        var right = new SourceFileReader(Paths.get(path2));

        var parser = recursive ? new RecursiveFlattener() : new NonRecursiveFlattener();
        var leftParsed = parser.parse(left.read());
        var rightParsed = parser.parse(right.read());

        var diff = TreeDiffer.diff(leftParsed, rightParsed);
        return switch (format) {
            case FORMAT_JSON -> DiffFormatJson.format(diff);
            case FORMAT_PLAIN -> DiffFormatPlain.format(diff);
            default -> DiffFormatStylish.format(diff);
        };
    }

}
