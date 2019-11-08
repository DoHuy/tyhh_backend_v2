package com.stadio.model.repository.main;

import com.stadio.model.documents.UserPoint;
import com.stadio.model.repository.main.custom.UserPointRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface UserPointRepository extends MongoRepository<UserPoint, String>, UserPointRepositoryCustom {

    List<UserPoint> findAllByUserIdAndCreatedDateBetweenOrderByCreatedDateAsc(String userId, Date start, Date end);

    List<UserPoint> findAllByUserIdAndCreatedDateBetweenOrderByCreatedDateDesc(String userId, Date start, Date end);

    List<UserPoint> findByCreatedDateBetween(Date start, Date end);
}
