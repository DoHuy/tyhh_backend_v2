package com.stadio.cms.service.impl;

import com.stadio.cms.model.PageInfo;
import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.response.TableList;
import com.stadio.cms.service.IManagerService;
import com.stadio.cms.service.IQuestionService;
import com.stadio.cms.validation.QuestionValidation;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.*;import com.hoc68.users.documents.Manager;
import com.stadio.model.dtos.cms.*;
import com.stadio.model.enu.QuestionLevel;
import com.stadio.model.enu.QuestionType;
import com.stadio.model.model.Answer;
import com.stadio.model.repository.main.ExamRepository;
import com.stadio.model.repository.main.QuestionInExamRepository;
import com.stadio.model.repository.main.QuestionRepository;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by sm on 12/14/17.
 */
@Service
public class QuestionService extends BaseService implements IQuestionService
{

    @Autowired QuestionInExamRepository questionInExamRepository;

    @Autowired QuestionRepository questionRepository;

    @Autowired IManagerService managerService;

    @Autowired QuestionValidation validation;

    @Autowired ExamRepository examRepository;

    @Override
    public ResponseResult processCreateOneQuestion(QuestionFormDTO questionFormDTO, String token)
    {
        ResponseResult responseResult = validation.inValidQuestionForm(questionFormDTO);
        if (responseResult != null)
        {
            return responseResult;
        }

        Question question = new Question();
        question.setContent(questionFormDTO.getContent());
        question.setAnswers(mapAnswerDTOtoAnswer(questionFormDTO.getAnswers()));
        question.setType(EnumUtils.getEnum(QuestionType.class, questionFormDTO.getType()));
        question.setClazzId(questionFormDTO.getClazzId());
        question.setLevel(EnumUtils.getEnum(QuestionLevel.class, questionFormDTO.getLevel()));
        question.setExplain(questionFormDTO.getExplain());
        question.setChapterId(question.getChapterId());

        Manager current = managerService.getManagerRequesting();

        question.setCreatedBy(current.getId());
        question.setUpdatedBy(current.getId());

        questionRepository.save(question);
        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("question.success.create"), new QuestionDetailDTO(question));
    }

    @Override
    public ResponseResult processCreateOneQuestion(Exam exam, Integer nth, QuestionFormDTO questionFormDTO, String token)
    {
        ResponseResult responseResult = validation.inValidQuestionForm(questionFormDTO);
        if (responseResult != null)
        {
            return responseResult;
        }

        Question question = new Question();
        question.setContent(questionFormDTO.getContent());
        //question.setAnswers(questionFormDTO.getAnswers());
        question.setType(EnumUtils.getEnum(QuestionType.class, questionFormDTO.getType()));
        question.setLevel(EnumUtils.getEnum(QuestionLevel.class, questionFormDTO.getLevel()));
        question.setAnswers(mapAnswerDTOtoAnswer(questionFormDTO.getAnswers()));
        question.setClazzId(questionFormDTO.getClazzId());
        question.setExplain(questionFormDTO.getExplain());
        question.setChapterId(questionFormDTO.getChapterId());

        Manager current = managerService.getManagerRequesting();

        question.setCreatedBy(current.getId());
        question.setUpdatedBy(current.getId());

        Question questionsave = questionRepository.save(question);

        QuestionInExam questionInExam = new QuestionInExam();
        questionInExam.setExam(exam);
        questionInExam.setQuestion(questionsave);
        questionInExam.setPosition(nth);

        questionInExamRepository.save(questionInExam);


        //update quantiy for exam
        Long quantity = questionInExamRepository.getQuestionQuantityOfExam(exam.getId());
        exam.setQuestionQuantity(quantity.intValue());
        examRepository.save(exam);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("question.success.create"), new QuestionDetailDTO(question));
    }

    @Override
    public ResponseResult processUpdateOneQuestion(QuestionFormDTO questionFormDTO, String token)
    {
        ResponseResult responseResult = validation.inValidQuestionForm(questionFormDTO);
        if (responseResult != null)
        {
            return responseResult;
        }
        if (!StringUtils.isNotNull(questionFormDTO.getId()))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("question.invalid.id"), null);
        }

        Question question = questionRepository.findOne(questionFormDTO.getId());

        if (question == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("question.invalid.question"), null);
        }

        question.setContent(questionFormDTO.getContent());
        question.setAnswers(mapAnswerDTOtoAnswer(questionFormDTO.getAnswers()));
        question.setClazzId(questionFormDTO.getClazzId());
        question.setType(EnumUtils.getEnum(QuestionType.class, questionFormDTO.getType()));
        question.setLevel(EnumUtils.getEnum(QuestionLevel.class, questionFormDTO.getLevel()));
        question.setExplain(questionFormDTO.getExplain());
        question.setChapterId(questionFormDTO.getChapterId());

        Manager manager = managerService.getManagerRequesting();

        question.setUpdatedBy(manager.getId());
        question.setUpdatedDate(new Date());

        questionRepository.save(question);
        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("question.success.update"), new QuestionDetailDTO(question));
    }

    @Override
    public ResponseResult processUpdateOneQuestion(Exam exam, Integer nth, QuestionFormDTO questionFormDTO, String token) {
        ResponseResult responseResult = validation.inValidQuestionForm(questionFormDTO);
        if (responseResult != null)
        {
            return responseResult;
        }
        if (!StringUtils.isNotNull(questionFormDTO.getId()))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("question.invalid.id"), null);
        }

        Question question = questionRepository.findOne(questionFormDTO.getId());

        if (question == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("question.invalid.question"), null);
        }

        question.setContent(questionFormDTO.getContent());
        question.setAnswers(mapAnswerDTOtoAnswer(questionFormDTO.getAnswers()));
        question.setClazzId(questionFormDTO.getClazzId());
        question.setType(EnumUtils.getEnum(QuestionType.class, questionFormDTO.getType()));
        question.setLevel(EnumUtils.getEnum(QuestionLevel.class, questionFormDTO.getLevel()));
        question.setExplain(questionFormDTO.getExplain());
        question.setChapterId(questionFormDTO.getChapterId());

        Manager manager = managerService.getManagerRequesting();

        question.setUpdatedBy(manager.getId());
        question.setUpdatedDate(new Date());

        Question questionsave = questionRepository.save(question);

        QuestionInExam questionInExam = questionInExamRepository.findFirstByExamAndPosition(exam,nth);
        if(questionInExam==null)
            questionInExam = new QuestionInExam();
        questionInExam.setExam(exam);
        questionInExam.setQuestion(questionsave);
        questionInExam.setPosition(nth);
        questionInExamRepository.save(questionInExam);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("question.success.update"), new QuestionDetailDTO(question));
    }

    @Override
    public ResponseResult processSearchQuestion(QuestionSearchFormDTO questionSearchFormDTO, Integer page, Integer pageSize, String uri)
    {
        Map questionSearch = new HashMap<String, Object>();
        questionSearch.put("content", questionSearchFormDTO.getContent());
        questionSearch.put("chapterId", questionSearchFormDTO.getChapterId());
        questionSearch.put("clazzId", questionSearchFormDTO.getClazzId());
        questionSearch.put("type", questionSearchFormDTO.getType());
        questionSearch.put("level", questionSearchFormDTO.getLevel());
        long questionQuanity = questionRepository.searchQuestionQuanity(questionSearch);
        List<Question> questions = questionRepository.findQuestionByPage(page, pageSize, questionSearch);
        List<QuestionDetailDTO> questionDetailsDTOS = new ArrayList<>();

        PageInfo pageInfo = new PageInfo(page, questionQuanity, pageSize, uri);

        if (questions != null)
        {
            questions.forEach(question ->
            {
                questionDetailsDTOS.add(new QuestionDetailDTO(question));
            });
        }

        TableList<QuestionDetailDTO> tableList = new TableList<>(pageInfo, questionDetailsDTOS);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("question.success.getList"), tableList);
    }

    @Override
    public ResponseResult processGetQuestionById(String id)
    {
        Question question = questionRepository.findOne(id);
        QuestionListDTO questionListDTO = null;
        if (question != null)
        {
            questionListDTO = new QuestionListDTO(question);
            return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("question.success.getById"), questionListDTO);
        }
        else
        {
            return ResponseResult.newInstance(ResponseCode.FILE_NOT_EXIST, getMessage("question.not.exist"), null);
        }

    }

    @Override
    public ResponseResult processDeleteOneQuestion(String id)
    {
        Question question = questionRepository.findOne(id);
        if (question == null)
        {
            return ResponseResult.newInstance(ResponseCode.FILE_NOT_EXIST, getMessage("question.not.exist"), null);
        }
        boolean use = questionInExamRepository.checkQuestionUse(question);
        if (use)
        {
            return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("question.delelte.use"), null);
        }
        questionRepository.delete(id);
        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("question.success.delete"), null);
    }
    
    

    public List<Answer> mapAnswerDTOtoAnswer(List<AnswerDTO> answerDTOS)
    {
        List<Answer> answers = new ArrayList<>();
        answerDTOS.forEach(answerDTO ->
        {
            Answer answer = new Answer();
            answer.setCode(answerDTO.getCode());
            answer.setContent(answerDTO.getContent());
            answer.setCorrect(answerDTO.getIsCorrect() == 1);
            answers.add(answer);
        });
        return answers;
    }

}
