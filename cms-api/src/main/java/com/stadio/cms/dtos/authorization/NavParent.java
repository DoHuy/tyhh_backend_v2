package com.stadio.cms.dtos.authorization;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class NavParent {

    private boolean title;
    private String name;
    private String icon;
    private String url;
    private Set<String> roles = new HashSet<>();
    private List<NavChild> children;

}
