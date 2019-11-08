package com.stadio.model.dtos.cms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.model.documents.Clazz;
import com.hoc68.users.documents.User;
import com.stadio.model.repository.main.ClazzRepository;
import lombok.Data;

import java.util.Date;

@Data
public class UserListDTO {
    private String id;
    private String avatar;
    private String code;
    private String fullName;
    private String username;
    private String email;
    private String clazz;
    private String phone;
    private Boolean enabled;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date createdDate;

    public UserListDTO(User user, ClazzRepository clazzRepository) {
        this.id=user.getId();
        this.avatar = user.getAvatar();
        this.code = user.getCode();
        this.fullName = user.getFullName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.enabled = user.isEnabled();
        this.createdDate = user.getCreatedDate();

        try {
            Clazz clazz1 = clazzRepository.findOneById(user.getClazzId());
            if (clazz1 != null) {
                this.clazz = clazz1.getName();
            }
        } catch (Exception e) {

        }

    }

    @Override
    public String toString() {
        return "UserListDTO{" +
                "id='" + id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", code='" + code + '\'' +
                ", fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", clazz=" + clazz +
                ", phone='" + phone + '\'' +
                ", enabled=" + enabled +
                ", createdDate=" + createdDate +
                '}';
    }
}
