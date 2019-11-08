package com.stadio.cms.dtos.authorization;

import lombok.Data;

@Data
public class FeatureChild {

    private String name;
    private String fullPath;
    private String hash;
    private String method;
    private boolean selected;

    public FeatureChild() {}

    public FeatureChild(String name, String path, String fullPath) {
        this.name = name;
        this.fullPath = fullPath;
    }

}
