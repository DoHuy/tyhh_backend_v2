package com.stadio.model.dtos.cms;

import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Exam;
import com.stadio.model.documents.Lecture;
import com.stadio.model.dtos.cms.course.QuestionInVideoDTO;
import com.stadio.model.model.course.QuestionInVideo;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
public class LectureDetailDTO {
    private String id;

    private String name;

    private Long position;

    private List<LectureExamItemDTO> exams = new ArrayList<>();

    private List<QuestionInVideoDTO> questionsInVideo = new ArrayList<>();

    private int videoDurration;

    private String videoId;

    public LectureDetailDTO(Lecture lecture){
        id = lecture.getId();
        name = lecture.getName();
        position = lecture.getPosition();

        this.videoDurration = lecture.getVideoDuration();
        this.videoId = StringUtils.getVideoIdFromVimeoUrl(lecture.getVideoUrl());
    }

    public void addExam(Exam exam){
        exams.add(new LectureExamItemDTO(exam));
    }

    public void addQuestions(List<QuestionInVideoDTO> questionsInVideo){
        questionsInVideo.addAll(questionsInVideo);
    }
}
