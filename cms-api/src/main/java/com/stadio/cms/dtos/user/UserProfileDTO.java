package com.stadio.cms.dtos.user;

import com.stadio.model.documents.Clazz;
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

    private long birthDay;

    private String clazz;

    private String clazzId;

    private String idNumber; //so chung minh thu

    private Date idIssueDate; //ngay cap

    private long balance;

    private Date createdDate;

    public UserProfileDTO() {

    }

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
        this.setClazzId(user.getClazzId());

        try {
            this.setBirthDay(user.getBirthDay().getTime()/1000);
        } catch (Exception e ) {
        }

        Clazz clazz1 = clazzRepository.findOneById(user.getClazzId());

        if (clazz1 != null){
            this.setClazz(clazz1.getName());
        }

        this.setIdNumber(user.getIdNumber());
        this.setIdIssueDate(user.getIdIssueDate());
        this.setBalance(user.getBalance());
        this.setCreatedDate(user.getCreatedDate());
    }

}
