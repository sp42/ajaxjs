package com.ajaxjs.developertools.monitor.model.jvm;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

import java.util.List;

/**
 * Node
 */
@Data
public class Node implements IBaseModel {
    private String key;

    private String fullName;

    private String title;

    private String nodeType;

    private List<Node> children;
}

