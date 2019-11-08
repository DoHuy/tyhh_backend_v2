package com.stadio.task.component;

import com.stadio.model.documents.Rank;
import com.stadio.model.documents.UserExam;
import com.stadio.model.documents.UserPoint;
import com.stadio.model.enu.RankType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.stadio.task.TaskSettings.*;
import static com.stadio.task.utils.AppConst.*;

/**
 * Created by Andy on 02/26/2018.
 */
@Component
public class RankDailyTask extends BaseRanking {
    private Logger logger = LogManager.getLogger(RankDailyTask.class);

    @Scheduled(cron = "${every.minute}") // every minute for testing
    public void reportEveryMinute() {
        if (ENABLE_RANKING_MINUTE) {
            logger.info("reportEveryMinute {}", dateFormat.format(new Date()));

            Rank rank = new Rank();
            rank.setStartTime(new Date(System.currentTimeMillis() - 60000));
            rank.setEndTime(new Date());
            rank.setRankType(RankType.MINUTE);
            rank.setCreatedDate(new Date());
            rank.setName("TEST");
            rankRepository.save(rank);

            logger.info("Created rank with id = " + rank.getId());

            List<UserExam> userExamList = userExamRepository.findAll();

            this.groupAndReportFromSubmit(userExamList, rank.getId());
        }
    }

    @Scheduled(cron = "${ranking.daily}") //daily at midnight
    public void reportRankingDaily() {

        if (ENABLE_RANKING_DAILY) {
            logger.info("Start reportRankingDaily {}", dateFormat.format(new Date()));
            Rank rank = new Rank();
            rank.setStartTime(new Date(System.currentTimeMillis() - A_DAY));
            rank.setEndTime(new Date(System.currentTimeMillis()));
            rank.setRankType(RankType.DAILY);
            rank.setCreatedDate(new Date());
            rank.setName("DAILY");
            rankRepository.save(rank);

            List<UserPoint> userPointList = userPointRepository.findByCreatedDateBetween(rank.getStartTime(), rank.getEndTime());

            this.groupAndReportFromSubmitWithUserPoint(userPointList, rank.getId());
        }
    }

}
