package org.rootdir.hexlet.java.m2k.interfaces;

import java.io.IOException;
import java.nio.file.Path;
import org.rootdir.hexlet.java.m2k.JsonFileReader;
import org.rootdir.hexlet.java.m2k.YamlFileReader;
import com.fasterxml.jackson.databind.JsonNode;

public interface SourceFileReader {

    public static final String FORMAT_JSON = "JSON";
    public static final String FORMAT_YAML = "YAML";

    public JsonNode read(Path path) throws IOException;

    public static JsonNode readFile(Path path, String format) throws IOException {
        var reader = SourceFileReader.FORMAT_YAML.equals(format) ? new YamlFileReader() : new JsonFileReader();

        return reader.read(path);
    }
}
