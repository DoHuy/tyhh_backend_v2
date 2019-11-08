package com.stadio.cms.dtos.user;

import com.hoc68.users.documents.User;
import lombok.Data;

@Data
public class UserPointDTO {

    private String id;

    private String username;

    private String fullName;

    private String avatar;

    private Double point;

    private String address;

    public UserPointDTO(User user, Double point){
        this.id = user.getId();
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.avatar = user.getAvatar();
        this.address = user.getAddress();
        this.point = point;
    }
}
