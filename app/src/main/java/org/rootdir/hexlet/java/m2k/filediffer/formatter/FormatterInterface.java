package org.rootdir.hexlet.java.m2k.filediffer.formatter;

import java.util.List;
import org.rootdir.hexlet.java.m2k.filediffer.NodeStatus;

public interface FormatterInterface {
    public String format(List<NodeStatus> diff);
}
