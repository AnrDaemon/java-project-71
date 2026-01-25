package org.rootdir.hexlet.java.m2k;

import java.io.IOException;
import java.nio.file.Path;
import org.rootdir.hexlet.java.m2k.interfaces.SourceFileReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class YamlFileReader implements SourceFileReader {

    @Override
    public JsonNode read(Path path) throws IOException {
        var mapper = new YAMLMapper();
        return mapper.readTree(path.toFile());
    }

}
