package com.stadio.cms.service;

import com.stadio.cms.dtos.faq.FAQFormDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.model.documents.FAQ;

import java.util.List;

public interface IFAQService {

    ResponseResult processCreateFAQ(FAQFormDTO faqFormDTO);

    ResponseResult processUpdateFAQ(FAQFormDTO faqFormDTO);

    ResponseResult processDeleteFAQ(String id);

    List<FAQ> processGetListFAQInGroup(String groupId);
}
