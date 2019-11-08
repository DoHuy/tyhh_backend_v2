package com.stadio.cms.service.impl;

import com.stadio.cms.RabbitProducer;
import com.stadio.cms.model.PageInfo;
import com.stadio.common.utils.JsonUtils;
import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.response.TableList;
import com.stadio.cms.service.IExamService;
import com.stadio.cms.service.IManagerService;
import com.stadio.cms.validation.ExamValidation;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Exam;
import com.hoc68.users.documents.Manager;
import com.stadio.model.documents.QuestionInExam;
import com.stadio.model.dtos.cms.*;
import com.stadio.model.enu.ActionEvent;
import com.stadio.model.enu.ExamType;
import com.stadio.model.repository.main.ExamHotRepository;
import com.stadio.model.repository.main.ExamRepository;
import com.stadio.model.repository.main.QuestionInExamRepository;

import org.apache.commons.lang3.EnumUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExamService extends BaseService implements IExamService
{
    private Logger logger = LogManager.getLogger(ExamService.class);

    @Autowired ExamRepository examRepository;

    @Autowired QuestionInExamRepository questionInExamRepository;

    @Autowired IManagerService managerService;

    @Autowired ExamHotRepository examHotRepository;

    @Autowired ExamValidation examValidation;

    @Autowired
    RabbitProducer rabbitProducer;

    @Override
    public ResponseResult processCreateOneExam(ExamFormDTO examFormDTO, String token)
    {
        return this.processUpdateOneExam(examFormDTO, null);
    }

    @Override
    public ResponseResult processUpdateOneExam(ExamFormDTO examFormDTO,String token)
    {

        ResponseResult responseResult = examValidation.inValidExamForm(examFormDTO);

        if (responseResult != null)
        {
            return responseResult;
        }

        Exam exam = new Exam();
        if (StringUtils.isNotNull(examFormDTO.getId()))
        {
            exam = examRepository.findOne(examFormDTO.getId());
        }

        if (exam == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("exam.invalid.id"), null);
        }

        exam.setCode(examFormDTO.getCode());
        exam.setName(examFormDTO.getName());
        exam.setTime(examFormDTO.getTime());
        exam.setPrice(examFormDTO.getPrice());

        exam.setEnable(examFormDTO.getEnable());
        exam.setSummary(examFormDTO.getSummary());
        exam.setKeywords(examFormDTO.getKeywords());
        exam.setImageUrl(examFormDTO.getImageUrl());
        exam.setChapterId(examFormDTO.getChapterId());
        exam.setQuestionMax(examFormDTO.getQuestionMax());
        exam.setClazzId(examFormDTO.getClazzId());
        exam.setHasCorrectionDetail(examFormDTO.getHasCorrectionDetail());

        Manager manager = managerService.getManagerRequesting();

        if (!StringUtils.isNotNull(examFormDTO.getId())) {
            exam.setCreatedBy(manager.getId());
        } else {
            exam.setUpdatedBy(manager.getId());
            exam.setUpdatedDate(new Date());
        }

        exam.setDeleted(false);
        exam.setType(EnumUtils.getEnum(ExamType.class, examFormDTO.getType()));

        Exam examSave = examRepository.save(exam);
        String examStr = JsonUtils.writeValue(examSave);
        rabbitProducer.sendExamManagerEvent(StringUtils.isNotNull(examFormDTO.getId()) ? ActionEvent.EXAM_UPDATE : ActionEvent.EXAM_CREATE,
                examStr);
        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("exam.success.update"), new ExamListDTO(exam));

    }

    @Override
    public ResponseResult processDeleteOneExam(String id,String token)
    {
        if (!StringUtils.isNotNull(id))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("exam.invalid.id"), null);
        }

        Exam exam = examRepository.findOne(id);

        if (exam == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("exam.invalid.id"), null);
        }

        exam.setDeleted(true);

        Manager manager = managerService.getManagerRequesting();

        exam.setUpdatedBy(manager.getId());

        exam.setUpdatedDate(new Date());

        Exam examSave = examRepository.save(exam);
        String examStr = JsonUtils.writeValue(examSave);
        rabbitProducer.sendExamManagerEvent(ActionEvent.EXAME_DELETE,examStr);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("exam.success.delete"), null);
    }

    @Override
    public ResponseResult processGetListExam(String token, Integer page, Integer pageSize, String uri)
    {
        if (page == null || page < 1)
        {
            page = 1;
        }

        if (pageSize == null || pageSize < 1)
        {
            pageSize = 50;
        }

        Manager current = managerService.getManagerRequesting();
        List<Exam> exams;
        if (current.getUserRole() == 2) {
            PageRequest request = new PageRequest(page - 1, pageSize);
            exams = examRepository.findExamByCreatedBy(current.getId(), request).getContent();
        } else {
            exams = examRepository.findExamByPage(page, pageSize);
        }
        //List<Exam> exams = examRepository.findExamByPage(page, pageSize);
        List<ExamListDTO> examListDTOS = new ArrayList<>();
        for (Exam exam : exams)
        {
            examListDTOS.add(new ExamListDTO(exam));
        }

        PageInfo pageInfo = new PageInfo(page, examRepository.count(), pageSize, uri);

        TableList tableList = new TableList(pageInfo, examListDTOS);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("exam.success.getList"), tableList);
    }

    @Override
    public ResponseResult processGetExamDetail(String id)
    {
        if (!StringUtils.isNotNull(id))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("exam.invalid.id"), null);
        }

        Exam exam = examRepository.findOne(id);

        if (exam == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("exam.invalid.id"), null);
        }

        ExamDetailDTO examDetailDTO = new ExamDetailDTO(exam);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("exam.success.getDetail"), examDetailDTO);
    }


    @Override
    public Exam findOne(String id)
    {
        return examRepository.findOne(id);
    }

    @Override
    public Void save(Exam exam)
    {
        examRepository.save(exam);
        return null;
    }

