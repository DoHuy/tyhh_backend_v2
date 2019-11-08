package com.stadio.mobi.dtos.theory;

import com.stadio.model.documents.Theory;
import lombok.Data;


@Data
public class TheoryItemDTO {

    private String id;
    private String name;
    private Boolean isRead;
    private double examProgress;

    public static TheoryItemDTO with(Theory theory) {
        return new TheoryItemDTO(theory);
    }

    public TheoryItemDTO(Theory theory) {
        this.id = theory.getId();
        this.name = theory.getName();
    }
}
