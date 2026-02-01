package org.rootdir.hexlet.java.m2k.filediffer;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NodeStatus {

    public static final String ADDED = "added";
    public static final String UPDATED = "updated";
    public static final String REMOVED = "removed";
    public static final String UNCHANGED = "unchanged";

    @EqualsAndHashCode.Include
    private final String name;

    private final Object oldValue;

    private final Object newValue;

    @EqualsAndHashCode.Include
    private final String status;

}
