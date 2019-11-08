package com.stadio.cms.service.impl;

import com.hoc68.users.documents.User;
import com.mongodb.DBObject;
import com.stadio.cms.dtos.UserPointFormDTO;
import com.stadio.cms.dtos.UserRankDTO;
import com.stadio.cms.dtos.user.UserPointDTO;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IUserPointService;
import com.stadio.common.utils.ResponseCode;
import com.stadio.common.utils.StringUtils;
import com.stadio.common.utils.Tuple;
import com.stadio.model.documents.Rank;
import com.stadio.model.documents.UserPoint;
import com.stadio.model.documents.UserRank;
import com.stadio.model.dtos.cms.UserPointMongoDTO;
import com.stadio.model.enu.RankType;
import com.stadio.model.repository.main.RankRepository;
import com.stadio.model.repository.main.UserPointRepository;
import com.stadio.model.repository.main.UserRankRepository;
import com.stadio.model.repository.user.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserPointService extends BaseService implements IUserPointService {

    @Autowired
    UserPointRepository userPointRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RankRepository rankRepository;

    @Autowired
    UserRankRepository userRankRepository;

    private Logger logger = LogManager.getLogger(UserPointService.class);

    private static final SimpleDateFormat fm = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public UserPoint createUserPoint(UserPointFormDTO userPointFormDTO) {

        if (userPointFormDTO != null && userPointFormDTO.getPoint() > 0) {
            UserPoint userPoint = new UserPoint();
            findAndUpdateUserRank(userPointFormDTO.getUserId(), userPointFormDTO.getPoint());
            userPoint.setUserId(userPointFormDTO.getUserId());
            userPoint.setPoint(userPointFormDTO.getPoint());
            userPoint.setSourceId(userPointFormDTO.getSourceId());
            userPoint.setSourceName(userPointFormDTO.getSourceName());
            userPoint.setSourceType(userPointFormDTO.getSourceType());
            userPointRepository.save(userPoint);
            return userPoint;
        } else {
            return null;
        }
    }

    @Async
    public void findAndUpdateUserRank(String userId, Double point) {

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

    private void updateUserRank(String userId, Double point, String rankId) {
        UserRank userRank = userRankRepository.findTopByRankIdAndUserId(rankId, userId);

        if (userRank == null) {
            userRank = new UserRank();
            userRank.setUserId(userId);
            userRank.setRankId(rankId);
        }

        userRank.setPoint(userRank.getPoint() + point);
        userRankRepository.save(userRank);
    }

    @Override
    public ResponseResult processCreateUserPoint(UserPointFormDTO userPointFormDTO) {
        UserPoint userPoint = createUserPoint(userPointFormDTO);
        if (userPoint != null) {
            return ResponseResult.newSuccessInstance(userPoint);
        } else {
            return ResponseResult.newErrorInstance(ResponseCode.MISSING_PARAM, getMessage("userPoint.invalid.point"));
        }
    }

    @Override
    public ResponseResult processGetUserPoint(String month, String userId) {

        Tuple<Date, Date> tupleDate = getStartDayInMonth(month);

        if (tupleDate != null) {
            List<UserPoint> pointList = userPointRepository.findAllByUserIdAndCreatedDateBetweenOrderByCreatedDateAsc(userId, tupleDate.x, tupleDate.y);
            return ResponseResult.newSuccessInstance(pointList);
        } else {
            return ResponseResult.newErrorInstance(ResponseCode.INVALID_VALUE, getMessage("userPoint.invalid.date.time"));
        }
    }

    private Tuple<Date, Date> getStartDayInMonth(String month) {

        try {
            Date targetDate = fm.parse(month);

            LocalDate targetLocaleDate = targetDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);

            Date firstDay = Date.from(targetLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Date endDate = Date.from(targetLocaleDate.plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

            return new Tuple(firstDay, endDate);

        } catch (Exception ex) {
            return null;
        }
    }

    private LocalDate getStartDateInMonth(String month) throws ParseException {
        Date targetDate = new Date();

        if (StringUtils.isNotNull(month)) {
            targetDate = fm.parse(month);
        }

        LocalDate targetLocaleDate = targetDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return targetLocaleDate;
    }

    @Override
    public ResponseResult processGetRank(String month) throws ParseException {

        LocalDate targetLocaleDate = getStartDateInMonth(month);

        Date start = Date.from(targetLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        cal.set(Calendar.DAY_OF_MONTH, 0);

        logger.info("processGetRank.start: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cal.getTime()));

        Rank rank = rankRepository.findTopByRankTypeAndStartTimeAfter(RankType.MONTHLY, cal.getTime());
        List<UserRankDTO> userRankDTOList = new ArrayList<>();
        if (rank != null) {
            userRankDTOList = getListUserRank(rank.getId());
            logger.info("userRankDTOList.size:" + userRankDTOList.size());
        }
        return ResponseResult.newSuccessInstance(userRankDTOList);
    }

    private List<UserRankDTO> getListUserRank(String rankId) {
        List<UserRankDTO> userRankDTOList = new ArrayList<>();

        PageRequest request = new PageRequest(0, 100, new Sort(Sort.Direction.DESC, "point"));
        List<UserRank> userRankList = userRankRepository.findByRankIdOrderByPointDesc(rankId, request).getContent();

        int position = 1;

        for (UserRank userRank : userRankList) {
            User user = userRepository.findOne(userRank.getUserId());
            if (user != null) {
                UserRankDTO userRankDTO = new UserRankDTO();
                userRankDTO.setId(user.getId());
                userRankDTO.setFullName(user.getFullName());
                userRankDTO.setAvatar(user.getAvatar());
                userRankDTO.setPoint(userRank.getPoint());
                userRankDTO.setPosition(position);
                userRankDTO.setUsername(user.getUsername());
                userRankDTOList.add(userRankDTO);
                position++;
            }
        }

        return userRankDTOList;
    }
}
