package com.stadio.model.dtos.cms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.Teacher;
import lombok.Data;

import java.util.Date;

@Data
public class TeacherItemDTO {

    private String id;

    private String name;

    private Integer age;

    private String phone;

    private String school;

    private String subject;

    private String pictureUrl;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date createdDate;

    public TeacherItemDTO(Teacher teacher){
        this.id= teacher.getId();
        this.name = teacher.getName();
        this.age = teacher.getAge();
        this.phone = teacher.getPhone();
        this.school = teacher.getSchool();
        this.subject = teacher.getSubject();
        this.pictureUrl= teacher.getPictureUrl();
        this.description = teacher.getDescription();
        this.createdDate = teacher.getCreatedDate();
    }
}
