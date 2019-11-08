package com.stadio.mobi.dtos.user;

import com.hoc68.users.documents.User;
import com.stadio.model.documents.Clazz;
import com.stadio.model.repository.main.ClazzRepository;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfoDTO {
    private String id;
    private String username;
    private String avatar;
    private String address;
    private String fullName;
    private Date birthDay;
    private String clazz;
    private Date createdDate;
    private String code;

    public UserInfoDTO() { }

    public UserInfoDTO(User user, ClazzRepository clazzRepository) {
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setAvatar(user.getAvatar());
        this.setAddress(user.getAddress());
        this.setFullName(user.getFullName());
        this.setBirthDay(user.getBirthDay());
        this.setCreatedDate(user.getCreatedDate());
        this.setCode(user.getCode());

        if (user.getClazzId() != null) {
            Clazz clazzOj = clazzRepository.findOneById(user.getClazzId());
            if (clazzOj != null) {
                this.setClazz(clazzOj.getName());
            }
        }
    }
}
