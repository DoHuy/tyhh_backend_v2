package com.stadio.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "tab_sequence_index")
public class SequenceIndex {

    @Id
    private String id;

    @Field(value = "sequence_key")
    private String sequenceKey;

    @Field(value = "current_sequence")
    private long currentSequence;

}
