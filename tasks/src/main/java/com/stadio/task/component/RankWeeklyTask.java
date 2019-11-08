package com.stadio.task.component;

import com.stadio.model.documents.Rank;
import com.stadio.model.enu.RankType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.stadio.task.TaskSettings.ENABLE_RANKING_WEEKLY;
import static com.stadio.task.utils.AppConst.*;

/**
 * Created by Andy on 02/28/2018.
 */
@Component
public class RankWeeklyTask extends BaseRanking
{
    private Logger logger = LogManager.getLogger(RankDailyTask.class);

    @Scheduled(cron = "${ranking.weekly}")
    public void reportRankingWeekly()
    {

        if (ENABLE_RANKING_WEEKLY)
        {
            logger.info("Start reportRankingWeekly {}", dateFormat.format(new Date()));
            Rank rank = new Rank();
            rank.setStartTime(new Date(System.currentTimeMillis() - A_WEEK));
            rank.setEndTime(new Date(System.currentTimeMillis()));
            rank.setRankType(RankType.WEEKLY);
            rank.setCreatedDate(new Date());
            rank.setName("WEEKLY");
            rankRepository.save(rank);

            logger.info("> Search rank about: " + dateFormat.format(rank.getStartTime()) + " ~ " + dateFormat.format(rank.getEndTime()));
            List<Rank> rankDayOfWeek = rankRepository.findByCreatedDateBetween(rank.getStartTime(), rank.getEndTime());
            this.groupAndReportFromDaily(rankDayOfWeek, rank.getId());

        }
    }
}
