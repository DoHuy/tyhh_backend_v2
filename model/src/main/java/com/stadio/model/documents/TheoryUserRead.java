package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "tab_theory_user_read")
public class TheoryUserRead {

    @Id
    private String id;

    @TextIndexed
    @Field("user_id_ref")
    private String userIdRef;

    @TextIndexed
    @Field("theory_id_ref")
    private String theoryIdRef;

}
