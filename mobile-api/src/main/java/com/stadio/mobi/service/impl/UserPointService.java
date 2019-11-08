package com.stadio.mobi.service.impl;

import com.hoc68.users.documents.User;
import com.mongodb.DBObject;
import com.stadio.common.utils.ResponseCode;
import com.stadio.common.utils.StringUtils;
import com.stadio.common.utils.Tuple;
import com.stadio.mobi.dtos.UserPointFormDTO;
import com.stadio.mobi.dtos.UserRankDTO;
import com.stadio.mobi.dtos.user.UserInfoDTO;
import com.stadio.mobi.dtos.user.UserPointDTO;
import com.stadio.mobi.dtos.user.UserProfileDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IFastPracticeService;
import com.stadio.mobi.service.IRankService;
import com.stadio.mobi.service.IUserPointService;
import com.stadio.model.documents.Rank;
import com.stadio.model.documents.UserPoint;
import com.stadio.model.documents.UserRank;
import com.stadio.model.dtos.cms.UserPointMongoDTO;
import com.stadio.model.dtos.mobility.UserDetailsDTO;
import com.stadio.model.enu.RankType;
import com.stadio.model.enu.UserPointType;
import com.stadio.model.repository.main.ClazzRepository;
import com.stadio.model.repository.main.RankRepository;
import com.stadio.model.repository.main.UserPointRepository;
import com.stadio.model.repository.main.UserRankRepository;
import com.stadio.model.repository.user.UserRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sun.reflect.generics.repository.ClassRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserPointService extends BaseService implements IUserPointService{

    @Autowired
    UserPointRepository userPointRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    IFastPracticeService fastPracticeService;

    @Autowired
    RankRepository rankRepository;

    @Autowired
    UserRankRepository userRankRepository;

    @Autowired IRankService rankService;

    @Autowired
    ClazzRepository clazzRepository;

    private static final SimpleDateFormat fm = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public void createUserPoint(UserPointFormDTO userPointFormDTO) {

        if (userPointFormDTO.getSourceType() == UserPointType.EXAM_OFFLINE){
            List<UserPoint> pointList = getUserPointInMonth(userPointFormDTO.getUserId(), null);
            Integer pointInDay = 0;
            Integer pointMonth = 0;

            for(UserPoint userPoint: pointList) {
                if (userPoint.getSourceType() == UserPointType.EXAM_OFFLINE){
                    pointMonth ++;
                    LocalDate localDate = userPoint.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate current = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if (localDate.getDayOfMonth() == current.getDayOfMonth()){
                        pointInDay ++;
                    }
                }
            }

            if (pointInDay <= 3 && pointMonth <= 20){
                saveUserPoint(userPointFormDTO, 10.0);
            }

        }else if (userPointFormDTO.getSourceType() == UserPointType.EXAM_ONLINE){
            saveUserPoint(userPointFormDTO, 30.0);
        }else {
            saveUserPoint(userPointFormDTO, 2.0);
        }
    }

    private void saveUserPoint(UserPointFormDTO userPointFormDTO, Double factor){

        Double point = factor * userPointFormDTO.getPoint();

        findAndUpdateUserRank(userPointFormDTO.getUserId(), point);

        UserPoint userPoint = new UserPoint();
        userPoint.setUserId(userPointFormDTO.getUserId());
        userPoint.setPoint(point);
        userPoint.setSourceId(userPointFormDTO.getSourceId());
        userPoint.setSourceName(userPointFormDTO.getSourceName());
        userPoint.setSourceType(userPointFormDTO.getSourceType());
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

    private List<UserPoint> getUserPointInMonth(String userId, String month) {
        if (!StringUtils.isNotNull(userId)) {
            userId = this.getUserRequesting().getId();
        }

        Tuple<Date, Date> tupleDate = getStartDateInMonthAndTheNextMonth(month);

        if (tupleDate != null) {
            return userPointRepository.findAllByUserIdAndCreatedDateBetweenOrderByCreatedDateDesc(userId, tupleDate.x, tupleDate.y);
        }
        return null;
    }

    @Override
    public ResponseResult processGetUserPoint(String userId, String month) {

        if (!StringUtils.isNotNull(userId) || userId.isEmpty()) {
            userId = this.getUserRequesting().getId();
        }

        Map<String, Object> response = new HashMap<>();

        List<UserPoint> pointList = getUserPointInMonth(userId, month);

        response.put("points", pointList);

        Tuple<Double, Long> daily = getUserPosition(RankType.DAILY, userId);
        response.put("dailyPoint", daily.x);
        response.put("dailyPosition", daily.y);

        Tuple<Double, Long> monthly = getUserPosition(RankType.MONTHLY, userId);
        response.put("monthlyPoint", monthly.x);
        response.put("monthlyPosition", monthly.y);

        User user = userRepository.findOne(userId);
        UserInfoDTO userInfoDTO = new UserInfoDTO(user, clazzRepository);
        response.put("info", userInfoDTO);

        if (pointList != null){
            return ResponseResult.newSuccessInstance(response);
        }else{
            return ResponseResult.newErrorInstance(ResponseCode.INVALID_VALUE, getMessage("userPoint.invalid.date.time"))   ;
        }
    }

    private Tuple<Double, Long> getUserPosition(RankType rankType, String userId)
    {
        Rank rank = rankRepository.findTopByRankTypeOrderByCreatedDateDesc(rankType);
        if (rank != null){

            UserRank userRank = userRankRepository.findTopByRankIdAndUserId(rank.getId(), userId);

            if (userRank != null){

                Long pos = userRankRepository.countAllByRankIdAndPointGreaterThanEqual(rank.getId(), userRank.getPoint());

                return new Tuple(userRank.getPoint(), pos);
            }
        }

        return new Tuple(0, -1);
    }

    private LocalDate getStartDateInMonth(String month) throws ParseException {
        Date targetDate = new Date();

        if (StringUtils.isNotNull(month)){
            targetDate = fm.parse(month);
        }

        LocalDate targetLocaleDate = targetDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);

        return targetLocaleDate;
    }

    private Tuple<Date, Date> getStartDateInMonthAndTheNextMonth(String month) {

        try {

            LocalDate targetLocaleDate = getStartDateInMonth(month);

            Date firstDay = Date.from(targetLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Date endDate = Date.from(targetLocaleDate.plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

            return new Tuple(firstDay, endDate);

        }catch (Exception ex){
            return  null;
        }
    }

    @Override
    public ResponseResult processGetRank(String month) throws ParseException {

        LocalDate targetLocaleDate = getStartDateInMonth(month);

        Date start = Date.from(targetLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Rank rank = rankRepository.findTopByRankTypeAndStartTimeEquals(RankType.MONTHLY, start);

        List<UserRankDTO> userRankDTOList = rankService.getListUserRank(rank.getId());

        return ResponseResult.newSuccessInstance(userRankDTOList);
    }
}