//    @Override
//    public List<ExamListDTO> getListExamRecommends()
//    {
//        List<Exam> exams = examRepository.findAll();
//        List<ExamListDTO> examListDTOS = new ArrayList<>();
//        for (Exam exam : exams)
//        {
//            examListDTOS.add(new ExamListDTO(exam));
//        }
//
//        return examListDTOS;
//    }

    @Override
    public ResponseResult processSearchExam(String token, ExamSearchFormDTO examSearchFormDTO, Integer page, Integer pageSize, String uri) {
        Map examSearch = new HashMap<String,Object>();
        examSearch.put("code",examSearchFormDTO.getCode().trim());
        examSearch.put("name",examSearchFormDTO.getName().trim());
        examSearch.put("clazzId",examSearchFormDTO.getClazzId());
        examSearch.put("type",examSearchFormDTO.getType().trim());
        examSearch.put("fromDate",examSearchFormDTO.getFromDate());
        examSearch.put("toDate",examSearchFormDTO.getToDate());
        if (examSearchFormDTO.getHasCorrectionDetail() != null) {
            examSearch.put("hasCorrectionDetail", examSearchFormDTO.getHasCorrectionDetail());
        }
        long examQuanity = examRepository.searchExamQuantity(examSearch);

        Manager current = managerService.getManagerRequesting();
        if (current.getUserRole() != null && current.getUserRole() == 2) {
            examSearch.put("createdBy", current.getId());
        }
        List<Exam> exams = examRepository.findExamByPage(page, pageSize, examSearch);


        List<ExamListDTO> examListDTOS = new ArrayList<>();

        if(exams!=null)
        {
            for(int pos = 0 ;pos < exams.size() ; pos++)
            {
                ExamListDTO examListDTO = new ExamDetailDTO(exams.get(pos));
                examListDTOS.add(examListDTO);
            }

        }


        PageInfo pageInfo = new PageInfo(page,examQuanity, pageSize, uri);

        TableList<ExamListDTO> tableList = new TableList<>(pageInfo, examListDTOS);
        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("exam.success.getList"), tableList);
    }

    @Override
    public ResponseResult processGetExamDetailForCMS(String id)
    {
        Exam exam = examRepository.findOne(id);
        ExamListDTO examListDTO;
        if (exam != null)
        {
            examListDTO = new ExamListDTO(exam);
            Long quantity = questionInExamRepository.getQuestionQuantityOfExam(exam.getId());
            examListDTO.setQuantity(quantity.intValue());
            return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("exam.success.getById"), examListDTO);
        }
        else
        {
            return ResponseResult.newInstance(ResponseCode.FILE_NOT_EXIST, getMessage("exam.not.exist"), null);
        }

    }

    @Override
    public ResponseResult getQuestionNthOfExam(String id,Integer nth) {

        QuestionInExam questionInExam = questionInExamRepository.getQuestionNthOfExam(id,nth);

        if(questionInExam!=null){
            QuestionListDTO questionListDTO = new QuestionListDTO(questionInExam.getQuestion());
            return ResponseResult.newInstance(ResponseCode.SUCCESS,getMessage("question.success.getByIdExam"),questionListDTO);
        }
        else return ResponseResult.newInstance(ResponseCode.FILE_NOT_EXIST,getMessage("question.not.exist.getByIdExam"),null);

    }

    @Override
    public ResponseResult getQuestionQuantityOfExam(String id) {

        Long quanity = questionInExamRepository.getQuestionQuantityOfExam(id);
        if(quanity==null)
            quanity=new Long(0);
        return ResponseResult.newInstance(ResponseCode.SUCCESS,getMessage("question.quanity.getByIdExam"),quanity);
    }

    @Override
    public ResponseResult getQuestionOfExam(String id) {
        List<QuestionInExam> questionInExams = questionInExamRepository.getQuestionOfExam(id);
        if(questionInExams!=null){
            List<QuestionDetailDTO> questionDetailDTOS = new ArrayList<>();
            questionInExams.forEach(questionInExam -> {
                QuestionDetailDTO questionDetailDTO =  new QuestionDetailDTO(questionInExam.getQuestion());
                questionDetailDTO.setPosition(questionInExam.getPosition());
                questionDetailDTOS.add(questionDetailDTO);
            });
            return ResponseResult.newInstance(ResponseCode.SUCCESS,getMessage("question.success.getByIdExam"),questionDetailDTOS);
        }else
            return ResponseResult.newInstance(ResponseCode.FILE_NOT_EXIST,getMessage("question.not.exist.getByIdExam"),null);
    }

//    public List<ExamListDTO> getListExamHot()
//    {
//        List<ExamHot> examHotList = examHotRepository.findAll(new Sort(Sort.Direction.ASC, "position"));
//        List<ExamListDTO> examList = new ArrayList<>();
//
//        if (examHotList != null && !examHotList.isEmpty())
//        {
//            examHotList.forEach(examHot ->
//            {
//                Exam exam = this.findOne(examHot.getExamIdRef());
//                if (exam != null)
//                {
//                    ExamListDTO examListDTO = new ExamListDTO(exam, examHot.getPosition());
//                    examList.add(examListDTO);
//                }
//            });
//        }
//
//        return examList;
//    }

}


