package com.stadio.model.repository.main;

import com.stadio.model.documents.Rank;
import com.stadio.model.enu.RankType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by Andy on 01/20/2018.
 */
public interface RankRepository extends MongoRepository<Rank, String>
{
    List<Rank> findByCreatedDateBetween(Date startTime, Date endTime);

    List<Rank> findByRankTypeAndCreatedDateBetween(RankType rankType, Date startTime, Date endTime);

    Rank findTopByRankTypeOrderByCreatedDateDesc(RankType rankType);

    Rank findTopByRankTypeAndStartTimeEquals(RankType rankType, Date start);

    Rank findTopByRankTypeAndStartTimeAfter(RankType rankType, Date start);
}
