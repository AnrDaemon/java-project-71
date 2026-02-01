package org.rootdir.hexlet.java.m2k.filereader;

import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;

public interface FileReaderInterface {

    String getFormat();

    JsonNode read() throws IOException;
}
