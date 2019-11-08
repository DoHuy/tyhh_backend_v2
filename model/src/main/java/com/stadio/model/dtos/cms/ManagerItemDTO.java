package com.stadio.model.dtos.cms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoc68.users.defines.RoleType;
import com.hoc68.users.documents.Manager;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class ManagerItemDTO
{
    private String id;
    private String fullName;
    private String username;
    private String email;
    private String phone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd/MM/yyyy")
    private Date createdDate;
    private String role;
    private Boolean isBlock;
    private Map<String, Object> action = new HashMap<>();

    public ManagerItemDTO(Manager manager){
        this.id = manager.getId();

        this.fullName = manager.getFullName();
        this.email = manager.getEmail();
        this.phone = manager.getPhone();
        this.username = manager.getUsername();
        this.createdDate = manager.getCreatedDate();
        this.isBlock = manager.isBlock();

    }
}
