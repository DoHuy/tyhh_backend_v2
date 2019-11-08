package com.stadio.model.dtos.cms;

import lombok.Data;

@Data
public class TeacherFormDTO {

    private String id;

    private String name;

    private Integer age;

    private String phone;

    private String school;

    private String subject;

    private String pictureUrl;

    private String description;
}
