package com.stadio.cms.service.impl;

import com.hoc68.users.documents.Manager;
import com.stadio.cms.dtos.faq.FAQGroupDTO;
import com.stadio.cms.dtos.faq.FAQGroupFormDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IFAQGroupService;
import com.stadio.cms.service.IFAQService;
import com.stadio.common.utils.ResponseCode;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.FAQ;
import com.stadio.model.documents.FAQGroup;
import com.stadio.model.repository.main.FAQGroupRepository;
import com.stadio.model.repository.user.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FAQGroupService extends BaseService implements IFAQGroupService {

    @Autowired
    IFAQService ifaqService;

    @Autowired
    FAQGroupRepository faqGroupRepository;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired ManagerService managerService;

    @Override
    public ResponseResult processGetListFAQGroup() {
        List<FAQGroup> faqGroupList = faqGroupRepository.findAllByDeletedFalse();
        List<FAQGroupDTO> faqGroupDTOList = new ArrayList<>();
        if (faqGroupList != null){
            faqGroupList.forEach(faqGroup -> {
                List<FAQ> faqList = ifaqService.processGetListFAQInGroup(faqGroup.getId());
                Manager manager = managerRepository.findOne(faqGroup.getUpdatedBy());
                FAQGroupDTO faqGroupDTO = new FAQGroupDTO(faqList, faqGroup, manager);
                faqGroupDTOList.add(faqGroupDTO);
            });
        }

        return ResponseResult.newSuccessInstance(faqGroupDTOList);
    }

    @Override
    public ResponseResult processCreateFAQGroup(FAQGroupFormDTO faqGroupFormDTO) {
        FAQGroup faqGroup = new FAQGroup();
        faqGroup.setName(faqGroupFormDTO.getName());
        faqGroup.setDescription(faqGroupFormDTO.getDescription());

        Manager manager = managerService.getManagerRequesting();
        faqGroup.setCreatedBy(manager.getId());
        faqGroup.setUpdatedBy(manager.getId());

        faqGroupRepository.save(faqGroup);

        return ResponseResult.newSuccessInstance(faqGroup);
    }

    @Override
    public ResponseResult processDeleteFAQGroup(String id) {

        if (StringUtils.isNotNull(id)) {
            FAQGroup faqGroup = faqGroupRepository.findOne(id);
            if (faqGroup != null && faqGroup.getDeleted() == false){
                Manager manager = managerService.getManagerRequesting();
                faqGroup.setUpdatedDate(new Date());
                faqGroup.setUpdatedBy(manager.getId());
                faqGroup.setDeleted(true);
                faqGroupRepository.save(faqGroup);
                return ResponseResult.newSuccessInstance(null);
            }
        }
        return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM, this.getMessage("faq.group.invalid.id"));

    }

    @Override
    public ResponseResult processUpdateFAQGroup(FAQGroupFormDTO faqGroupFormDTO) {
        if (faqGroupFormDTO != null && StringUtils.isNotNull(faqGroupFormDTO.getId())) {
            FAQGroup faqGroup = faqGroupRepository.findOne(faqGroupFormDTO.getId());
            if (faqGroup != null){
                Manager manager = managerService.getManagerRequesting();
                faqGroup.setName(faqGroupFormDTO.getName());
                faqGroup.setDescription(faqGroupFormDTO.getDescription());
                faqGroup.setUpdatedDate(new Date());
                faqGroup.setUpdatedBy(manager.getId());
                if (faqGroupFormDTO.getDeleted() != null){
                    faqGroup.setDeleted(faqGroupFormDTO.getDeleted());
                }
                faqGroupRepository.save(faqGroup);
                return ResponseResult.newSuccessInstance(faqGroup);
            }
        }
        return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM, this.getMessage("faq.group.invalid.id"));
    }
}
