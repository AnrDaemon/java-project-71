package org.rootdir.hexlet.java.m2k;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NodeStatus {

    public static final String ADDED = "added";
    public static final String UPDATED = "updated";
    public static final String REMOVED = "removed";
    public static final String UNCHANGED = "unchanged";

    private final String name;
    private final Object oldValue;
    private final Object newValue;
    private final String status;

}
