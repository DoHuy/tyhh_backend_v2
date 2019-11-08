package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "tab_role_menu")
@Data
public class RoleMenu {

    @Id
    private String id;

    @Field(value = "role_id")
    private String roleId;

    @Field(value = "menu_id")
    private String menuId;
}
