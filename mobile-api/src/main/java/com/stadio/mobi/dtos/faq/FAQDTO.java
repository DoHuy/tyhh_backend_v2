package com.stadio.mobi.dtos.faq;

import com.stadio.model.documents.FAQ;
import lombok.Data;

import java.util.Date;

@Data
public class FAQDTO {

    private String id;
    private String question;
    private String answer;
    private Date updateDate;

    public FAQDTO(FAQ faq){
        this.id = faq.getId();
        this.question = faq.getQuestion();
        this.answer = faq.getAnswer();
        this.updateDate = faq.getUpdatedDate();
    }
}
