package com.stadio.es.service.impl;

import com.stadio.common.utils.JsonUtils;
import com.stadio.model.es.documents.ESExam;
import com.stadio.model.es.repository.ESExamRepository;
import com.stadio.es.service.IExamService;
import com.stadio.model.documents.Exam;
import com.stadio.model.repository.main.ExamRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService extends BaseService implements IExamService{
    private Logger logger = LogManager.getLogger(ExamService.class);

    @Autowired
    ESExamRepository examESRepository;

    @Autowired
    ExamRepository examRepository;

    @Override
    public void resetData() {
        examESRepository.deleteAll();

        long movieListSize = examRepository.count();
        int pageSize = 30;
        long pageQuantity = movieListSize / pageSize;
        int page =0;

        while (page <= pageQuantity){
            List<Exam> examList = examRepository.findAll(new PageRequest(page, pageSize)).getContent();
            examList.parallelStream().forEach(exam -> {
                ESExam examES = ESExam.with(JsonUtils.writeValue(exam));
                examESRepository.save(examES);
            });
            page+=1;
            logger.info("reset exam elastic page {}",page);
        }
    }
}
