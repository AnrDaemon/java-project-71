package org.rootdir.hexlet.java.m2k.filediffer.formatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.rootdir.hexlet.java.m2k.filediffer.NonRecursiveFlattener;
import org.rootdir.hexlet.java.m2k.filediffer.TreeDiffer;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.FileReadingTest;

public class DiffFormatPlainTest extends FileReadingTest {

    @Test
    void formatTest() throws Exception {
        var file1 = getResourceFile("file1-basic.json");
        var file2 = getResourceFile("file2-basic.json");
        var mapper = new ObjectMapper();
        var left = mapper.readTree(file1);
        var right = mapper.readTree(file2);
        var parser = new NonRecursiveFlattener();
        var diff = TreeDiffer.diff(parser.parse(left), parser.parse(right));
        var result = FormatterSelector.select(FormatterSelector.PLAIN).format(diff);
        var expected = readFixture("expected-basic.plain.txt");
        assertEquals(expected, result);
    }

}
