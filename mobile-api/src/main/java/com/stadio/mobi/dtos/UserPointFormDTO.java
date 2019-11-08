package com.stadio.mobi.dtos;

import com.stadio.model.enu.UserPointType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserPointFormDTO {

    @NotNull
    private String userId;

    @NotNull
    private Double point;

    @NotNull
    private String sourceName;

    @NotNull
    private UserPointType sourceType;

    private String sourceId;

}
