package com.stadio.mobi.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.reflect.TypeToken;
import com.stadio.common.enu.FolderName;
import com.stadio.common.service.IStorageService;
import com.stadio.common.utils.JsonUtils;
import com.stadio.common.utils.ResponseCode;
import com.stadio.mobi.dtos.ExamResultsDTO;
import com.stadio.mobi.dtos.ExamSubmitDTO;
import com.stadio.mobi.dtos.UserPointFormDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IPracticeService;
import com.stadio.mobi.service.IUserPointService;
import com.stadio.model.documents.*;
import com.hoc68.users.documents.User;
import com.stadio.model.dtos.mobility.PracticeListDTO;
import com.stadio.model.dtos.mobility.QuestionResponseDTO;
import com.stadio.model.enu.ActionBase;
import com.stadio.model.enu.PracticeStatus;
import com.stadio.model.enu.PracticeType;
import com.stadio.model.enu.UserPointType;
import com.stadio.model.model.Answer;
import com.stadio.model.model.UserAnswer;
import com.stadio.model.redisUtils.ExamPracticeRedisRepository;
import com.stadio.model.repository.main.ExamRepository;
import com.stadio.model.repository.main.QuestionInExamRepository;
import com.stadio.model.repository.main.UserExamRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class PracticeService extends BaseService implements IPracticeService
{
    private Logger logger = LogManager.getLogger(PracticeService.class);

    @Autowired QuestionInExamRepository questionInExamRepository;

    @Autowired UserExamRepository userExamRepository;

    @Autowired IStorageService storageService;

    @Autowired ExamRepository examRepository;

    @Autowired
    IUserPointService userPointService;

    @Autowired
    ExamPracticeRedisRepository examPracticeRedisRepository;

    @Override
    public ResponseResult processGetPractice()
    {
        List<PracticeListDTO> practiceListDTOList = new ArrayList<>();

        PracticeListDTO practiceListDTO = new PracticeListDTO();
        practiceListDTO.setName("Thi thử THPT quốc gia 2017");
        practiceListDTO.setThumbnailUrl("https://cdn.pixabay.com/photo/2016/07/13/06/38/sunrise-1513732_960_720.jpg");
        practiceListDTO.setActionType(PracticeType.TRY_EXAM);
        practiceListDTO.getActions().put(ActionBase.DETAILS, null);
        practiceListDTOList.add(practiceListDTO);

        practiceListDTO = new PracticeListDTO();
        practiceListDTO.setName("Luyện theo chuyên đề");
        practiceListDTO.setActionType(PracticeType.PRACTICE);
        practiceListDTO.setThumbnailUrl("https://cdn.pixabay.com/photo/2016/07/13/06/38/sunrise-1513732_960_720.jpg");
        practiceListDTO.getActions().put(ActionBase.DETAILS, null);
        practiceListDTOList.add(practiceListDTO);

        practiceListDTO = new PracticeListDTO();
        practiceListDTO.setName("Luyện nhanh");
        practiceListDTO.setThumbnailUrl("https://cdn.pixabay.com/photo/2016/07/13/06/38/sunrise-1513732_960_720.jpg");
        practiceListDTO.setActionType(PracticeType.FAST_PRACTICE);
        practiceListDTO.getActions().put(ActionBase.DETAILS, null);
        practiceListDTOList.add(practiceListDTO);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("home.success.getPractice"), practiceListDTOList);
    }


    @Async
    public void updateUserPoint(String userId, Double right, Double total, String sourceId){

        UserPointFormDTO userPointFormDTO = new UserPointFormDTO();

        userPointFormDTO.setUserId(userId);

        userPointFormDTO.setPoint(right / total);

        userPointFormDTO.setSourceId(sourceId);

        Exam exam = examRepository.findOne(sourceId);

        userPointFormDTO.setSourceName(exam.getName());

        userPointFormDTO.setSourceType(UserPointType.EXAM_OFFLINE);

        userPointService.createUserPoint(userPointFormDTO);

        return;
    }

    @Override
    public ResponseResult processSubmitResults(String token, ExamSubmitDTO examSubmitDTO, String examId)
    {
        //logger.info("User submit: \n" + JsonUtils.pretty(examSubmitDTO));
        User user = this.getUserRequesting();
        ResponseResult response = new ResponseResult();
        try
        {
            List<UserAnswer> answers = examSubmitDTO.getAnswers();

            String key = examId;

            Map<String, String> items = examPracticeRedisRepository.processGetExamPractice(key);

            Map<String, Object> results = !items.isEmpty() ? compareFromRedis(answers, items) : compareFromMongo(answers, examId);

            response.setErrorCode("00");
            response.setData(results);

            //UserExam userExam = new UserExam();
            UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(user.getId(), examId);
            if (userExam != null && userExam.getStatus().equals(PracticeStatus.PROCESS))
            {
                userExam.setUserIdRef(user.getId());
                userExam.setExamIdRef(examId);
                userExam.setEndTime(new Date());
                userExam.setCorrectNumber(Integer.parseInt(results.get("correct_number").toString()));

                try {
                    Integer total = Integer.parseInt( results.get("total_question").toString() );
                    userExam.setTotal(total);
                    updateUserPoint(user.getId(), new Double(userExam.getCorrectNumber()), new Double(total), examId);
                } catch (Exception e) {
                    logger.info("Parse total question: ", e);
                }

                userExam.setDuration(examSubmitDTO.getDuration());
                userExam.setStatus(PracticeStatus.SUBMIT);

                Path p = storageService.getLocation(FolderName.USERS)
                        .resolve("save_by_feature")
                        .resolve("submit_exam")
                        .resolve(user.getId().substring(user.getId().length()-2))
                        .resolve(user.getId().substring(user.getId().length()-4,user.getId().length()-2))
                        .resolve(user.getId().substring(user.getId().length()-6,user.getId().length()-4))
                        .resolve(user.getId());
                if (!p.toFile().exists())
                {
                    Files.createDirectories(p);
                }
                String fname = examId + "_" + System.currentTimeMillis();
                File f = p.resolve(fname).toFile();
                handlerFileStorage(f, results);
                userExam.setDetails(f.getPath());
                logger.info("User submit to: " + f.getPath());
                userExamRepository.save(userExam);

                updateExamSubmitCount(examId, (double)results.get("avg"));

                response.setErrorCode(ResponseCode.SUCCESS);

                List<QuestionInExam> questionInExamList = questionInExamRepository.getQuestionOfExam(examId);
                List<Question> questionList = new ArrayList<>();
                for (QuestionInExam q : questionInExamList)
                {
                    questionList.add(q.getQuestion());
                }

            }

        }
        catch (Exception e)
        {
            response.setErrorCode("01");
            response.setMessage(this.getMessage("practice.cannot.submit"));
        }
        return response;
    }

    private void handlerFileStorage(File f, Object results) {
        try {

            String json = mapper.writeValueAsString(results);
            ExamResultsDTO examResultsDTO = mapper.readValue(json, ExamResultsDTO.class);
            for (QuestionResponseDTO questionResponseDTO: examResultsDTO.getResults()) {
                questionResponseDTO.setExplain("");
                questionResponseDTO.setContent("");
                for(Answer answer: questionResponseDTO.getAnswers()) {
                    answer.setContent("");
                }
            }
            mapper.writeValue(f, examResultsDTO);
        } catch (IOException e) {
            logger.error("handlerFileStorage: " + e);
        }
    }

    @Async
    public void updateExamSubmitCount(String examId, double avg)
    {
        Exam exam = examRepository.findOne(examId);
        if (exam != null)
        {
            int oldSubmitCount = exam.getSubmitCount();
            exam.setSubmitCount(oldSubmitCount + 1);
            double average = (oldSubmitCount * exam.getAverage() + avg) / exam.getSubmitCount();
            exam.setAverage(average);
            examRepository.save(exam);
        }
    }

    @Override
    public ResponseResult processGetAnswers(String token, String examId)
    {
        User user = this.getUserRequesting();
        UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(user.getId(), examId);
        if (userExam == null)
        {
            return ResponseResult.newErrorInstance("01", this.getMessage("practice.invalid.answers"));
        }
        else
        {
            List<QuestionInExam> questionInExamList = questionInExamRepository.getQuestionOfExam(examId);
            List<Question> questionList = new ArrayList<>();
            for (QuestionInExam q : questionInExamList)
            {
                questionList.add(q.getQuestion());
            }
            return ResponseResult.newSuccessInstance(questionList);
        }

    }

    private Map<String, Object> compareFromRedis(List<UserAnswer> userAnswers, Map<String, String> items)
    {
        Map<String, Object> results = new HashMap<>();

        int correctNumber = 0;

        List<QuestionResponseDTO> questionResponseDTOList = new ArrayList<>();
        for (String hashKey : items.keySet())
        {
            try
            {
                QuestionResponseDTO questionResponse = new QuestionResponseDTO();
                Question question = JsonUtils.convertJsonToObject(items.get(hashKey), Question.class);
                String questionId = question.getId();
                questionResponse.setAnswers(question.getAnswers());
                questionResponse.setCorrectAnswer(question.getCorrectAnswer());
                questionResponse.setId(questionId);
                questionResponse.setContent(question.getContent());
                questionResponse.setExplain(question.getExplain());
                for (UserAnswer uk : userAnswers)
                {
                    if (questionId.equals(uk.getQuestionId()))
                    {
                        questionResponse.setChoose(uk.getCode());
                        if (question.getCorrectAnswer().equals(uk.getCode()))
                        {
                            correctNumber++;
                        }
                    }
                }
                questionResponseDTOList.add(questionResponse);
            }
            catch (Exception e)
            {
                logger.error("Exception parsing: ", e);
            }
        }

        int n = items.size() != 0 ? items.size() : 1;
        results.put("correct_number", correctNumber);
        results.put("total_question", items.size());
        results.put("avg", (double)correctNumber/n);
        results.put("results", questionResponseDTOList);
        return results;
    }

    private Map<String, Object> compareFromMongo(List<UserAnswer> userAnswers, String examId)
    {
        Map<String, Object> results = new HashMap<>();

        int correctNumber = 0;
        List<QuestionResponseDTO> questionResponseDTOList = new ArrayList<>();
        List<QuestionInExam> questionInExamList = questionInExamRepository.getQuestionOfExam(examId);
        for (QuestionInExam questionInExam : questionInExamList)
        {
            QuestionResponseDTO questionResponse = new QuestionResponseDTO();
            Question question = questionInExam.getQuestion();
            questionResponse.setAnswers(question.getAnswers());
            questionResponse.setCorrectAnswer(question.getCorrectAnswer());
            questionResponse.setId(question.getId());
            questionResponse.setContent(question.getContent());
            questionResponse.setExplain(question.getExplain());

            for (UserAnswer qk : userAnswers)
            {
                if (qk.getQuestionId() == null)
                {
                    continue;
                }
                if (qk.getQuestionId().equals(questionInExam.getQuestion().getId()))
                {
                    questionResponse.setChoose(qk.getCode());
                    if (qk.getCode().equals(questionInExam.getQuestion().getCorrectAnswer()))
                    {
                        correctNumber++;
                    }
                }
            }

            questionResponseDTOList.add(questionResponse);
        }

        int n = questionInExamList.size() != 0 ? questionInExamList.size() : 1;
        results.put("correct_number", correctNumber);
        results.put("total_question", questionInExamList.size());
        results.put("avg", (double)correctNumber/n);
        results.put("results", questionResponseDTOList);
        return results;
    }
}
