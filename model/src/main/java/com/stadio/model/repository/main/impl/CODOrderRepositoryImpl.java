package com.stadio.model.repository.main.impl;

import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.CODOrder;
import com.stadio.model.documents.SequenceIndex;
import com.stadio.model.dtos.cms.recharge.CODOrderSearchFormDTO;
import com.stadio.model.dtos.cms.recharge.ExportRechargeSearchFormDTO;
import com.stadio.model.enu.SequenceKey;
import com.stadio.model.repository.main.custom.CODOrderRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class CODOrderRepositoryImpl implements CODOrderRepositoryCustom {


    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<CODOrder> findWithFormSearch(CODOrderSearchFormDTO form) {
        List<CODOrder> codOrders = mongoTemplate.find(this.queryWithFormSearch(form), CODOrder.class);
        return codOrders;
    }

    @Override
    public long countWithFormSearch(CODOrderSearchFormDTO form) {
        return mongoTemplate.count(this.queryWithFormSearch(form), CODOrder.class);
    }

    private Query queryWithFormSearch(CODOrderSearchFormDTO form) {
        Query query = new Query();

        if (StringUtils.isNotNull(form.getCode())) {
            query.addCriteria(Criteria.where("code").is(form.getCode()));
        }

        if (StringUtils.isNotNull(form.getUserCode())) {
            query.addCriteria(Criteria.where("user_code").regex(".*" + form.getUserCode() +".*"));
        }

        if (StringUtils.isNotNull(form.getUserPhone())) {
            query.addCriteria(Criteria.where("user_phone").regex(".*" + form.getUserPhone() +".*"));
        }

        if (StringUtils.isNotNull(form.getUserFullName())) {
            query.addCriteria(Criteria.where("user_full_name").regex(".*" + form.getUserFullName() +".*"));
        }

        if (StringUtils.isNotNull(form.getAddress())) {
            query.addCriteria(Criteria.where("address").regex(".*" + form.getAddress() +".*"));
        }

        if (form.getStatus() != null && StringUtils.isNotNull(form.getStatus().name())) {
            query.addCriteria(Criteria.where("status").is(form.getStatus()));
        }

        if (form.getValue() > 0) {
            query.addCriteria(Criteria.where("value").is(form.getValue()));
        }

        final Pageable pageableRequest = new PageRequest(form.getPage(), form.getPageSize());
        query.with(pageableRequest);

        // Sort Date tu ngay gan nhat den xa nhat
        query.with(new Sort(Sort.Direction.DESC, "created_date"));
        return query;
    }

    @Override
    public void saveNew(CODOrder order) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sequence_key").is(SequenceKey.COD.name()));

        SequenceIndex sequenceIndex = this.mongoTemplate.findOne(query, SequenceIndex.class);
        long idx = 1;

        if (sequenceIndex == null) {
            sequenceIndex = new SequenceIndex();
            sequenceIndex.setSequenceKey(SequenceKey.COD.name());
        } else {
            idx = sequenceIndex.getCurrentSequence() + 1;
        }
        sequenceIndex.setCurrentSequence(idx);
        mongoTemplate.save(sequenceIndex);

        order.setCode(String.valueOf(idx));
        mongoTemplate.save(order);
    }
}
