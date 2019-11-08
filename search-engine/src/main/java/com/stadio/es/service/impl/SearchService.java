package com.stadio.es.service.impl;

import com.stadio.common.utils.JsonUtils;
import com.stadio.common.utils.StringUtils;
import com.stadio.es.response.ResponseResult;
import com.stadio.es.service.ISearchService;
import com.stadio.model.es.documents.ESChemicalEquation;
import com.stadio.model.es.documents.ESExam;
import com.stadio.model.es.dtos.ExamItemDTO;
import com.stadio.model.es.repository.ESChemicalEquationRepository;
import com.stadio.model.es.repository.ESExamRepository;
import com.stadio.model.repository.main.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SearchService extends BaseService implements ISearchService {

    @Autowired
    ESExamRepository examESRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    ESChemicalEquationRepository esChemicalEquationRepository;

    @Override
    public ResponseResult processSearchExam(String text) {

        text = StringUtils.normalized(text);

        PageRequest request = new PageRequest(0, 15);
        List<ESExam> examESList = new LinkedList<>();
        List<ExamItemDTO> examItemDTOList = new LinkedList<>() ;

        try {
            if (text.trim().indexOf(" ") == -1) {
                examESList = examESRepository.findByKeywordsIsLikeAndEnableIsAndDeletedIsOrderByCreatedDate(text,true,false,request).getContent();
            } else {
                examESList = examESRepository.findByKeywordsRegexAndEnableIsAndDeletedIsOrderByCreatedDate(text,true,false,request).getContent();
            }

            if (examESList!=null && examESList.size() > 0) {
                examESList.stream().forEach(examES -> {
                    ExamItemDTO examItemDTO = ExamItemDTO.with(JsonUtils.writeValue(examES));
                    examItemDTOList.add(examItemDTO);
                });
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return ResponseResult.newSuccessInstance(examItemDTOList);
    }

    @Override
    public ResponseResult processSearchChemicalEquation(String content) {
        List<ESChemicalEquation> esChemicalEquationList = esChemicalEquationRepository.findByContentMatches(content, new PageRequest(0,15)).getContent();
        List<String> result = new LinkedList<>();
        if(esChemicalEquationList!=null){
            esChemicalEquationList.forEach(esChemicalEquation -> {
                result.add(esChemicalEquation.getContent());
            });
        }
        return ResponseResult.newSuccessInstance(result);
    }
}
