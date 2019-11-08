package com.stadio.cms.service.impl;

import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.ISectionService;
import com.stadio.common.utils.ResponseCode;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Section;
import com.stadio.model.dtos.cms.SectionFormDTO;
import com.stadio.model.dtos.cms.SectionItemDTO;
import com.stadio.model.repository.main.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SectionService extends BaseService implements ISectionService {

    @Autowired
    SectionRepository sectionRepository;

    @Override
    public ResponseResult processGetListSection(String courseId) {
        List<Section> sectionList = sectionRepository.findByCourseIdIsAndDeletedIsOrderByPositionAsc(courseId,false);
        List<SectionItemDTO> sectionItemDTOList = new LinkedList<>();
        if(sectionList!=null){
            sectionList.stream().forEach(section -> {
                SectionItemDTO sectionItemDTO = new SectionItemDTO(section);
                sectionItemDTOList.add(sectionItemDTO);
            });
        }
        return ResponseResult.newSuccessInstance(sectionItemDTOList);
    }

    @Override
    public ResponseResult processCreateSection(SectionFormDTO sectionFormDTO) {
        if(!StringUtils.isValid(sectionFormDTO.getName()))
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"section.invalid.name");
        if(!StringUtils.isValid(sectionFormDTO.getCourseId()))
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"section.invalid.courseId");

        Section section = new Section();
        section.setName(sectionFormDTO.getName());
        section.setCourseId(sectionFormDTO.getCourseId());
        section.setPosition(sectionRepository.countByCourseIdIs(sectionFormDTO.getCourseId())+1);
        sectionRepository.save(section);

        return ResponseResult.newSuccessInstance(section);
    }

    @Override
    public ResponseResult processUpdateSection(SectionFormDTO sectionFormDTO) {
        if(!StringUtils.isValid(sectionFormDTO.getName()))
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"section.invalid.name");
        if(!StringUtils.isValid(sectionFormDTO.getCourseId()))
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"section.invalid.courseId");
        if(!StringUtils.isValid(sectionFormDTO.getId()))
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"section.invalid.id");

        Section section = sectionRepository.findOne(sectionFormDTO.getId());
        if(section!=null){
            section.setName(sectionFormDTO.getName());
            sectionRepository.save(section);
            return ResponseResult.newSuccessInstance(section);
        }else
            return ResponseResult.newErrorInstance(ResponseCode.FAIL,"section.not.exist");
    }

    @Override
    public ResponseResult processDeleteSection(String id) {
        Section section = sectionRepository.findOne(id);
        if(section!=null){
            section.setDeleted(true);
            sectionRepository.save(section);
        }
        return ResponseResult.newSuccessInstance("section.delete.success");
    }
}
