package com.stadio.mobi.service.impl;

import com.stadio.mobi.dtos.faq.FAQGroupDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IFAQService;
import com.stadio.model.documents.FAQ;
import com.stadio.model.documents.FAQGroup;
import com.stadio.model.repository.main.FAQGroupRepository;
import com.stadio.model.repository.main.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FAQService extends BaseService implements IFAQService {

    @Autowired
    FAQRepository faqRepository;

    @Autowired
    FAQGroupRepository faqGroupRepository;

    @Override
    public ResponseResult processGetListFAQ() {
        List<FAQGroup> faqGroupList = faqGroupRepository.findAllByDeletedFalse();
        List<FAQGroupDTO> faqGroupDTOList = new ArrayList<>();
        if (faqGroupList != null){
            faqGroupList.forEach(faqGroup -> {
                List<FAQ> faqList = faqRepository.findFAQSByGroupIdAndDeletedFalse(faqGroup.getId());
                if (faqList != null && faqList.size() != 0){
                    FAQGroupDTO faqGroupDTO = new FAQGroupDTO(faqGroup, faqList);
                    faqGroupDTOList.add(faqGroupDTO);
                }
            });
        }

        return ResponseResult.newSuccessInstance(faqGroupDTOList);
    }
}
