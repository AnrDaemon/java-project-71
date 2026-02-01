package org.rootdir.hexlet.java.m2k.filereader;

import java.io.IOException;
import java.nio.file.Path;
import com.fasterxml.jackson.databind.JsonNode;

public class SourceFileReader implements FileReaderInterface {

    private FileReaderInterface reader;

    private static String getFileExtension(String fileName) {
        int extPos = fileName.lastIndexOf('.');
        return (extPos > 0 && extPos < fileName.length() - 1) ? fileName.substring(extPos + 1) : "";
    }

    /**
     * @return File format name.
     */
    @Override
    public String getFormat() {
        return reader.getFormat();
    }

    /**
     * @return File tree structure.
     */
    @Override
    public JsonNode read() throws IOException {
        return reader.read();
    }


    public SourceFileReader(Path path) {
        var ext = SourceFileReader.getFileExtension(path.getFileName().toString()).toLowerCase();

        this.reader = switch (ext) {
            case "yaml", "yml" -> new YamlFileReader(path);
            case "json" -> new JsonFileReader(path);
            default -> throw new IllegalArgumentException("Unsupported file format");
        };
    }

}
