package com.stadio.mobi.service.impl;

import com.hoc68.users.documents.User;
import com.stadio.common.enu.FolderName;
import com.stadio.common.service.impl.StorageService;
import com.stadio.common.utils.MathUtils;
import com.stadio.common.utils.ResponseCode;
import com.stadio.mobi.controllers.ExamOnlineController;
import com.stadio.mobi.dtos.UserPointFormDTO;
import com.stadio.mobi.dtos.examOnline.*;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IOnlineTestService;
import com.stadio.mobi.service.IUserPointService;
import com.stadio.model.documents.*;
import com.stadio.model.dtos.mobility.QuestionItemDTO;
import com.stadio.model.dtos.mobility.QuestionResponseDTO;
import com.stadio.model.dtos.mobility.UserPracticeDTO;
import com.stadio.model.enu.ActionBase;
import com.stadio.model.enu.OnlineTestStatus;
import com.stadio.model.enu.PracticeStatus;
import com.stadio.model.enu.UserPointType;
import com.stadio.model.model.UserAnswer;
import com.stadio.model.repository.main.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OnlineTestService extends BaseService implements IOnlineTestService {

    private Logger logger = LogManager.getLogger(OnlineTestService.class);

    @Autowired
    ExamOnlineRepository examOnlineRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    ExamSubscribeRepository examSubscribeRepository;

    @Autowired
    QuestionInExamRepository questionInExamRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserExamRepository userExamRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ExamLikesRepository examLikesRepository;

    @Autowired
    StorageService storageService;

    @Autowired
    IUserPointService userPointService;


    @Override
    public ResponseResult processGetListExamOnlineOpening() {

        List<ExamOnline> examOnlineList = examOnlineRepository.findByStatusOrderByCreatedDateDesc(OnlineTestStatus.OPENING);

        Date currentDate = new Date();

        List<ExamOnlineItemDTO> examOnlineItemDTOList = new ArrayList<>();

        for (ExamOnline examOnline: examOnlineList) {
            if (examOnline.getEndTime().before(currentDate)) {
                continue;
            }

            ExamOnlineItemDTO examOnlineItemDTO = new ExamOnlineItemDTO();

            examOnlineItemDTO.setId(examOnline.getId());
            examOnlineItemDTO.setPrice(examOnline.getPrice());
            examOnlineItemDTO.setStartTime(examOnline.getStartTime());
            examOnlineItemDTO.setEndTime(examOnline.getEndTime());

            Exam exam = examRepository.findOne(examOnline.getExamId());
            if (exam == null) {
                continue;
            }

            examOnlineItemDTO.setName(exam.getName());
            examOnlineItemDTO.setCode(exam.getCode());
            examOnlineItemDTO.setJoined(false);

            long remainingTime = examOnline.getStartTime().getTime() - System.currentTimeMillis();
            long totalTime = examOnline.getEndTime().getTime() - examOnline.getStartTime().getTime();

            if (remainingTime > 0) {
                examOnlineItemDTO.setRemainingTime(remainingTime);
            } else if (remainingTime < 0 && remainingTime > totalTime*(-0.25)) {
                examOnlineItemDTO.setRemainingTime(0); // duoi 1/4 thoi gian, user van duoc vao thi
            } else {
                examOnlineItemDTO.setRemainingTime(-1); //qua 1/4 thoi gian, khong duoc vao thi nua
            }

            examOnlineItemDTO.setExamId(exam.getId());

            User currentUser = getUserRequesting();

            if (currentUser != null) {
                ExamSubscribe userSubscribe = examSubscribeRepository.findByExamIdAndUserId(examOnline.getId(), currentUser.getId());
                if (userSubscribe != null) {
                    examOnlineItemDTO.setJoined(true);
                }

                UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(currentUser.getId(), examOnline.getExamId());

                if (userExam != null) {
                    if (userExam.getStatus().equals(PracticeStatus.PROCESS)) {
                        examOnlineItemDTO.setRemainingTime(0);
                    } else if (userExam.getStatus().equals(PracticeStatus.SUBMIT) || userExam.getStatus().equals(PracticeStatus.FAIL)) {
                        examOnlineItemDTO.setSubmitted(true);
                    }
                }

            }

            try {
                String subscribeUrl = MvcUriComponentsBuilder.fromMethodName(ExamOnlineController.class, "subscribeExamOnline", examOnline.getId())
                        .host(host).port(port)
                        .toUriString();
                examOnlineItemDTO.getAction().put(ActionBase.SUBSCRIBE_ONLINE_TEST, subscribeUrl);

                String detailsUrl = MvcUriComponentsBuilder.fromMethodName(ExamOnlineController.class, "details", examOnline.getId())
                        .host(host).port(port)
                        .toUriString();
                examOnlineItemDTO.getAction().put(ActionBase.DETAILS, detailsUrl);

            } catch (Exception e) {
                logger.info("build subscribeUrl exception: ", e);
            }

            examOnlineItemDTOList.add(examOnlineItemDTO);
        }

        return ResponseResult.newSuccessInstance(examOnlineItemDTOList);
    }

    @Override
    public ResponseResult processUserSubscribeExamOnline(String id) {

        User currentUser = getUserRequesting();

        ExamOnline examOnline = examOnlineRepository.findOne(id);
        if (examOnline == null) {
            return ResponseResult.newErrorInstance("404", getMessage("exam.online.not.found"));
        }

        if (examOnline.getMaximum() > -1)  {
            int count = examSubscribeRepository.countByExamId(id);
            if (count >= examOnline.getMaximum()) {
                return ResponseResult.newErrorInstance("404", getMessage("exam.online.is.limited"));
            }
        }

        ExamSubscribe examSubscribe = examSubscribeRepository.findByExamIdAndUserId(id, currentUser.getId());
        if (examSubscribe == null) {
            examSubscribe = new ExamSubscribe();
            examSubscribe.setUserId(currentUser.getId());
            examSubscribe.setReceiveMessage(true);
            examSubscribe.setReceiveEmail(false);
            examSubscribe.setExamId(id);

            examSubscribeRepository.save(examSubscribe);

            return ResponseResult.newSuccessInstance(examSubscribe);
        } else {
            return ResponseResult.newSuccessInstance(null);
        }
    }

    @Override
    public ResponseResult processUserCancelExamOnline(String id) {

        User currentUser = this.getUserRequesting();

        ExamSubscribe examSubscribe = examSubscribeRepository.findByExamIdAndUserId(id, currentUser.getId());
        if (examSubscribe != null) {
            examSubscribeRepository.delete(examSubscribe);
            return ResponseResult.newSuccessInstance(null);
        } else {
            return ResponseResult.newErrorInstance("404", getMessage("exam.online.null.subscribe"));
        }

    }

    @Override
    public ResponseResult processGetQuestionList(String id) {

        User currentUser = this.getUserRequesting();

        ExamOnline examOnline = examOnlineRepository.findOne(id);
        if (examOnline == null) {
            return ResponseResult.newErrorInstance("404", getMessage("exam.online.not.found"));
        }

        Exam exam = examRepository.findOne(examOnline.getExamId());

        if (exam == null) {
            return ResponseResult.newErrorInstance("404", getMessage("exam.online.not.found"));
        }

        String examId = examOnline.getExamId();
        String userId = currentUser.getId();

        subscribeAutomation(userId, id);

        UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(userId, examId);

        Date currentTime = new Date();
        long totalTime = examOnline.getEndTime().getTime() - examOnline.getStartTime().getTime();
        Date after15m = new Date(examOnline.getStartTime().getTime() + (long)(totalTime*0.25));
        Date timeCanReceiveExam = new Date(examOnline.getStartTime().getTime() - 2 * 60 * 1000);

        if (currentTime.before(timeCanReceiveExam)) {
            return ResponseResult.newErrorInstance("01", getMessage("exam.online.invalid.time"));
        } else if (currentTime.after(after15m) && userExam == null){
            return ResponseResult.newErrorInstance("01", getMessage("exam.online.time.out"));
        } else {

            if (userExam == null) {
                //chua thi lan nao
                userExam = new UserExam();
                userExam.setStartTime(new Date());
                userExam.setUserIdRef(userId);
                userExam.setStatus(PracticeStatus.PROCESS);
                userExam.setExamIdRef(examId);
                userExamRepository.save(userExam);
            } else if (userExam.getStatus().equals(PracticeStatus.SUBMIT)) {
                return ResponseResult.newErrorInstance("404", getMessage("exam.online.submitted"));
            }

            List<QuestionInExam> questionInExamList = questionInExamRepository.getQuestionOfExam(examId);
            List<QuestionItemDTO> questionDTOList = new ArrayList<>();

            for (QuestionInExam q: questionInExamList) {
                QuestionItemDTO questionItem = QuestionItemDTO.with(q.getQuestion());
                questionDTOList.add(questionItem);
            }

            UserPracticeDTO userPracticeDTO = new UserPracticeDTO();
            userPracticeDTO.setExamId(examId);

            userPracticeDTO.setQuestionList(questionDTOList);
            userPracticeDTO.setRemainingTime(examOnline.getEndTime().getTime() - System.currentTimeMillis());
            return ResponseResult.newSuccessInstance(userPracticeDTO);
        }

    }

    private void subscribeAutomation(String userId, String id) {

        ExamSubscribe examSubscribe = examSubscribeRepository.findByExamIdAndUserId(id, userId);
        if (examSubscribe == null) {
            examSubscribe = new ExamSubscribe();
            examSubscribe.setUserId(userId);
            examSubscribe.setReceiveMessage(true);
            examSubscribe.setReceiveEmail(false);
            examSubscribe.setExamId(id);
            examSubscribeRepository.save(examSubscribe);
        }
    }

    @Override
    public ResponseResult processGetDetails(String id) {

        Date currentTime = new Date();

        ExamOnline examOnline = examOnlineRepository.findOne(id);
        if (examOnline == null) {
            return ResponseResult.newErrorInstance("404", getMessage("exam.online.not.found"));
        }

        Exam exam = examRepository.findOne(examOnline.getExamId());

        if (exam == null) {
            return ResponseResult.newErrorInstance("404", getMessage("exam.online.not.found"));
        }
        User currentUser = this.getUserRequesting();

        if (currentUser != null) {
            UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(currentUser.getId(), exam.getId());
            if (userExam != null && userExam.getStatus().equals(PracticeStatus.SUBMIT)) {
                return ResponseResult.newErrorInstance("404", getMessage("exam.online.submitted"));
            }
        }

        long remainingTime = examOnline.getStartTime().getTime() - System.currentTimeMillis();
        if (remainingTime < -15*60*1000) {
            return ResponseResult.newErrorInstance("01", getMessage("exam.online.time.out"));
        }

        ExamOnlineDetailsDTO examOnlineDetailsDTO = new ExamOnlineDetailsDTO();
        examOnlineDetailsDTO.setId(examOnline.getId());
        examOnlineDetailsDTO.setStartTime(examOnline.getStartTime());
        examOnlineDetailsDTO.setEndTime(examOnline.getEndTime());
        examOnlineDetailsDTO.setRemainingTime(examOnline.getStartTime().getTime() - currentTime.getTime());
        examOnlineDetailsDTO.setPrice(examOnline.getPrice());
        examOnlineDetailsDTO.setName(exam.getName());
        examOnlineDetailsDTO.setCode(exam.getCode());
        examOnlineDetailsDTO.setLikes(examOnline.getLikes());
        examOnlineDetailsDTO.setQuestionQuantity(exam.getQuestionQuantity());
        examOnlineDetailsDTO.setDescription(examOnline.getDescription());

        if (currentUser != null){
            ExamLikes examLikes = examLikesRepository.findByExamIdAndUserId(examOnline.getId(), currentUser.getId());
            examOnlineDetailsDTO.setLiked(examLikes != null);

            ExamSubscribe userSubscribe = examSubscribeRepository.findByExamIdAndUserId(id, currentUser.getId());
            if (userSubscribe != null) {
                examOnlineDetailsDTO.setJoined(true);
            } else {
                examOnlineDetailsDTO.setJoined(false);
            }
        }

        long totalComment = commentRepository.countByObjectId(examOnline.getId());
        examOnlineDetailsDTO.setTotalComment(totalComment);

        PageRequest request = new PageRequest(0, 4, new Sort(Sort.Direction.DESC, "created_date"));
        Page<ExamSubscribe> examSubscribePage = examSubscribeRepository.findByExamId(id, request);
        examOnlineDetailsDTO.setTotalJoin((int)examSubscribePage.getTotalElements());
        List<ExamSubscribe> examSubscribeList = examSubscribePage.getContent();

        SimpleDateFormat fm = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        List<JoinerDTO> joinerDTOList = new ArrayList<>();
        for (ExamSubscribe subscribe: examSubscribeList) {
            User userJoin = userRepository.findOne(subscribe.getUserId());
            if (userJoin != null) {
                JoinerDTO joinerDTO = new JoinerDTO();
                joinerDTO.setId(userJoin.getId());
                joinerDTO.setUsername(userJoin.getFullName());
                joinerDTO.setAvatar(userJoin.getAvatar());
                joinerDTO.setUserCode(userJoin.getCode());
                joinerDTO.setJoinTime(fm.format(subscribe.getJoinTime()));
                joinerDTOList.add(joinerDTO);
            }
        }

        examOnlineDetailsDTO.setUserList(joinerDTOList);

        try {
            String subscribeUrl = MvcUriComponentsBuilder.fromMethodName(ExamOnlineController.class, "subscribeExamOnline", examOnline.getId())
                    .host(host).port(port)
                    .toUriString();
            examOnlineDetailsDTO.getAction().put(ActionBase.SUBSCRIBE_ONLINE_TEST, subscribeUrl);

            String likeUrl = MvcUriComponentsBuilder.fromMethodName(ExamOnlineController.class, "actionLike", examOnline.getId())
                    .host(host).port(port)
                    .toUriString();
            examOnlineDetailsDTO.getAction().put(ActionBase.LIKE, likeUrl);

        } catch (Exception e) {
            logger.error("Builder actions fail: ", e);
        }


        return ResponseResult.newSuccessInstance(examOnlineDetailsDTO);
    }

    @Override
    public ResponseResult processGetListExamOnlineFinished(int page, int limit) {
        PageRequest request = new PageRequest(page - 1, limit, new Sort(Sort.Direction.DESC, "start_time"));
        List<ExamOnline> examOnlineList = examOnlineRepository.findByStatus(OnlineTestStatus.FINISHED,  request).getContent();

        List<HistoryItemDTO> historyItemDTOList = new ArrayList<>();
        for (ExamOnline examOnline: examOnlineList) {

            HistoryItemDTO historyItemDTO = new HistoryItemDTO();
            historyItemDTO.setId(examOnline.getId());
            Exam exam = examRepository.findOne(examOnline.getExamId());
            if (exam == null) {
                continue;
            }
            historyItemDTO.setName(exam.getName());
            historyItemDTO.setSubmitCount(examOnline.getSubmitCount());
            historyItemDTO.setAverage(MathUtils.round(examOnline.getAverage() * 10.0));
            historyItemDTO.setTime(examOnline.getStartTime());

            try {

                String resultUrl = MvcUriComponentsBuilder.fromMethodName(ExamOnlineController.class, "getTablePoint", examOnline.getId())
                        .host(host).port(port)
                        .toUriString();
                historyItemDTO.getActions().put(ActionBase.DETAILS, resultUrl);

            } catch (Exception e) {
                logger.error("processGetListExamOnlineFinished: ", e);
            }

            historyItemDTOList.add(historyItemDTO);

        }

        return ResponseResult.newSuccessInstance(historyItemDTOList);
    }

    @Override
    public ResponseResult processGetTablePointOfExamOnline(String id) {

        ExamOnline examOnline = examOnlineRepository.findOne(id);
        if (examOnline == null) {
            return ResponseResult.newErrorInstance("404", getMessage("exam.online.not.found"));
        }

        Map<String, Object> body = new HashMap<>();
        body.put("results", null);

        User currentUser = this.getUserRequesting();

        List<UserExam> userExamList = userExamRepository.findByExamIdRefAndStatusOrderByCorrectNumberDescDurationAsc(examOnline.getExamId(), PracticeStatus.SUBMIT);
        body.put("submitCount", userExamList.size());
        List<TablePointDTO> tablePointDTOList = new ArrayList<>();

        int position = 1;
        for (UserExam userExam: userExamList) {
            User user = userRepository.findOne(userExam.getUserIdRef());
            if (user == null) {
                continue;
            }

            if (!PracticeStatus.SUBMIT.equals(userExam.getStatus())) {
                continue;
            }

            if (position < 100) {
                TablePointDTO tablePointDTO = new TablePointDTO();
                tablePointDTO.setId(user.getId());
                tablePointDTO.setAvatar(user.getAvatar());
                tablePointDTO.setFullname(user.getFullName());
                tablePointDTO.setUsername(user.getUsername());
                tablePointDTO.setPosition(position);
                double point = (userExam.getCorrectNumber() * 10.0 / userExam.getTotal());
                tablePointDTO.setPoint(MathUtils.round(point));
                tablePointDTOList.add(tablePointDTO);
            }

            if (currentUser != null && currentUser.getId().equals(user.getId())) {
                Map<String, Object> yourResults = new LinkedHashMap<>();
                yourResults.put("correctNumber", userExam.getCorrectNumber());
                yourResults.put("total", userExam.getTotal());
                yourResults.put("position", position);
                body.put("results", yourResults);
            }

            position++;
        }


        body.put("tablePoint", tablePointDTOList);

        return ResponseResult.newSuccessInstance(body);
    }

    @Override
    public ResponseResult processGetResultsOfExamOnline(String id) {
        ExamOnline examOnline = examOnlineRepository.findOne(id);
        if (examOnline == null) {
            return ResponseResult.newErrorInstance("404", getMessage("exam.online.not.found"));
        }


        String failMessage = "";
        User user = this.getUserRequesting();
        if (user != null) {
           UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(user.getId(), examOnline.getExamId());
           if (userExam != null && userExam.getStatus().equals(PracticeStatus.SUBMIT)) {
               File f = new File(userExam.getDetails());
               try {
                   Map<String, Object> results = mapper.readValue(f, HashMap.class);
                   return ResponseResult.newSuccessInstance(results);
               } catch (IOException e) {
                   logger.error("processGetResultsOfExamOnline: ", e);
                   failMessage = e.getMessage();
               }
           } else {
               return ResponseResult.newErrorInstance("01", "Bạn chưa nộp đề thi này!");
           }
        }

        return ResponseResult.newErrorInstance("01", failMessage);
    }

    @Override
    public ResponseResult processAllowUserCanLikeIt(String id) {
        User user = this.getUserRequesting();

        ExamOnline examOnline = examOnlineRepository.findOne(id);
        if (examOnline == null) {
            return ResponseResult.newErrorInstance("404", getMessage("exam.online.not.found"));
        }

        ExamLikes examLikes = examLikesRepository.findByExamIdAndUserId(id, user.getId());
        if (examLikes == null) {
            examLikes = new ExamLikes();
            examLikes.setExamId(id);
            examLikes.setUserId(user.getId());
            examLikesRepository.save(examLikes);

            long likes = examOnline.getLikes();
            likes++;
            examOnline.setLikes(likes);
            examOnlineRepository.save(examOnline);
            return ResponseResult.newSuccessInstance(null);
        } else {
            return ResponseResult.newErrorInstance("01", "This exam is liked");
        }

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
    public ResponseResult processSubmitExamOnline(String id, ExamOnlineSubmitDTO examOnlineSubmitDTO) {

        User user = this.getUserRequesting();

        ExamOnline examOnline = examOnlineRepository.findOne(id);
        if (examOnline == null) {
            return ResponseResult.newErrorInstance("04", getMessage("exam.online.not.found"));
        }

        UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(user.getId(), examOnline.getExamId());

        if (userExam != null && userExam.getStatus().equals(PracticeStatus.PROCESS)) {

            try {
                Map<String, Object> results = this.compareFromMongo(examOnlineSubmitDTO.getAnswers(), examOnline.getExamId());
                //logger.info("["+user.getId()+"] Results: " + results.toString());
                userExam.setUserIdRef(user.getId());
                userExam.setExamIdRef(examOnline.getExamId());
                userExam.setEndTime(new Date(examOnlineSubmitDTO.getEndTime()));
                userExam.setCorrectNumber(Integer.parseInt(results.get("correct_number").toString()));
                Integer total = Integer.parseInt( results.get("total_question").toString() );
                userExam.setTotal(total);

                updateUserPoint(user.getId(),new Double(userExam.getCorrectNumber()), new Double(total), examOnline.getExamId());

                int duration = (int) (userExam.getEndTime().getTime() - userExam.getStartTime().getTime()) / 1000;
                userExam.setDuration(duration);
                userExam.setStatus(PracticeStatus.SUBMIT);

                Path p = storageService.getLocation(FolderName.USERS)
                        .resolve("save_by_feature")
                        .resolve("submit_online")
                        .resolve(examOnline.getId());

                if (!p.toFile().exists()) {
                    Files.createDirectories(p);
                }
                String fname = user.getId() + "_" + System.currentTimeMillis();
                File f = p.resolve(fname).toFile();
                mapper.writeValue(f, results);
                userExam.setDetails(f.getPath());
                logger.info("User submit online to: " + f.getPath());

                updateExamOnlineAverage(examOnline.getId(), (double)results.get("avg"));

                userExamRepository.save(userExam);

                return ResponseResult.newSuccessInstance(null);
            } catch (Exception e) {
                logger.error("submit exception: ", e);
                return ResponseResult.newErrorInstance("01", e.getMessage());
            }
        } else {
            return ResponseResult.newErrorInstance(ResponseCode.SUCCESS, "receiveSubmit: User submitted -> " + userExam.getDetails());
        }

    }

    @Async
    public void updateExamOnlineAverage(String examOnlineId, double avg) {

        ExamOnline examOnline = examOnlineRepository.findOne(examOnlineId);
        if (examOnline != null) {
            int oldSubmitCount = examOnline.getSubmitCount();
            examOnline.setSubmitCount(oldSubmitCount + 1);
            double average = (oldSubmitCount * examOnline.getAverage() + avg) / examOnline.getSubmitCount();
            examOnline.setAverage(average);
            examOnlineRepository.save(examOnline);
            logger.info("updateExamOnlineAverage:" + examOnline.getSubmitCount() + " : " + examOnline.getAverage());
        }
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
            //questionResponse.setExplain(question.getExplain());

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
        results.put("total_question", n);
        results.put("avg", (double)correctNumber/n);
        results.put("results", questionResponseDTOList);
        return results;
    }

}
