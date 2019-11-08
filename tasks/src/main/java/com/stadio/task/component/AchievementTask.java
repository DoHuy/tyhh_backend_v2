package com.stadio.task.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stadio.model.documents.*;
import com.stadio.model.dtos.cms.NotificationQueue;
import com.stadio.model.enu.*;
import com.stadio.model.repository.main.*;
import com.stadio.task.RabbitProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class AchievementTask {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    AchievementRepository achievementRepository;

    @Autowired
    RankRepository rankRepository;

    @Autowired
    UserRankRepository userRankRepository;

    @Autowired
    UserAchievementRepository userAchievementRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired DeviceRepository deviceRepository;

    @Autowired
    RabbitProducer rabbitProducer;

    protected ObjectMapper mapper = new ObjectMapper();

    protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private Logger logger = LogManager.getLogger(AchievementTask.class);

//    public void processCaculateAchievement(ScheduleType scheduleType){
//        // find achievement where rank_type
//        List<Achievement> achievements = mongoTemplate.find(new Query(Criteria.where("scheduleType").is(scheduleType)), Achievement.class);
//        CompletableFuture<Void> completableFutures[] = new CompletableFuture[achievements.size()];
//
//
//        for(int pos = 0;pos<achievements.size();pos++) {
//            Achievement achievement = achievements.get(pos);
//            // create and add comptableFuture
//            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
//                String achievementId = achievement.getId();
//                String condition = achievement.getCondition();
//                try{
//                    List<User> users = mongoTemplate.find(new BasicQuery(condition), User.class);
//                    users.forEach(user -> {
//                        Query query1 = new Query()
//                                .addCriteria(Criteria.where("userId").is(user.getId()))
//                                .addCriteria(Criteria.where("achievementId").is(achievementId));
//                        // for hour type =  not set expired
//                        if(!scheduleType.equals(RankType.HOURLY))
//                                query1.addCriteria(Criteria.where("expired").lt(new Date()));
//                        //check duplicate
//                        UserAchievement userAchievement = mongoTemplate.findOne(query1, UserAchievement.class);
//                        if (userAchievement == null) {
//                            userAchievement = new UserAchievement();
//                            userAchievement.setAchievementId(achievementId);
//                            userAchievement.setUserId(user.getId());
//
//                            Calendar calendar= Calendar.getInstance();
//                            switch (scheduleType){
//                                case MINUTE:
//                                    userAchievement.setExpired(new Date(System.currentTimeMillis()+60000));
//                                    break;
//                                case HOURLY:
//                                    //not set expired
//                                    break;
//                                case DAILY:
//                                    calendar.add(Calendar.DATE,1);
//                                    userAchievement.setExpired(calendar.getTime());
//                                    break;
//                                case WEEKLY:
//                                    calendar.add(Calendar.WEEK_OF_MONTH,1);
//                                    calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
//                                    userAchievement.setExpired(calendar.getTime());
//                                    break;
//                                case MONTHLY:
//                                    calendar.add(Calendar.MONTH, 1);
//                                    calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
//                                    userAchievement.setExpired(calendar.getTime());
//                                    break;
//                                case QUARTERLY:
//                                    calendar.add(Calendar.MONTH, 3);
//                                    calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
//                                    userAchievement.setExpired(calendar.getTime());
//                                    break;
////                                case YEARLY:
////                                    calendar.add(Calendar.YEAR,1);
////                                    calendar.set(Calendar.DAY_OF_YEAR,1);
////                                    userAchievement.setExpired(calendar.getTime());
////                                    break;
//                            }
//                            mongoTemplate.save(userAchievement);
//                        }
//                    });
//                }catch (Exception ex){
//                    ex.printStackTrace();
//                }
//            });
//
//            completableFutures[pos] = completableFuture;
//        }
//
//        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(completableFutures);
//        try {
//            combinedFuture.get();
//        } catch (Exception e) {
//            logger.error("Exception processCaculateAchievement: ", e);
//        }
//    }


    @Scheduled(cron = "${achievement.daily}")
    public void reportAchievementDaily()
    {
        logger.info("Start reportAchievementDaily: " + dateFormat.format(new Date()));
        Achievement achievement = achievementRepository.findByAchievementType(AchievementType.TOP_1_DAY);
        if (achievement == null) {
            achievement = new Achievement();
            achievement.setName(AchievementType.TOP_1_DAY.name());
            achievement.setScheduleType(ScheduleType.DAILY);
            achievement.setAchievementType(AchievementType.TOP_1_DAY);
            achievementRepository.save(achievement);
        }

        setAchievementWith(achievement, RankType.DAILY);
    }

    @Scheduled(cron = "${achievement.weekly}")
    public void reportAchievementWeekly()
    {
        logger.info("Start reportAchievementWeekly: " + dateFormat.format(new Date()));
        Achievement achievement = achievementRepository.findByAchievementType(AchievementType.TOP_1_WEEK);
        if (achievement == null) {
            achievement = new Achievement();
            achievement.setName(AchievementType.TOP_1_WEEK.name());
            achievement.setScheduleType(ScheduleType.WEEKLY);
            achievement.setAchievementType(AchievementType.TOP_1_WEEK);
            achievementRepository.save(achievement);
        }

        setAchievementWith(achievement, RankType.WEEKLY);
    }

    @Scheduled(cron = "${achievement.monthly}")
    public void reportAchievementMonthly()
    {
        logger.info("Start reportAchievementMonthly: " + dateFormat.format(new Date()));
        Achievement achievement = achievementRepository.findByAchievementType(AchievementType.TOP_1_MONTH);
        if (achievement == null) {
            achievement = new Achievement();
            achievement.setName(AchievementType.TOP_1_MONTH.name());
            achievement.setScheduleType(ScheduleType.MONTHLY);
            achievement.setAchievementType(AchievementType.TOP_1_MONTH);
            achievementRepository.save(achievement);
        }

        setAchievementWith(achievement, RankType.MONTHLY);
    }

    private void setAchievementWith(Achievement achievement, RankType rankType) {

        Rank rank = rankRepository.findTopByRankTypeOrderByCreatedDateDesc(rankType);

        logger.info("> Found top rank: " + rank.getName());
        if (rank != null) {
            List<UserRank> userRankList = userRankRepository.findByRankIdOrderByPointDesc(rank.getId());
            if (userRankList != null && !userRankList.isEmpty()) {
                UserAchievement userAchievement = userAchievementRepository.findByUserIdAndAchievementId(userRankList.get(0).getUserId(), achievement.getId());
                if (userAchievement == null) {
                    userAchievement = new UserAchievement();
                    userAchievement.setAchievementId(achievement.getId());
                    userAchievement.setExpired(null);
                    userAchievement.setUserId(userRankList.get(0).getUserId());
                    userAchievementRepository.save(userAchievement);

                    Notification notification = new Notification();
                    notification.setTitle("Thành tích mới: " + achievement.getName());
                    notification.setMessage("Bạn vừa đạt được thành tích " + achievement.getName() + " giữ gìn \"phong độ\" để luôn đứng TOP nhé!!!");
                    notification.setType(NotificationType.FIRE_BASE);
                    notification.setScreen(MobileScreen.PROFILE);
                    Date sendTime = new Date(System.currentTimeMillis());
                    notification.setSendTime(sendTime);

                    List<Device> deviceList = deviceRepository.findDeviceByUserId(userAchievement.getUserId());
                    pushSingleDeviceToQueue(notification, deviceList);

                    Message message = new Message();
                    message.setTitle(notification.getTitle());
                    message.setContent(notification.getMessage());
                    message.setUserId(userAchievement.getUserId());

                    messageRepository.save(message);

                    logger.info("> Give achievement for userId: " + userRankList.get(0).getUserId());
                }
            }
        }

    }

    @Async
    public void pushSingleDeviceToQueue(Notification notification, List<Device> deviceList)
    {
        List<String> deviceListInQueue = new ArrayList<>();

        for (Device device: deviceList) {
            deviceListInQueue.add(device.getDeviceToken());
        }

        try
        {
            NotificationQueue queue = new NotificationQueue();
            queue.setDeviceList(deviceListInQueue);
            queue.setOsVersion(null);
            queue.setNotification(notification);
            String jsonArr = mapper.writeValueAsString(queue);
            rabbitProducer.sendNotification(jsonArr);
        }
        catch (Exception e)
        {
            logger.error("RabbitMQ exception pushSingleDeviceToQueue: ", e);
        }
    }
}
