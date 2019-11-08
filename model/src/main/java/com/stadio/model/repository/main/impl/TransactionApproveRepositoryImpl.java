package com.stadio.model.repository.main.impl;

import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.TransactionApprove;
import com.stadio.model.dtos.cms.transaction.TransactionApproveFromSearchDTO;
import com.stadio.model.repository.main.custom.TransactionApproveRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class TransactionApproveRepositoryImpl implements TransactionApproveRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<TransactionApprove> findWithFormSearch(TransactionApproveFromSearchDTO form) {
        return mongoTemplate.find(this.queryWithFormSearch(form), TransactionApprove.class);
    }

    @Override
    public long countWithFormSearch(TransactionApproveFromSearchDTO form) {
        return mongoTemplate.count(this.queryWithFormSearch(form), TransactionApprove.class);
    }

    private Query queryWithFormSearch(TransactionApproveFromSearchDTO form) {
        Query query = new Query();

        if (form.getStatus() > -1) {
            query.addCriteria(Criteria.where("status").is(form.getStatus()));
        }

        if (StringUtils.isNotNull(form.getCreatedBy())) {
            query.addCriteria(Criteria.where("created_by").regex(".*" + form.getCreatedBy() +".*"));
        }

        if (StringUtils.isNotNull(form.getUserCode())) {
            query.addCriteria(Criteria.where("user_code_ref").regex(".*" + form.getUserCode() +".*"));
        }

        if (StringUtils.isNotNull(form.getTransactionContent())) {
            query.addCriteria(Criteria.where("trans_content").regex(".*" + form.getTransactionContent() +".*"));
        }

        if (form.getValue() > 0) {
            query.addCriteria(Criteria.where("amount").is(form.getValue()));
        }

        final Pageable pageableRequest = new PageRequest(form.getPage(), form.getPageSize());
        query.with(pageableRequest);

        // Sort Date tu ngay gan nhat den xa nhat
        query.with(new Sort(Sort.Direction.DESC, "created_date"));
        return query;
    }

}
