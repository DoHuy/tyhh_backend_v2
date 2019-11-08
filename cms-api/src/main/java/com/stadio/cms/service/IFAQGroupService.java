package com.stadio.cms.service;

import com.stadio.cms.dtos.faq.FAQGroupFormDTO;
import com.stadio.cms.response.ResponseResult;

public interface IFAQGroupService {

    ResponseResult processGetListFAQGroup();

    ResponseResult processCreateFAQGroup(FAQGroupFormDTO faqGroupFormDTO);

    ResponseResult processDeleteFAQGroup(String id);

    ResponseResult processUpdateFAQGroup(FAQGroupFormDTO faqGroupFormDTO);

}
