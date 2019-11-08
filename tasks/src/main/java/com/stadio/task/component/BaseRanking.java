package com.stadio.task.component;

import com.stadio.model.documents.Rank;
import com.stadio.model.documents.UserExam;
import com.stadio.model.documents.UserPoint;
import com.stadio.model.documents.UserRank;
import com.stadio.model.repository.main.RankRepository;
import com.stadio.model.repository.main.UserExamRepository;
import com.stadio.model.repository.main.UserPointRepository;
import com.stadio.model.repository.main.UserRankRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Andy on 02/28/2018.
 */
public class BaseRanking
{
    @Autowired UserRankRepository userRankRepository;

    protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Autowired UserExamRepository userExamRepository;

    @Autowired RankRepository rankRepository;

    @Autowired
    UserPointRepository userPointRepository;

    private Logger logger = LogManager.getLogger(BaseRanking.class);

    public void groupAndReportFromSubmitWithUserPoint(List<UserPoint> userPoints, String rankId)
    {
        Map<String, List<UserPoint>> grouped = userPoints.stream()
                .collect(Collectors.groupingBy(UserPoint::getUserId));

        grouped.keySet().forEach(userId ->
        {
            UserRank userRank = new UserRank();
            int total = 0;
            for (UserPoint uk: grouped.get(userId))
            {
                total += uk.getPoint();
            }
            userRank.setPoint(total);
            userRank.setUserId(userId);
            userRank.setRankId(rankId);
            userRankRepository.save(userRank);
        });
    }

    public void groupAndReportFromSubmit(List<UserExam> userExamList, String rankId)
    {
        Map<String, List<UserExam>> grouped = userExamList.stream()
                .collect(Collectors.groupingBy(UserExam::getUserIdRef));

        grouped.keySet().forEach(userId ->
        {
            UserRank userRank = new UserRank();
            int total = 0;
            for (UserExam uk: grouped.get(userId))
            {
                total += uk.getCorrectNumber();
            }
            userRank.setPoint(total);
            userRank.setUserId(userId);
            userRank.setRankId(rankId);
            userRankRepository.save(userRank);
        });
    }

    public void groupAndReportFromDaily(List<Rank> items, final String rankId)
    {
        List<UserRank> rankGroupList = new ArrayList<>();

        //Scan rank theo tung ngay
        for (Rank dailyRank: items) {
            List<UserRank> rankList = userRankRepository.findByRankId(dailyRank.getId());
            rankGroupList.addAll(rankList);
        }

        logger.info("> There are " + rankGroupList.size() + " users join rank");

        Map<String, Double> grouped = rankGroupList.stream().collect(Collectors.groupingBy(UserRank::getUserId, Collectors.summingDouble(UserRank::getPoint)));
        grouped.keySet().forEach(userId -> {
            Double point = grouped.get(userId);
            logger.info("> userId:" + userId + ", point: " + point);
            UserRank userRank = userRankRepository.findTopByRankIdAndUserId(rankId, userId);
            if (userRank == null){
                userRank = new UserRank();
                userRank.setUserId(userId);
                userRank.setRankId(rankId);
            }
            userRank.setPoint(point);
            userRankRepository.save(userRank);
        });
    }
}
