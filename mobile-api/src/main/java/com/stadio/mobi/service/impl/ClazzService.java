package com.stadio.mobi.service.impl;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IClazzService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.dtos.cms.ClazzListDTO;
import com.stadio.model.dtos.mobility.ExamItemDTO;
import com.stadio.model.repository.main.ClazzRepository;
import com.stadio.model.repository.main.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClazzService extends BaseService implements IClazzService {

    @Autowired ClazzRepository clazzRepository;

    @Autowired ExamRepository examRepository;

    @Autowired ExamService examService;


    @Override
    public ResponseResult processGetAllClazz()
    {

        List<Clazz> clazzList = clazzRepository.findAll();
        List<ClazzListDTO> clazzListDTOS = new ArrayList<>();

        if (clazzList != null)
        {
            clazzList.forEach(clazz ->
            {
                if (!clazz.isDeleted())
                {
                    clazzListDTOS.add(new ClazzListDTO(clazz));
                }
            });
        }

        return ResponseResult.newSuccessInstance(clazzListDTOS);
    }

    @Override
    public ResponseResult processGetExamByClazzId(String token, String clazzId, int page, int limit)
    {
        PageRequest request = new PageRequest(page - 1, limit);
        List<Exam> examList = examRepository.findExamByClazzIdOrderByCreatedByDesc(clazzId, request).getContent();
        List<ExamItemDTO> examItemDTOList = examService.getListExamItemDTO(examList, token);
        return ResponseResult.newSuccessInstance(examItemDTOList);
    }


}
