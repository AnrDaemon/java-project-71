package org.rootdir.hexlet.java.m2k.filediffer.formatter;

public class FormatterSelector {

    public static final String STYLISH = "stylish";
    public static final String PLAIN = "plain";
    public static final String JSON = "json";

    public static FormatterInterface select(String name) {
        return switch (name) {
            case JSON -> new DiffFormatJson();
            case PLAIN -> new DiffFormatPlain();
            default -> new DiffFormatStylish();
        };
    }

}
