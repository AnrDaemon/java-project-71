package org.rootdir.hexlet.java.m2k.filereader;

import java.io.IOException;
import java.nio.file.Path;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class YamlFileReader implements FileReaderInterface {

    @Getter
    private final String format = "YAML";


    private final Path path;

    /**
     * Read the path to a node tree.
     *
     * @return Parsed tree.
     */
    @Override
    public JsonNode read() throws IOException {
        var mapper = new YAMLMapper();
        return mapper.readTree(this.path.toFile());
    }

}
