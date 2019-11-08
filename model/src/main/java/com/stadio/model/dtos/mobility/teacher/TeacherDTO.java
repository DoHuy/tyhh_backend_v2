package com.stadio.model.dtos.mobility.teacher;

import com.stadio.model.documents.Teacher;
import lombok.Data;

@Data
public class TeacherDTO {

    private String id;

    private String avatar;

    private String name;

    private String school;

    private long courseCount;

    private long studentCount;

    public TeacherDTO(Teacher teacher) {

        this.id = teacher.getId();
        this.name = teacher.getName();
        this.school = teacher.getSchool();
        this.avatar = teacher.getPictureUrl();
    }
}
