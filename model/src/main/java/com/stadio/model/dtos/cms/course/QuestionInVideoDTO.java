package com.stadio.model.dtos.cms.course;

import com.stadio.model.documents.Question;
import com.stadio.model.dtos.cms.QuestionDetailDTO;
import com.stadio.model.model.course.QuestionInVideo;
import com.stadio.model.repository.main.QuestionRepository;
import lombok.Data;

@Data
public class QuestionInVideoDTO implements Comparable {

    private QuestionDetailDTO questionDetail;

    private int positionInSecond;

    public QuestionInVideoDTO(QuestionInVideo questionInVideo, QuestionRepository questionRepository) {
        this.positionInSecond = questionInVideo.getPositionInSecond();
        try {
            Question question = questionRepository.findOne(questionInVideo.getQuestionId());
            QuestionDetailDTO questionDetailDTO = new QuestionDetailDTO(question);
            this.questionDetail = questionDetailDTO;
        } catch (Exception e){}
    }

    @Override
    public int compareTo(Object o) {
        QuestionInVideoDTO question = (QuestionInVideoDTO) o;
        return this.positionInSecond - question.getPositionInSecond();
    }
}
