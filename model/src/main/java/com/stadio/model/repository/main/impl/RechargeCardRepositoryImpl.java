package com.stadio.model.repository.main.impl;

import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.RechargeCard;
import com.stadio.model.dtos.cms.recharge.RechargeCardSearchFormDTO;
import com.stadio.model.repository.main.custom.RechargeCardRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class RechargeCardRepositoryImpl implements RechargeCardRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<RechargeCard> findNoValueCards(Integer page, Integer pageSize) {

        Query query = new Query();
        query.addCriteria(Criteria.where("value").is(0));

        final Pageable pageableRequest = new PageRequest(page-1, pageSize);
        query.with(pageableRequest);
        return mongoTemplate.find(query, RechargeCard.class);
    }

    @Override
    public List<RechargeCard> findWithFormSearch(RechargeCardSearchFormDTO form) {
        return mongoTemplate.find(this.queryFromFormSearch(form), RechargeCard.class);
    }

    @Override
    public long countWithFormSearch(RechargeCardSearchFormDTO form) {
        return mongoTemplate.count(this.queryFromFormSearch(form), RechargeCard.class);
    }

    @Override
    public long sumRevenue() {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id_used").ne(null));

        List<RechargeCard> rechargeCards = mongoTemplate.find(query, RechargeCard.class);

        long sum = 0;
        for (RechargeCard card: rechargeCards) {
            sum += card.getValue();
        }
        return sum;
    }

    Query queryFromFormSearch(RechargeCardSearchFormDTO form) {

        Query query = new Query();

        //Value
        if (form.getValue() != 0) {
            query.addCriteria(Criteria.where("value").is(form.getValue()));
        } else if (form.getValueTo() != 0) {
            query.addCriteria(Criteria.where("value").lte(form.getValueTo()).gte(form.getValueFrom()));
        }

        if (form.isUsed()) {
            query.addCriteria(Criteria.where("user_id_used").ne(null));
        } else {
            query.addCriteria(Criteria.where("user_id_used").is(null));
        }

        query.addCriteria(Criteria.where("is_printed").is(form.isPrinted()));

        if (StringUtils.isNotNull(form.getSerial())) {
            query.addCriteria(Criteria.where("serial").regex(".*" + form.getSerial() +".*"));
        }

        //Page start with 0
        final Pageable pageableRequest = new PageRequest(form.getPage(), form.getPageSize());
        query.with(pageableRequest);
        return query;
    }
}
