package com.stadio.cms.dtos.faq;

import com.hoc68.users.documents.Manager;
import com.stadio.cms.service.impl.ManagerService;
import com.stadio.model.documents.FAQ;
import com.stadio.model.documents.FAQGroup;
import com.stadio.model.repository.user.ManagerRepository;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FAQGroupDTO {
    private String id;

    private String name;

    private String description;

    private Date updatedDate;

    private String updatedBy;

    private List<FAQ> faqList;

    public FAQGroupDTO(List<FAQ> faqList, FAQGroup group, Manager manager){
        this.id = group.getId();
        this.name = group.getName();
        this.description = group.getDescription();
        this.updatedDate = group.getUpdatedDate();
        this.updatedBy = manager.getFullName();
        this.faqList = faqList;
    }
}
