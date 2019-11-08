package com.stadio.cms.dtos.faq;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FAQFormDTO {

    private String id;

    @NotNull
    private String question;

    @NotNull
    private String answer;

    @NotNull
    private String groupId;

    private Boolean deleted;

}
