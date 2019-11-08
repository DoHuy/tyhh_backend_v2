package com.stadio.mobi.service.impl;

import com.stadio.common.utils.ResponseCode;
import com.stadio.mobi.controllers.ExamController;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IQuestionService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.dtos.mobility.QuestionItemDTO;
import com.stadio.model.dtos.mobility.UserPracticeDTO;
import com.stadio.model.enu.ActionBase;
import com.stadio.model.enu.PracticeStatus;
import com.stadio.model.enu.TransactionType;
import com.stadio.model.redisUtils.ExamPracticeRedisRepository;
import com.stadio.model.repository.main.ExamRepository;
import com.stadio.model.repository.main.QuestionInExamRepository;
import com.stadio.model.repository.main.UserExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.*;

/**
 * Created by sm on 12/14/17.
 */
@Service
public class QuestionService extends BaseService implements IQuestionService
{

    @Autowired QuestionInExamRepository questionInExamRepository;

    @Autowired UserExamRepository userExamRepository;

    @Autowired ExamRepository examRepository;

    @Autowired UserService userService;

    @Autowired TransactionService transactionService;

    @Autowired
    ExamPracticeRedisRepository examPracticeRedisRepository;

    @Override
    public ResponseResult processGetQuestionForPractice(String token, String examId)
    {
        User user = getUserRequesting();

        Exam exam = examRepository.findOne(examId);
        if (exam == null)
        {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, this.getMessage("exam.not.exist"));
        }

//        String key = user.getUsername() + ":" +examId;
        String key = examId;
        Map<String, String> mapResults = examPracticeRedisRepository.processGetExamPractice(key);

        UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(user.getId(), examId);

        if (userExam == null)
        {
            //chua thi lan nao
            userExam = new UserExam();
            userExam.setStartTime(new Date());
            userExam.setUserIdRef(user.getId());
            userExam.setStatus(PracticeStatus.PROCESS);
            userExam.setExamIdRef(examId);
            userExamRepository.save(userExam);

            //sau do la kiem tra tien
            if (user.getBalance() < exam.getPrice())
            {
                return ResponseResult.newErrorInstance(ResponseCode.BALANCE, this.getMessage("balance.not.enough"));
            }
            else
            {
                if (exam.getPrice() > 0) {
                    Transaction transaction = new Transaction();
                    transaction.setObjectId(exam.getId());
                    transaction.setTransContent("Mua đề thi [" + exam.getName() + "] mã đề [" + exam.getCode() + "]");
                    transaction.setTransType(TransactionType.EXAM);
                    transaction.setUserIdRef(user.getId());
                    transactionService.processDeduction(token, exam.getPrice(), transaction);

                }
            }
        }
        //List<QuestionItemDTO> questionDTOList = new ArrayList<>();

        //TODO - tam thoi bo lay question tu Redis di vi dang co loi lay Content cua 1 question.
//        if (!mapResults.isEmpty())
//        {
//            Map<String,String> resultsTreeMap = new TreeMap<>(mapResults);
//            List<String> questionDTOStrList = new LinkedList<>(resultsTreeMap.values());
//            for (int pos = 0; pos < questionDTOStrList.size(); pos++)
//            {
//                Question question = JsonUtils.parse(questionDTOStrList.get(pos), Question.class);
//                questionDTOList.add(QuestionItemDTO.with(question));
//            }
//        }
//        else
//        {
        List<QuestionInExam> questionInExamList = questionInExamRepository.getQuestionOfExam(examId);
        List<QuestionItemDTO> questionDTOList = new ArrayList<>();
        //examPracticeRedisRepository.processPutExamPractice(key,questionInExamList);
//        }

        for (QuestionInExam q: questionInExamList) {
            QuestionItemDTO questionItem = QuestionItemDTO.with(q.getQuestion());
            questionDTOList.add(questionItem);
        }

        UserPracticeDTO userPracticeDTO = new UserPracticeDTO();
        userPracticeDTO.setExamId(examId);

        String submitUrl = MvcUriComponentsBuilder.fromMethodName(ExamController.class, "actionSubmit", null, null, null)
                .path(examId)
                .host(host).port(port)
                .build().toString();

        userPracticeDTO.getActions().put(ActionBase.SUBMIT, submitUrl);
        userPracticeDTO.setQuestionList(questionDTOList);
        return ResponseResult.newSuccessInstance(userPracticeDTO);
    }

}
