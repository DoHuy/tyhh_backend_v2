package com.stadio.deeplink.service;

import com.stadio.model.repository.main.ExamRepository;
import com.stadio.model.repository.main.UserExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamService {

    @Autowired
    ExamRepository examRepository;

    @Autowired
    UserExamRepository userExamRepository;
}
