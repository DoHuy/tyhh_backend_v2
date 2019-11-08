package com.stadio.mobi.service.impl;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.ISearchService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.dtos.mobility.ExamItemDTO;
import com.stadio.model.repository.main.ExamRepository;
import com.stadio.model.repository.main.SearchKeywordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 01/09/2018.
 */
@Service
public class SearchService implements ISearchService
{
    @Autowired SearchKeywordsRepository searchKeywordsRepository;

    @Autowired ExamRepository examRepository;

    @Autowired ExamService examService;

    @Override
    public List<SearchKeywords> getKeywords()
    {
        return searchKeywordsRepository.findAll();
    }

    @Override
    public ResponseResult processSearch(String keyword) {

        List<ExamItemDTO> examItemDTOList = new ArrayList<>();
        if(keyword!=null && !keyword.equals("")) {
            List<Exam> exams = examRepository.fullTextSearch(keyword,1,10);
            if (exams != null) {
                exams.forEach(exam -> {
                    if (exam.isEnable() && !exam.isDeleted()) {
                        ExamItemDTO examItemDTO = ExamItemDTO.with(exam);
                        examItemDTOList.add(examItemDTO);
                    }
                });
            }
        }

        return ResponseResult.newSuccessInstance(examItemDTOList);
    }

}
