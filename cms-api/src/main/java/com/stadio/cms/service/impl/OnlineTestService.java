package com.stadio.cms.service.impl;

import com.hoc68.users.documents.User;
import com.stadio.cms.RabbitProducer;
import com.stadio.cms.dtos.examOnline.*;
import com.stadio.cms.model.PageInfo;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.response.TableList;
import com.stadio.cms.service.IOnlineTestService;
import com.stadio.common.define.Constant;
import com.stadio.common.utils.MathUtils;
import com.stadio.model.documents.*;
import com.stadio.model.dtos.cms.NotificationQueue;
import com.stadio.model.enu.*;
import com.stadio.model.repository.main.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OnlineTestService extends BaseService implements IOnlineTestService {

    private Logger logger = LogManager.getLogger(OnlineTestService.class);

    private static final String queueName = Constant.QUEUE_NAME.EXAM_ONLINE;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    ExamOnlineRepository examOnlineRepository;

    @Autowired
    ExamSubscribeRepository examSubscribeRepository;

    @Autowired
    RabbitProducer rabbitProducer;

    @Autowired
    UserExamRepository userExamRepository;

    @Autowired
    DeviceRepository deviceRepository;


    private SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private SimpleDateFormat fm2 = new SimpleDateFormat("HH:mm dd/MM/yyyy");

    @Override
    public ResponseResult processCreateOrUpdateExamOnlineTest(ExamOnlineTestForm examOnlineTestForm) {
        boolean isUpdated = false;

        Exam exam = examRepository.findExamByCode(examOnlineTestForm.getExamCode());
        if (exam == null) {
            return ResponseResult.newErrorInstance("01", getMessage("exam.online.null.exam"));
        }

        if (exam.isEnable() || exam.isDeleted()) {
            return ResponseResult.newErrorInstance("01", getMessage("exam.online.invalid.exam"));
        }

        ExamOnline exist = examOnlineRepository.findByExamId(exam.getId());
        if (exist != null) {
            return ResponseResult.newErrorInstance("02", getMessage("exam.online.found.exam"));
        }

        try {
            Date startTime = new Date(fm.parse(examOnlineTestForm.getStartTime()).getTime() + 7*3600*1000);
            Date endTime = new Date(startTime.getTime() + (exam.getTime() * 60 * 1000));

            Date currentDate = new Date();

            if (startTime.before(currentDate)) {
                return ResponseResult.newErrorInstance("01", getMessage("exam.online.before.current"));
            }

            ExamOnline examOnline = new ExamOnline();
            if (examOnlineTestForm.getId() != null) {
                examOnline = examOnlineRepository.findOne(examOnlineTestForm.getId());
            }

            examOnline.setStartTime(startTime);
            examOnline.setEndTime(endTime);
            if (null != examOnlineTestForm.getMaximum()) {
                examOnline.setMaximum(examOnlineTestForm.getMaximum());
            }

            examOnline.setPrice(examOnlineTestForm.getPrice());
            examOnline.setExamId(exam.getId());
            examOnline.setSubmitCount(0);
            examOnline.setAverage(0d);
            if (StringUtils.isNotBlank(examOnlineTestForm.getDescription())) {
                examOnline.setDescription(examOnlineTestForm.getDescription());
            }

            if (examOnline.getStatus() == null) {
                examOnline.setStatus(OnlineTestStatus.PENDING);
            }

            if (org.apache.commons.lang3.StringUtils.isNotBlank(examOnline.getId())) {
                isUpdated = true;
            }

            examOnlineRepository.save(examOnline);

            logger.info("Saved: " + examOnline.getId());

            if (isUpdated) {
                rabbitProducer.sendTrackingEvent(queueName, ActionEvent.EXAM_ONLINE_IS_UPDATED, examOnline.getId());
            } else {
                rabbitProducer.sendTrackingEvent(queueName, ActionEvent.EXAM_ONLINE_IS_CREATED, examOnline.getId());
            }

            return ResponseResult.newSuccessInstance(examOnline);

        } catch (Exception e) {
            logger.error("Process exception: ", e);
        }

        return ResponseResult.newErrorInstance("01", getMessage("something.went.wrong"));
    }

    @Override
    public ResponseResult processCancelExamOnlineTest(String id) {

        ExamOnline examOnline = examOnlineRepository.findOne(id);

        if (examOnline != null) {
            examOnline.setStatus(OnlineTestStatus.CANCELLED);
            examOnlineRepository.save(examOnline);

            rabbitProducer.sendTrackingEvent(queueName, ActionEvent.EXAM_ONLINE_IS_CANCELLED, examOnline.getId());

            return ResponseResult.newSuccessInstance(null);
        } else {
            return ResponseResult.newErrorInstance("01", getMessage("exam.online.null.exam"));
        }

    }

    @Override
    public ResponseResult processGetListExamOnline(int page, int limit) {

        PageRequest request = new PageRequest(page - 1, limit, new Sort(Sort.Direction.DESC, "created_date"));
        List<ExamOnline> examOnlineList = examOnlineRepository.findByStatusNot(OnlineTestStatus.CANCELLED, request).getContent();
        List<ExamOnlineItemDTO> examOnlineItemDTOList = new ArrayList<>();

        for (ExamOnline examOnline: examOnlineList) {
            Exam exam = examRepository.findOne(examOnline.getExamId());
            ExamOnlineItemDTO examOnlineItemDTO = ExamOnlineItemDTO.with(examOnline, exam);

            int count = examSubscribeRepository.countByExamId(examOnline.getId());
            examOnlineItemDTO.setJoin(count);

            examOnlineItemDTOList.add(examOnlineItemDTO);
        }

        return ResponseResult.newSuccessInstance(examOnlineItemDTOList);
    }

    @Override
    public ResponseResult processGetShortInformation(String id) {
        ExamOnline examOnline = examOnlineRepository.findOne(id);
        if (examOnline != null) {
            ExamOnlineTestForm examOnlineTestForm = new ExamOnlineTestForm();
            examOnlineTestForm.setId(id);
            examOnlineTestForm.setMaximum(examOnline.getMaximum());
            examOnlineTestForm.setDescription(examOnline.getDescription());

            Exam exam = examRepository.findOne(examOnline.getExamId());
            if (exam != null) {
                examOnlineTestForm.setExamCode(exam.getCode());
            }

            examOnlineTestForm.setPrice(examOnline.getPrice());
            long startTime = examOnline.getStartTime().getTime() - 7*3600*1000;
            //long endTime = examOnline.getEndTime().getTime() - 7*3600*1000;

            examOnlineTestForm.setStartTime(fm.format(new Date(startTime)));
            //examOnlineTestForm.setEndTime(fm.format(new Date(endTime)));

            return ResponseResult.newSuccessInstance(examOnlineTestForm);
        }

        return ResponseResult.newErrorInstance("401", getMessage("exam.online.null.exam"));
    }

    @Override
    public ResponseResult processOpeningRegister(String id) {

        ExamOnline examOnline = examOnlineRepository.findOne(id);

        if (examOnline != null) {
            examOnline.setStatus(OnlineTestStatus.OPENING);
            examOnline.setUpdatedDate(new Date());
            examOnlineRepository.save(examOnline);
        }

        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult processDetailsExamOnlineTest(String id) {

        ExamOnline examOnline = examOnlineRepository.findOne(id);

        if (examOnline == null) {
            return ResponseResult.newErrorInstance("401", getMessage("exam.online.null.exam"));
        }

        Exam exam = examRepository.findOne(examOnline.getExamId());
        if (exam == null) {
            return ResponseResult.newErrorInstance("401", getMessage("exam.online.null.exam"));
        }

        //check online status
        Date currentDate = new Date();
        if (currentDate.after(examOnline.getStartTime()) && currentDate.before(examOnline.getEndTime())) {
            examOnline.setStatus(OnlineTestStatus.ONGOING);
        } else if (currentDate.after(examOnline.getEndTime())) {
            examOnline.setStatus(OnlineTestStatus.FINISHED);
        }

        examOnlineRepository.save(examOnline);

        ExamOnlineDetailsDTO examOnlineDetailsDTO = ExamOnlineDetailsDTO.with(examOnline, exam);
        int count = examSubscribeRepository.countByExamId(examOnline.getExamId());
        examOnlineDetailsDTO.setJoin(count);

        return ResponseResult.newSuccessInstance(examOnlineDetailsDTO);
    }

    @Override
    public ResponseResult processGetListJoinerExamOnline(String id, int page, int limit) {


        PageRequest request = new PageRequest(page - 1, limit, new Sort(Sort.Direction.DESC, "created_date"));
        Page<ExamSubscribe> examSubscribePage = examSubscribeRepository.findByExamId(id, request);
        List<ExamSubscribe> examSubscribeList = examSubscribePage.getContent();
        PageInfo pageInfo = new PageInfo(page, examSubscribePage.getTotalElements(), limit, "");

        List<JoinerItemDTO> joinerItemDTOList = new ArrayList<>();
        for (ExamSubscribe subscribe: examSubscribeList) {

            User user = userRepository.findOne(subscribe.getUserId());

            JoinerItemDTO joinerItemDTO = new JoinerItemDTO();
            joinerItemDTO.setId(subscribe.getId());
            joinerItemDTO.setReceiveMessage(subscribe.isReceiveMessage());
            joinerItemDTO.setReceiveEmail(subscribe.isReceiveEmail());
            joinerItemDTO.setJoinTime(fm.format(subscribe.getJoinTime()));

            if (user != null) {
                joinerItemDTO.setUserCode(user.getCode());
                joinerItemDTO.setUsername(user.getUsername());
            }
            joinerItemDTOList.add(joinerItemDTO);
        }

        TableList tableList = new TableList(pageInfo, joinerItemDTOList);

        return ResponseResult.newSuccessInstance(tableList);
    }

    @Override
    public ResponseResult processGetTablePoint(String id, int page, int limit) {

        ExamOnline examOnline = examOnlineRepository.findOne(id);
        if (examOnline == null) {
            return ResponseResult.newErrorInstance("404", getMessage("exam.online.not.found"));
        }

        PageRequest request = new PageRequest(page - 1, limit);
        Page<UserExam> userExamPage = userExamRepository.findByExamIdRefAndStatusOrderByCorrectNumberDescDurationAsc(examOnline.getExamId(), PracticeStatus.SUBMIT, request);
        PageInfo pageInfo = new PageInfo(page, userExamPage.getTotalElements(), limit, "");

        List<TablePointDTO> tablePointDTOList = new ArrayList<>();
        List<UserExam> userExamList = userExamPage.getContent();

        int position = 1;
        for (UserExam userExam: userExamList) {
            User user = userRepository.findOne(userExam.getUserIdRef());
            if (user == null) {
                continue;
            }

            TablePointDTO tablePointDTO = new TablePointDTO();
            tablePointDTO.setId(user.getId());
            tablePointDTO.setAvatar(user.getAvatar());
            tablePointDTO.setFullname(user.getFullName());
            tablePointDTO.setUsername(user.getUsername());
            tablePointDTO.setPosition(position);
            tablePointDTO.setDuration(userExam.getDuration());

            double point = (userExam.getCorrectNumber() * 10.0 / userExam.getTotal());
            tablePointDTO.setPoint(MathUtils.round(point));
            tablePointDTOList.add(tablePointDTO);

            position++;
        }

        TableList tableList = new TableList(pageInfo, tablePointDTOList);

        return ResponseResult.newSuccessInstance(tableList);
    }

    @Override
    public ResponseResult processPushMessageRemind(String id) {
        ExamOnline examOnline = examOnlineRepository.findOne(id);
        if (examOnline != null) {
            Date currentDate = new Date();
            if (currentDate.before(examOnline.getStartTime())) {
                rabbitProducer.sendTrackingEvent(queueName, ActionEvent.EXAM_ONLINE_PUSH_REMIND, examOnline.getId());
                return ResponseResult.newSuccessInstance(null);
            } else {
                return ResponseResult.newErrorInstance("01", getMessage("exam.online.message.remind"));
            }
        } else {
            return ResponseResult.newErrorInstance("404", getMessage("exam.online.not.found"));
        }

    }

    @Override
    public ResponseResult processPushMessageResults(String id) {
        ExamOnline examOnline = examOnlineRepository.findOne(id);
        if (examOnline != null) {
            Date currentDate = new Date();
            if (currentDate.after(examOnline.getEndTime())) {
                rabbitProducer.sendTrackingEvent(queueName, ActionEvent.EXAM_ONLINE_PUSH_RESULT, examOnline.getId());
                return ResponseResult.newSuccessInstance(null);
            } else {
                return ResponseResult.newErrorInstance("01", getMessage("exam.online.message.results"));
            }
        } else {
            return ResponseResult.newErrorInstance("404", getMessage("exam.online.not.found"));
        }
    }


}
