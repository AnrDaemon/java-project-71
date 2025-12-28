package org.rootdir.hexlet.java.m2k;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NodeStatus {

    private final String name;
    private final Object content;
    private final String status;

}
