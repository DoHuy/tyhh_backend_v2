package com.stadio.model.repository.main;

import com.stadio.model.documents.UserRank;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import org.springframework.data.domain.*;
/**
 * Created by Andy on 01/20/2018.
 */
public interface UserRankRepository extends MongoRepository<UserRank, String>
{
    List<UserRank> findByRankId(String rankId);

    UserRank findTopByRankIdAndUserId(String rankId, String userId);

    Page<UserRank> findByRankIdOrderByPointDesc(String rankId, Pageable pageable);

    List<UserRank> findByRankIdOrderByPointDesc(String rankId);

    Long countAllByRankIdAndPointGreaterThanEqual(String rankId, Double point);

}
