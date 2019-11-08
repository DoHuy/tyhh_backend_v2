package com.stadio.deeplink.controllers;

import com.hoc68.users.documents.User;
import com.stadio.deeplink.service.ExamService;
import com.stadio.model.documents.Exam;
import com.stadio.model.documents.Question;
import com.stadio.model.documents.QuestionInExam;
import com.stadio.model.documents.UserExam;
import com.stadio.model.dtos.mobility.QuestionResponseDTO;
import com.stadio.model.model.Answer;
import com.stadio.model.repository.main.ExamRepository;
import com.stadio.model.repository.main.QuestionInExamRepository;
import com.stadio.model.repository.main.QuestionRepository;
import com.stadio.model.repository.main.UserExamRepository;
import com.stadio.model.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class DeepLinkController {

    @Autowired
    UserExamRepository userExamRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    QuestionInExamRepository questionInExamRepository;

    @Autowired
    QuestionRepository questionRepository;


    @GetMapping
    public void home(HttpServletResponse response) {
        response.setHeader("Location", "http://toiyeuhoahoc.net");
        response.setStatus(307);
    }

    @GetMapping(value = "thong-tin-de-thi")
    public void examInfo(HttpServletResponse response) {
        response.setHeader("Location", "http://toiyeuhoahoc.net");
        response.setStatus(307);
    }

    @GetMapping(value = "ket-qua-lam-bai")
    public String examResult(HttpServletResponse response,
                             Map<String, Object> model,
                           @RequestParam(value = "id", required = false, defaultValue = "") String examId,
                           @RequestParam(value = "userId", required = false, defaultValue = "") String userId,
                             @RequestParam(value = "ref", required = false, defaultValue = "") String ref) {
        model.put("examId", examId);
        model.put("ref", ref);
        User user = userRepository.findOne(userId);
        if (user != null) {
            model.put("avatar", user.getAvatar());
            model.put("fullname", user.getFullName());
        }

        Exam exam = examRepository.findOne(examId);
        if (exam != null) {
            model.put("examName", exam.getName());
        }

        List<QuestionInExam> questionInExamList = questionInExamRepository.getQuestionOfExam(examId);
        List<Question> questionList = new ArrayList<>();

        for (int i = 0; i <= questionInExamList.size(); i++) {
            Question question = questionInExamList.get(i).getQuestion();
            String questionContent = question.getContent().replace("font-size:18px", "font-size:36px");
            question.setContent(questionContent);

            for (Answer answer: question.getAnswers()) {
                String content = answer.getContent().replaceFirst("<p>", "");
                content = content.replace("font-size:18px", "font-size:36px");
                answer.setContent(content);
            }
            questionList.add(questionInExamList.get(i).getQuestion());
            if (i >= 5) break;
        }
        model.put("questionList", questionList);

        UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(userId, examId);
        if (userExam != null) {
            model.put("correctNumber", userExam.getCorrectNumber());
            model.put("total", userExam.getTotal());
            long time = userExam.getDuration();
            String minute = String.format("%02d", (int)time/60);
            String second = String.format("%02d", (int)time%60);
            model.put("time", minute + ":" + second);
        }

        return "exam_results";
    }

    @GetMapping(value = "bang-tinh-tan")
    public void solubilityTable(HttpServletResponse response) {
        response.setHeader("Location", "http://toiyeuhoahoc.net");
        response.setStatus(307);
    }

    @GetMapping(value = "bang-tuan-hoan")
    public void periodicTable(HttpServletResponse response) {
        response.setHeader("Location", "http://toiyeuhoahoc.net");
        response.setStatus(307);
    }

    @GetMapping(value = "bang-xep-hang")
    public void rankingTable(HttpServletResponse response) {
        response.setHeader("Location", "http://toiyeuhoahoc.net");
        response.setStatus(307);
    }

    @GetMapping(value = "ket-qua-thi-online")
    public void examOnlineResult(HttpServletResponse response) {
        response.setHeader("Location", "http://toiyeuhoahoc.net");
        response.setStatus(307);
    }

    @GetMapping(value = "thi-online")
    public void examOnlineWaitingRoom(HttpServletResponse response) {
        response.setHeader("Location", "http://toiyeuhoahoc.net");
        response.setStatus(307);
    }

    @GetMapping(value = "chuyen-de")
    public void category(HttpServletResponse response) {
        response.setHeader("Location", "http://toiyeuhoahoc.net");
        response.setStatus(307);
    }

    @GetMapping(value = "thanh-tich")
    public void achievement(HttpServletResponse response) {
        response.setHeader("Location", "http://toiyeuhoahoc.net");
        response.setStatus(307);
    }

    @GetMapping(value = "khoa-hoc")
    public void courseInfo(HttpServletResponse response) {
        response.setHeader("Location", "http://toiyeuhoahoc.net");
        response.setStatus(307);
    }

    @GetMapping(value = "ly-thuyet")
    public void theoryTable(HttpServletResponse response) {
        response.setHeader("Location", "http://toiyeuhoahoc.net");
        response.setStatus(307);
    }


}
