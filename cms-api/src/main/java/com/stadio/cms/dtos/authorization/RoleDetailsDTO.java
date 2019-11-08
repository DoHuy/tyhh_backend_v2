package com.stadio.cms.dtos.authorization;

import lombok.Data;

import java.util.List;

@Data
public class RoleDetailsDTO {

    private String id;
    private String name;
    private String description;
    private List<FeatureParent> featureParentList;
}
