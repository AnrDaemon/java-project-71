package org.rootdir.hexlet.java.m2k;

import java.io.IOException;
import java.nio.file.Path;
import org.rootdir.hexlet.java.m2k.interfaces.SourceFileReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFileReader implements SourceFileReader {

    @Override
    public JsonNode read(Path path) throws IOException {
        var mapper = new ObjectMapper();
        return mapper.readTree(path.toFile());
    }

}
