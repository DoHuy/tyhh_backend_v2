package com.stadio.model.repository.main.impl;

import com.stadio.common.define.Constant;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Notification;
import com.stadio.model.repository.main.custom.NotificationRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Notification> findNotificationByPage(Integer page) {

        if(page == null || page < 1)
            page = 1;

        Query query = new Query();

        final Pageable pageableRequest = new PageRequest(page-1, Constant.PAGE_SIZE);
        query.with(pageableRequest);
        query.with(new Sort(Sort.Direction.DESC, "created_date"));

        return mongoTemplate.find(query, Notification.class);
    }

    @Override
    public List<Notification> searchNotification(Integer page, Integer pageSize, Map<String, String> search)
    {
        Query query = new Query();
        search.keySet().forEach(key ->
        {
            if (StringUtils.isValid(search.get(key)))
            {
                query.addCriteria(Criteria.where(key).regex(search.get(key)));
            }
        });

        final Pageable pageableRequest = new PageRequest(page - 1, pageSize);
        query.with(pageableRequest);
        query.with(new Sort(Sort.Direction.DESC, "created_date"));

        return mongoTemplate.find(query, Notification.class);
    }
}
