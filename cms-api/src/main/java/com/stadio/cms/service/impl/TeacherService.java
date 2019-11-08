package com.stadio.cms.service.impl;

import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.ITeacherService;
import com.stadio.common.utils.ResponseCode;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Teacher;
import com.stadio.model.dtos.cms.TeacherFormDTO;
import com.stadio.model.dtos.cms.TeacherItemDTO;
import com.stadio.model.repository.main.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class TeacherService extends BaseService implements ITeacherService {

    @Autowired
    TeacherRepository teacherRepository;

    @Override
    public ResponseResult processCreateOneTeacher(TeacherFormDTO teacherFormDTO) {
        ResponseResult responseResult = invalidTeacherForm(teacherFormDTO);
        if(responseResult!=null) {
            return responseResult;
        }

        Teacher teacher = new Teacher();

        teacher.setName(teacherFormDTO.getName());
        teacher.setAge(teacherFormDTO.getAge());
        teacher.setPhone(teacherFormDTO.getPhone());
        teacher.setSchool(teacherFormDTO.getSchool());
        teacher.setSubject(teacherFormDTO.getSubject());
        teacher.setDescription(teacherFormDTO.getDescription());
        teacher.setPictureUrl(teacherFormDTO.getPictureUrl());

        teacherRepository.save(teacher);

        return ResponseResult.newSuccessInstance(teacher);
    }

    @Override
    public ResponseResult processUpdateOneTeacher(TeacherFormDTO teacherFormDTO) {
        ResponseResult responseResult = invalidTeacherForm(teacherFormDTO);
        if(responseResult!=null) {
            return responseResult;
        }

        if (!StringUtils.isNotNull(teacherFormDTO.getId()))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("teacher.invalid.id"), null);
        }

        Teacher teacher = teacherRepository.findOne(teacherFormDTO.getId());

        if(teacher==null){
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("teacher.invalid.teacher"), null);
        }

        teacher.setName(teacherFormDTO.getName());
        teacher.setAge(teacherFormDTO.getAge());
        teacher.setPhone(teacherFormDTO.getPhone());
        teacher.setSchool(teacherFormDTO.getSchool());
        teacher.setSubject(teacherFormDTO.getSubject());
        teacher.setDescription(teacherFormDTO.getDescription());
        teacher.setPictureUrl(teacherFormDTO.getPictureUrl());

        teacherRepository.save(teacher);

        return ResponseResult.newSuccessInstance(teacher);
    }

    @Override
    public ResponseResult processGetAllTeacher() {
        List<Teacher> teacherList = teacherRepository.findByDeletedIs(false);
        List<TeacherItemDTO> teacherItemDTOList = new LinkedList<>();
        if(teacherList!=null){
            teacherList.stream().forEach(teacher -> {
                TeacherItemDTO teacherItemDTO = new TeacherItemDTO(teacher);
                teacherItemDTOList.add(teacherItemDTO);
            });
        }
        return ResponseResult.newSuccessInstance(teacherItemDTOList);
    }

    @Override
    public ResponseResult processDeleteTeacher(String id) {
        Teacher teacher = teacherRepository.findOne(id);
        if(teacher!=null){
            teacher.setDeleted(true);
            teacherRepository.save(teacher);
        }
        return ResponseResult.newSuccessInstance("delete success");
    }

    private ResponseResult invalidTeacherForm(TeacherFormDTO teacherFormDTO){
        if(teacherFormDTO == null){
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"teacher.invalid.teacher");
        }
        if(!StringUtils.isValid(teacherFormDTO.getName())){
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"teacher.invalid.name");
        }
        if(teacherFormDTO.getAge()==null){
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"teacher.invalid.age");
        }

        if(!StringUtils.isValid(teacherFormDTO.getPhone())){
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"teacher.invalid.phone");
        }

        if(!StringUtils.isValid(teacherFormDTO.getSubject())){
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM,"teacher.invalid.subject");
        }
        return null;
    }
}
