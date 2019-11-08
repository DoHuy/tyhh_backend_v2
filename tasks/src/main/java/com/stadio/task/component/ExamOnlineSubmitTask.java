package com.stadio.task.component;

import com.hoc68.users.documents.User;
import com.stadio.common.enu.FolderName;
import com.stadio.common.service.impl.StorageService;
import com.stadio.model.documents.*;
import com.stadio.model.dtos.cms.NotificationQueue;
import com.stadio.model.dtos.mobility.QuestionResponseDTO;
import com.stadio.model.enu.PracticeStatus;
import com.stadio.model.enu.RankType;
import com.stadio.model.enu.UserPointType;
import com.stadio.model.model.Answer;
import com.stadio.model.model.UserAnswer;
import com.stadio.model.repository.main.*;
import com.stadio.model.repository.user.UserRepository;
import com.stadio.task.model.ExamOnlineSubmitDTO;
import com.stadio.task.service.PushNotificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ExamOnlineSubmitTask {

    private Logger logger = LogManager.getLogger(ExamOnlineSubmitTask.class);

    private ObjectMapper mapper = new ObjectMapper();

    private SimpleDateFormat fm = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExamOnlineRepository examOnlineRepository;

    @Autowired
    QuestionInExamRepository questionInExamRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    UserPointRepository userPointRepository;

    @Autowired
    UserExamRepository userExamRepository;

    @Autowired
    StorageService storageService;

    @Autowired RankRepository rankRepository;

    @Autowired UserRankRepository userRankRepository;

    @Autowired
    PushNotificationService pushNotificationService;

    /**
     * Nhan message submit tu RabbitMQ ve handler va luu xuong database
     * @param payload
     */
    @RabbitListener(queues = "#{queueExamOnlineSubmit.name}")
    public void receiveSubmit(byte[] payload) {
        Date currentDate = new Date();

        try {
            String msg = new String(payload, "UTF-8");
            //logger.error("receiveSubmit: " + msg);
            ExamOnlineSubmitDTO examOnlineSubmitDTO = mapper.readValue(msg, ExamOnlineSubmitDTO.class);

            User user = userRepository.findOne(examOnlineSubmitDTO.getSenderId());
            if (user == null) {
                logger.error("receiveSubmit: Not found user -> " + examOnlineSubmitDTO.getSenderId());
                return;
            }

            ExamOnline examOnline = examOnlineRepository.findOne(examOnlineSubmitDTO.getExamOnlineId());
            if (examOnline == null) {
                logger.error("receiveSubmit: Not found examOnline -> " + examOnlineSubmitDTO.getExamOnlineId());
                return;
            }

            UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(user.getId(), examOnline.getExamId());

            if (userExam != null && userExam.getStatus().equals(PracticeStatus.PROCESS)) {

                //Tra ve loi neu gui bai muon sau 5' ket thuc de thi
                Date endTimeDelay = new Date(examOnline.getEndTime().getTime() + 5*60*1000);
                if (currentDate.after(endTimeDelay)) {
                    String msgFail = "Expired receive online -> sendTime: " + fm.format(currentDate) + " | endTimeDelay: " + fm.format(endTimeDelay);
                    logger.error("receiveSubmit: Expired receive online -> sendTime: " + fm.format(currentDate) + " | endTimeDelay: " + fm.format(endTimeDelay));
                    userExam.setUserIdRef(user.getId());
                    userExam.setExamIdRef(examOnline.getExamId());
                    userExam.setStatus(PracticeStatus.FAIL);
                    userExam.setDetails(msgFail);
                } else {
                    Map<String, Object> results = this.compareFromMongo(examOnlineSubmitDTO.getAnswers(), examOnline.getExamId());
                    //logger.info("["+user.getId()+"] Results: " + results.toString());
                    userExam.setUserIdRef(user.getId());
                    userExam.setExamIdRef(examOnline.getExamId());
                    userExam.setEndTime(new Date());
                    userExam.setCorrectNumber(Integer.parseInt(results.get("correct_number").toString()));
                    Integer total = Integer.parseInt( results.get("total_question").toString() );
                    userExam.setTotal(total);
                    int duration = (int) (userExam.getEndTime().getTime() - userExam.getStartTime().getTime()) / 1000;
                    userExam.setDuration(duration);
                    userExam.setStatus(PracticeStatus.SUBMIT);
                    saveUserPoint(user.getId(), examOnline.getExamId(), new Double(userExam.getCorrectNumber()), new Double(total));
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
                }
                userExamRepository.save(userExam);
            } else {
                logger.info("receiveSubmit: User submitted -> " + userExam.getDetails());
                return;
            }
        } catch (Exception e) {
            logger.error("receiveSubmit: " + e);
        }

        return;
    }

    @Async
    public void saveUserPoint(String userId, String examId, Double total, Double right){

        Double point = 30 * right / total;

        findAndUpdateUserRank(userId, point);

        UserPoint userPoint = new UserPoint();
        userPoint.setUserId(userId);
        userPoint.setPoint(point);
        userPoint.setSourceId(examId);
        userPoint.setSourceName(examRepository.findOne(examId).getName());
        userPoint.setSourceType(UserPointType.EXAM_ONLINE);
        userPointRepository.save(userPoint);
    }


    @Async
    public void findAndUpdateUserRank(String userId, Double point){

        LocalDate targetLocaleDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Date startOfDay = Date.from(targetLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Rank rank = rankRepository.findTopByRankTypeAndStartTimeEquals(RankType.DAILY, startOfDay);

        if (rank == null){
            rank = new Rank();
            rank.setStartTime(startOfDay);
            LocalDate tomorrow = targetLocaleDate.plusDays(1);
            rank.setEndTime(Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            rank.setRankType(RankType.DAILY);
            rank.setCreatedDate(new Date());
            rank.setName("DAILY");
            rankRepository.save(rank);
        }

        updateUserRank(userId, point, rank.getId());
    }

    private void updateUserRank(String userId, Double point, String rankId){
        UserRank userRank = userRankRepository.findTopByRankIdAndUserId(rankId, userId);

        if (userRank == null){
            userRank = new UserRank();
            userRank.setUserId(userId);
            userRank.setRankId(rankId);
        }

        userRank.setPoint( userRank.getPoint() + point);
        userRankRepository.save(userRank);
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
