package com.stadio.mobi.service.impl;

import com.stadio.common.utils.JsonUtils;
import com.stadio.common.utils.ResponseCode;
import com.stadio.mobi.controllers.ExamController;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IHotExamService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.hoc68.users.documents.User;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.dtos.mobility.ExamItemDTO;
import com.stadio.model.enu.ActionBase;
import com.stadio.model.enu.TopType;
import com.stadio.model.redisUtils.HotExamRedisRepository;
import com.stadio.model.repository.main.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.*;

@Service
public class HotExamService extends BaseService implements IHotExamService {

    private Logger logger = LogManager.getLogger(HotExamService.class);

    @Autowired
    ExamRepository examRepository;

    @Autowired
    QuestionInExamRepository questionInExamRepository;

    @Autowired
    ExamHotRepository examHotRepository;

    @Autowired CategoryService categoryService;

    @Autowired
    ExamLikesRepository examLikesRepository;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    UserExamRepository userExamRepository;

    @Autowired
    HotExamRedisRepository hotExamRedisRepository;

    @Override
    public ResponseResult processGetHotExams(String token, String topType)
    {
        List<ExamItemDTO> examItemDTOList = new LinkedList<>();

        Map<String,String> mapExamHotList = hotExamRedisRepository.processGetHotExam(topType);

        if(!mapExamHotList.isEmpty()){
            Map<String, String> examHottreeMap = new TreeMap<>(mapExamHotList);
            List<String> examHotList = new LinkedList<>(examHottreeMap.values());
            examHotList.forEach(examHotStr ->
            {
                ExamItemDTO examItemDTO = JsonUtils.parse(examHotStr, ExamItemDTO.class);
                String detailsUrl = MvcUriComponentsBuilder
                        .fromMethodName(ExamController.class, "getExamDetails", examItemDTO.getId())
                        .host(host).port(port).build().toString();
                examItemDTO.getActions().put(ActionBase.DETAILS, detailsUrl);

                setPriceAndDidDone(examItemDTO, token);

                examItemDTOList.add(examItemDTO);
            });
        }else{
            List<ExamHot> examHotList = examHotRepository.findByTopTypeOrderByPositionAsc(TopType.valueOf(topType));
            Map<ExamHot,Exam> examHotExamMap = new HashMap<>();
            examHotList.forEach(examHot -> {
                Exam exam = examRepository.findOne(examHot.getExamIdRef());
                if(exam != null && exam.isEnable() && !exam.isDeleted()){
                    examHotExamMap.put(examHot,exam);
                    ExamItemDTO examItemDTO = ExamItemDTO.with(exam);

                    String detailsUrl = MvcUriComponentsBuilder
                            .fromMethodName(ExamController.class, "getExamDetails", examItemDTO.getId())
                            .host(host).port(port)
                            .build().toString();
                    examItemDTO.getActions().put(ActionBase.DETAILS, detailsUrl);

                    setPriceAndDidDone(examItemDTO, token);

                    examItemDTOList.add(examItemDTO);
                }else{
                    examHotRepository.delete(examHot.getId());
                }
            });
            hotExamRedisRepository.processPutAllHotExam(examHotExamMap,topType);
        }
        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("home.success.getHotExams"), examItemDTOList);
    }

    private void setPriceAndDidDone(ExamItemDTO examItemDTO, String token) {

        User user = this.getUserRequesting();

        if (user != null) {

            UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(user.getId(), examItemDTO.getId());

            examItemDTO.setDidDone(userExam != null);

            if (userExam != null && examItemDTO.getPrice() > 0) {
                examItemDTO.setPrice(-1);
            }
        }
    }
}
