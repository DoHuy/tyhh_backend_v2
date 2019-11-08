package com.stadio.mobi.dtos;

import com.stadio.model.dtos.mobility.QuestionResponseDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExamResultsDTO {

    private int correct_number;
    private int total_question;
    private double avg;
    private List<QuestionResponseDTO> results = new ArrayList<>();


}
