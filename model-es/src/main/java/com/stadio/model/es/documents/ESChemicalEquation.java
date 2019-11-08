package com.stadio.model.es.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "chemistry",type = "chemicalEquation")
public class ESChemicalEquation {
    @Id
    private String id;

    @Field(type = FieldType.String)
    private String content;
}
