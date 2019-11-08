package com.stadio.task.component;

import com.stadio.common.utils.JsonUtils;
import com.stadio.model.documents.Exam;
import com.stadio.model.es.documents.ESExam;
import com.stadio.model.es.repository.ESExamRepository;
import com.stadio.model.model.ExamManagerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExamManagerSyncTask {
    private Logger logger = LogManager.getLogger(ExamManagerSyncTask.class);

    @Autowired
    ESExamRepository examESRepository;

    @RabbitListener(queues = "#{queueExamManager.name}")
    public void receiveSubmit(String msg) {
        try{
            ExamManagerEvent examManagerEvent = JsonUtils.parse(msg, ExamManagerEvent.class);
            switch (examManagerEvent.getEvent()){
                case EXAM_CREATE:
                    Exam exam = JsonUtils.parse(examManagerEvent.getObject(), Exam.class);
                    ESExam examES = ESExam.with(JsonUtils.writeValue(exam));
                    examESRepository.save(examES);
                    break;
                case EXAM_UPDATE:
                    exam = JsonUtils.parse(examManagerEvent.getObject(), Exam.class);
                    examES = examESRepository.findFirstByExamId(exam.getId());
                    if (examES != null) {
                        examES.update(examManagerEvent.getObject());
                        examESRepository.save(examES);
                    }
                    break;
                case EXAME_DELETE:
                    exam = JsonUtils.parse(examManagerEvent.getObject(), Exam.class);
                    examES = examESRepository.findFirstByExamId(exam.getId());
                    if (examES != null) {
                        examES.update(examManagerEvent.getObject());
                        examESRepository.save(examES);
                    }
                    break;
            }
        }catch (Exception e){
            logger.error("recieve Exam manager ",e);
        }
    }
}
