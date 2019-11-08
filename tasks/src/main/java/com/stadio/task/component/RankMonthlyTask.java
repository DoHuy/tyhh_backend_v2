package com.stadio.task.component;

import com.stadio.model.documents.Rank;
import com.stadio.model.enu.RankType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.stadio.task.TaskSettings.*;
import static com.stadio.task.utils.AppConst.A_MONTH;

/**
 * Created by Andy on 02/28/2018.
 */
@Component
public class RankMonthlyTask extends BaseRanking {

    private Logger logger = LogManager.getLogger(RankMonthlyTask.class);

    @Scheduled(cron = "${every.minute}")
    public void reportRankingMonthly() {

        if (ENABLE_RANKING_MONTHLY) {
            Rank old = rankRepository.findTopByRankTypeOrderByCreatedDateDesc(RankType.MONTHLY);

            LocalDate createdLocal = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
            Date startTime = Date.from(createdLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

            if (old != null) {
                if (old.getStartTime().equals(startTime)) {
                    updateRankMonth(old);
                    return;
                }
            }

            logger.info("Start reportRankingMonthly {}", dateFormat.format(new Date()));
            Rank rank = new Rank();
            rank.setStartTime(startTime);
            Date endDate = Date.from(createdLocal.plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            rank.setEndTime(endDate);
            rank.setRankType(RankType.MONTHLY);
            rank.setCreatedDate(new Date());
            rank.setName("MONTHLY");
            rankRepository.save(rank);

            updateRankMonth(rank);
        }
    }

    private void updateRankMonth(Rank rank) {
        logger.info("> Search rank about: " + dateFormat.format(rank.getStartTime()) + " ~ " + dateFormat.format(rank.getEndTime()));
        List<Rank> rankDays = rankRepository.findByRankTypeAndCreatedDateBetween(RankType.DAILY, rank.getStartTime(), rank.getEndTime());
        this.groupAndReportFromDaily(rankDays, rank.getId());
    }
}
