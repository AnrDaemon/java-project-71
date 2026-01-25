package org.rootdir.hexlet.java.m2k.interfaces;

import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;

public interface TreeParserInterface {

    public Map<String, JsonNode> parse(JsonNode tree);

}
