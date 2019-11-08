package com.stadio.task.component;

import com.hoc68.users.documents.User;
import com.stadio.common.utils.MathUtils;
import com.stadio.model.documents.*;
import com.stadio.model.dtos.cms.NotificationQueue;
import com.stadio.model.enu.*;
import com.stadio.model.model.TrackingEvent;
import com.stadio.model.repository.main.*;
import com.stadio.model.repository.user.UserRepository;
import com.stadio.task.RabbitProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class ExamOnlineTrackingEventTask {

    private Logger logger = LogManager.getLogger(ExamOnlineTrackingEventTask.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    ExamOnlineRepository examOnlineRepository;

    @Autowired
    ExamSubscribeRepository examSubscribeRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    RabbitProducer rabbitProducer;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    UserExamRepository userExamRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    private static final long DURATION_TIME_SEND_RESULT = 10*60*1000;

    @RabbitListener(queues = "#{queueTrackingExamOnline.name}")
    public void receiveTrackingExamOnline(String msg) {
        logger.error("TrackingEvent: " + msg);
        try {
            TrackingEvent trackingEvent = mapper.readValue(msg, TrackingEvent.class);
            if( trackingEvent.getEvent().equals(ActionEvent.EXAM_ONLINE_IS_CREATED)) {
                String examOnlineId = trackingEvent.getObjectId();
                final ExamOnline examOnline = examOnlineRepository.findOne(examOnlineId);
                if (examOnline != null) {
                    Exam exam = examRepository.findOne(examOnline.getExamId());
                    if (exam != null) {
                        executeScheduleRemindBefore5Min(examOnline, exam);
                        autoFinishedExamOnline(examOnline);
                        executeScheduleTablePointMessage(examOnline, exam);
                    }
                }
            }

            if ( trackingEvent.getEvent().equals(ActionEvent.EXAM_ONLINE_PUSH_REMIND)) {
                String examOnlineId = trackingEvent.getObjectId();
                final ExamOnline examOnline = examOnlineRepository.findOne(examOnlineId);
                if (examOnline != null) {
                    Exam exam = examRepository.findOne(examOnline.getExamId());
                    if (exam != null) {
                        pushRemindNotification(examOnline, exam);
                        pushResultsNotification(examOnline, exam);
                    }
                }
            }

            if ( trackingEvent.getEvent().equals(ActionEvent.EXAM_ONLINE_PUSH_RESULT)) {
                String examOnlineId = trackingEvent.getObjectId();
                final ExamOnline examOnline = examOnlineRepository.findOne(examOnlineId);
                if (examOnline != null) {
                    Exam exam = examRepository.findOne(examOnline.getExamId());
                    if (exam != null) {
                        pushResultsNotification(examOnline, exam);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("receiveTrackingExamOnline: ", e);
        }

    }

    @Async
    public void executeScheduleTablePointMessage(ExamOnline examOnline, Exam exam) {
        logger.info("executeScheduleTablePointMessage:" + exam.getName());
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

        long timeExecute = examOnline.getEndTime().getTime() + DURATION_TIME_SEND_RESULT - System.currentTimeMillis();
        Runnable task = () -> pushResultsNotification(examOnline, exam);
        timeExecute = timeExecute/1000;
        executor.schedule(task, timeExecute, TimeUnit.SECONDS);
    }

    @Async
    public void autoFinishedExamOnline(ExamOnline examOnline) {
        logger.info("autoFinishedExamOnline:" + examOnline.getId());
        long timeExecute = examOnline.getEndTime().getTime() + 9*60*1000 - System.currentTimeMillis();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

        Runnable task = () -> {
            examOnline.setStatus(OnlineTestStatus.FINISHED);
            examOnlineRepository.save(examOnline);
            logger.info("Run autoFinishedExamOnline with: " + examOnline.toString());
        };
        timeExecute = timeExecute/1000;
        executor.schedule(task, timeExecute, TimeUnit.SECONDS);
    }

    @Async
    public void executeScheduleRemindBefore5Min(ExamOnline examOnline, Exam exam) {
        logger.info("executeScheduleRemindBefore5Min:" + exam.getName());

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

        long timeExecute = examOnline.getStartTime().getTime() - 5*60*1000 - System.currentTimeMillis();
        Runnable task = () -> pushRemindNotification(examOnline, exam);
        timeExecute = timeExecute/1000;
        executor.schedule(task, timeExecute, TimeUnit.SECONDS);

    }

    private void pushRemindNotification(ExamOnline examOnline, Exam exam) {
        List<ExamSubscribe> examSubscribeList = examSubscribeRepository.findAllByExamId(examOnline.getId());
        List<String> userIdList = examSubscribeList.stream().map(x -> x.getUserId()).collect(Collectors.toList());
        List<String> deviceList = deviceRepository.findDeviceByUserIdIn(userIdList).stream().map(x -> x.getDeviceToken()).collect(Collectors.toList());
        //push notification on service
        try {
            NotificationQueue queue = new NotificationQueue();
            queue.setDeviceList(deviceList);
            queue.setOsVersion(null);

            Notification notification = new Notification();
            notification.setTitle(exam.getName());
            notification.setMessage("Chỉ còn 5 phút nữa là bắt đầu " + exam.getName() + " bạn hãy nhanh tay chuẩn bị dụng cụ để bắt đầu kỳ thi thật tốt.");
            notification.setObjectId(examOnline.getId());
            notification.setScreen(MobileScreen.EXAM_ONLINE_REMIND);
            notification.setStatus(NotificationStatus.PROCESS);

            Date sendTime = new Date(System.currentTimeMillis());
            notification.setSendTime(sendTime);

            notification.setType(NotificationType.FIRE_BASE);

            queue.setNotification(notification);
            String jsonArr = mapper.writeValueAsString(queue);

            logger.info("Run executeScheduleRemindBefore5Min with: " + jsonArr);

            rabbitProducer.sendNotification(jsonArr);
        } catch (Exception e) {
            logger.error("executeScheduleRemindBefore5Min: ", e);
        }
    }

    private void pushResultsNotification(ExamOnline examOnline, Exam exam) {
        List<UserExam> userExamList = userExamRepository.findByExamIdRefAndStatusOrderByCorrectNumberDescDurationAsc(examOnline.getExamId(), PracticeStatus.SUBMIT);
        int n = userExamList.size();
        int position = 1;
        for (UserExam userExam: userExamList) {
            User user = userRepository.findOne(userExam.getUserIdRef());
            if (user != null) {
                double point = userExam.getCorrectNumber() * 10.0 / userExam.getTotal();
                //double point = userExam.getCorrectNumber()/userExam.getTotal() * 10.0;
                String title = "Kết quả " + exam.getName();
                String msg = "Bạn đạt " + MathUtils.round(point) + " điểm trong kỳ thi " + exam.getName() + ". Xếp hạng " + position + "/" + n + " người tham gia.";

                Message message = new Message();
                message.setTitle(title);
                message.setContent(msg);
                message.setUserId(user.getId());

                messageRepository.save(message);

                List<Device> deviceList = deviceRepository.findDeviceByUserId(userExam.getUserIdRef());
                List<String> strDeviceList = deviceList.stream().map(x -> x.getDeviceToken()).collect(Collectors.toList());

                //push notification on service
                try {
                    NotificationQueue queue = new NotificationQueue();
                    queue.setDeviceList(strDeviceList);
                    queue.setOsVersion(null);

                    Notification notification = new Notification();
                    notification.setTitle(title);
                    notification.setMessage(msg);
                    notification.setObjectId(examOnline.getId());
                    notification.setScreen(MobileScreen.TABLE_POINT);
                    notification.setStatus(NotificationStatus.PROCESS);

                    Date sendTime = new Date(System.currentTimeMillis());
                    notification.setSendTime(sendTime);

                    notification.setType(NotificationType.FIRE_BASE);

                    queue.setNotification(notification);
                    String jsonArr = mapper.writeValueAsString(queue);

                    logger.info("Run executeScheduleTablePointMessage with: " + jsonArr);

                    rabbitProducer.sendNotification(jsonArr);
                } catch (Exception e) {
                    logger.error("executeScheduleRemindBefore5Min: ", e);
                }

            }
            position++;
        }
    }


}
