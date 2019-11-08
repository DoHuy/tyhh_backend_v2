package com.stadio.model.repository.main.impl;

import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Device;
import com.stadio.model.repository.main.custom.DeviceRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 02/16/2018.
 */
public class DeviceRepositoryImpl implements DeviceRepositoryCustom
{

    @Autowired MongoTemplate mongoTemplate;

    @Override
    public List<Device> searchDevice(Integer page, Integer pageSize, Map<String, String> search)
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

        return mongoTemplate.find(query, Device.class);
    }
}
