package com.stadio.model.documents;

import com.hoc68.users.model.UserBase;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "md_menu")
public class MDMenu {

    @Id
    private String id;

    private String router;

    private String method;

    private String name;

    @Field("class_name")
    private String className;

    @Field
    private List<Integer> roles;

    public Boolean isUserCanAccess(UserBase user) {
        return roles.contains(user.getUserRole());
    }

}
