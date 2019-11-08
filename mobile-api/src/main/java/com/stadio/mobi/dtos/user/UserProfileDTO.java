package com.stadio.mobi.dtos.user;

import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.hoc68.users.documents.User;
import com.stadio.model.repository.main.ClazzRepository;
import lombok.Data;

import java.util.Date;

@Data
public class UserProfileDTO {

    private String id;
    private String username;
    private boolean enabled;
    private String avatar;
    private String email;
    private String phone;
    private String address;
    private String fullName;
    private String firstName;
    private String lastName;
    private String facebookId;
    private String googleId;
    private Date birthDay;
    private String clazz;
    private String clazzId;
    private String idNumber; //so chung minh thu
    private Date idIssueDate; //ngay cap
    private long balance;

    private Date createdDate;
    private Date updatedDate;

    private String code;

    public UserProfileDTO() { }

    public UserProfileDTO(User user, ClazzRepository clazzRepository) {
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setPhone(user.getPhone());
        this.setEnabled(user.isEnabled());
        this.setAvatar(user.getAvatar());
        this.setEmail(user.getEmail());
        this.setPhone(user.getPhone());
        this.setAddress(user.getAddress());
        this.setFullName(user.getFullName());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setFacebookId(user.getFacebookId());
        this.setGoogleId(user.getGoogleId());
        this.setBirthDay(user.getBirthDay());
        this.setIdNumber(user.getIdNumber());
        this.setIdIssueDate(user.getIdIssueDate());
        this.setBalance(user.getBalance());
        this.setCreatedDate(user.getCreatedDate());
        this.setUpdatedDate(user.getUpdatedDate());
        this.setClazzId(user.getClazzId());
        this.setCode(user.getCode());

        if (user.getClazzId() != null) {
            Clazz clazzOj = clazzRepository.findOneById(user.getClazzId());
            if (clazzOj != null) {
                this.setClazz(clazzOj.getName());
            }
        }
    }

}
