package com.stadio.mobi.dtos.faq;

import com.stadio.model.documents.FAQ;
import com.stadio.model.documents.FAQGroup;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class FAQGroupDTO {
    private String id;
    private String name;
    private String description;
    private Date updatedDate;
    private List<FAQDTO> faqList;

    public FAQGroupDTO(FAQGroup faqGroup, List<FAQ> faqList){
        List<FAQDTO> faqDTOList = new ArrayList<>();
        if (faqList != null) {
            faqList.forEach(faq -> {
                faqDTOList.add(new FAQDTO(faq));
            });
        }
        this.faqList = faqDTOList;
        this.id = faqGroup.getId();
        this.name = faqGroup.getName();
        this.description = faqGroup.getDescription();
        this.updatedDate = faqGroup.getUpdatedDate();
    }
}
