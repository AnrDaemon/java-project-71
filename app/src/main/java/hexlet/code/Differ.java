package hexlet.code;

import java.nio.file.Paths;
import org.rootdir.hexlet.java.m2k.filediffer.NonRecursiveFlattener;
import org.rootdir.hexlet.java.m2k.filediffer.RecursiveFlattener;
import org.rootdir.hexlet.java.m2k.filediffer.TreeDiffer;
import org.rootdir.hexlet.java.m2k.filediffer.formatter.FormatterSelector;
import org.rootdir.hexlet.java.m2k.filereader.SourceFileReader;

public class Differ {


    public static String generate(String path1, String path2) throws Exception {
        return generate(path1, path2, FormatterSelector.STYLISH);
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
        return FormatterSelector.select(format).format(diff);
    }

}
