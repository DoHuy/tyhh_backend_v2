package com.stadio.model.repository.main.impl;

import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.RechargeCardExport;
import com.stadio.model.dtos.cms.recharge.ExportRechargeSearchFormDTO;
import com.stadio.model.dtos.cms.recharge.UserRechargeActionSearchFormDTO;
import com.stadio.model.repository.main.custom.RechargeCardExportRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class RechargeCardExportRepositoryImpl implements RechargeCardExportRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<RechargeCardExport> findWithFormSearch(ExportRechargeSearchFormDTO form) {
        List<RechargeCardExport> rechargeCardExports = mongoTemplate.find(this.queryWithFormSearch(form), RechargeCardExport.class);
        return rechargeCardExports;
    }

    @Override
    public long countWithFormSearch(ExportRechargeSearchFormDTO form) {
        return mongoTemplate.count(this.queryWithFormSearch(form), RechargeCardExport.class);
    }

    private Query queryWithFormSearch(ExportRechargeSearchFormDTO form) {
        Query query = new Query();

        if (StringUtils.isNotNull(form.getManagerUsername())) {
            query.addCriteria(Criteria.where("manager_username").regex(".*" + form.getManagerUsername() +".*"));
        }

        if (StringUtils.isNotNull(form.getManagerUsername())) {
            query.addCriteria(Criteria.where("title").regex(".*" + form.getTitle() +".*"));
        }

        if (form.getCardValue() > 0) {
            query.addCriteria(Criteria.where("card_value").is(form.getCardValue()));
        }

        if (form.getCreateDate() != null) {
            query.addCriteria(Criteria.where("title").regex(".*" + form.getCreateDate() +".*"));
        }

        if (form.getQuantity() > 0) {
            query.addCriteria(Criteria.where("quantity").is(form.getQuantity()));
        }

        final Pageable pageableRequest = new PageRequest(form.getPage(), form.getPageSize());
        query.with(pageableRequest);

        // Sort Date tu ngay gan nhat den xa nhat
        query.with(new Sort(Sort.Direction.DESC, "created_date"));
        return query;
    }
}
