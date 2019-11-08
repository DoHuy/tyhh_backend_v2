package com.stadio.cms.service.impl;

import com.hoc68.users.documents.Manager;
import com.stadio.cms.dtos.faq.FAQFormDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IFAQService;
import com.stadio.common.utils.ResponseCode;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.FAQ;
import com.stadio.model.repository.main.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FAQService extends BaseService implements IFAQService {

    @Autowired
    FAQRepository faqRepository;

    @Autowired ManagerService managerService;


    @Override
    public ResponseResult processCreateFAQ(FAQFormDTO faqFormDTO) {

        FAQ faq = new FAQ();

        faq.setQuestion(faqFormDTO.getQuestion());
        faq.setAnswer(faqFormDTO.getAnswer());
        faq.setGroupId(faqFormDTO.getGroupId());

        Manager current = managerService.getManagerRequesting();

        faq.setCreatedBy(current.getId());
        faq.setUpdatedBy(current.getId());
        faqRepository.save(faq);
        return ResponseResult.newSuccessInstance(faq);
    }

    @Override
    public ResponseResult processUpdateFAQ(FAQFormDTO faqFormDTO) {
        Manager current = managerService.getManagerRequesting();

        if (faqFormDTO != null && StringUtils.isNotNull(faqFormDTO.getId())) {
            FAQ faq = faqRepository.findOne(faqFormDTO.getId());
            if (faq != null) {
                faq.setGroupId(faqFormDTO.getGroupId());
                faq.setUpdatedBy(current.getId());
                faq.setUpdatedDate(new Date());
                faq.setQuestion(faqFormDTO.getQuestion());
                faq.setAnswer(faqFormDTO.getAnswer());

                if (faqFormDTO.getDeleted() != null){
                    faq.setDeleted(faqFormDTO.getDeleted());
                }
                faqRepository.save(faq);
                return ResponseResult.newSuccessInstance(faq);
            }
        }

        return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM, this.getMessage("faq.invalid.id"));
    }

    @Override
    public ResponseResult processDeleteFAQ(String id) {
        Manager current = managerService.getManagerRequesting();

        if (StringUtils.isNotNull(id)) {
            FAQ faq = faqRepository.findOne(id);
            if (faq != null && faq.getDeleted() == false){
                faq.setUpdatedDate(new Date());
                faq.setUpdatedBy(current.getId());
                faq.setDeleted(true);
                faqRepository.save(faq);
                return ResponseResult.newSuccessInstance(null);
            }
        }

        return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM, this.getMessage("faq.invalid.id"));
    }

    @Override
    public List<FAQ> processGetListFAQInGroup(String groupId) {
        return faqRepository.findFAQSByGroupIdAndDeletedFalse(groupId);
    }
}
