package com.stadio.model.repository.main;

import com.stadio.model.documents.Notification;
import com.stadio.model.repository.main.custom.NotificationRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String>, NotificationRepositoryCustom
{

    List<Notification> findBySendTimeAfter(Date sendTime);

    Page<Notification> findBySendToOrSendToNull(String userId, Pageable pageable);

    Page<Notification> findBySendToIsNull(Pageable pageable);
}
