package com.stadio.cms.dtos.authorization;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FeatureParent {

    private String name;
    private String path;
    private List<FeatureChild> children = new ArrayList<>();
    private String hash;
    private String controller;
    private boolean selected;

    public FeatureParent() {}

    public FeatureParent(String name, String path, String controller) {
        this.name = name;
        this.path = path;
        this.controller = controller;
    }
}
