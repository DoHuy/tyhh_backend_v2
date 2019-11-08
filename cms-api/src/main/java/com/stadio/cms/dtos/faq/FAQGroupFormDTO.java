package com.stadio.cms.dtos.faq;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FAQGroupFormDTO {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    private Boolean deleted;
}
