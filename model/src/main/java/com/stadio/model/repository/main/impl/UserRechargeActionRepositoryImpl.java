package com.stadio.model.repository.main.impl;

import com.stadio.common.define.Constant;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.UserRechargeAction;
import com.stadio.model.dtos.cms.recharge.UserRechargeActionSearchFormDTO;
import com.stadio.model.repository.main.custom.UserRechargeActionRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;

public class UserRechargeActionRepositoryImpl implements UserRechargeActionRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public long countLastRechargeFailureIn24H(String userId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId));
        query.addCriteria(Criteria.where("created_date").gte(new Date(System.currentTimeMillis() - 1L * 24 * 3600 * 1000)));

        final Pageable pageableRequest = new PageRequest(0, Constant.MAX_RECHARGE_DDOS_TIME_PER_DAY);
        query.with(pageableRequest);

        // Sort Date tu ngay gan nhat den xa nhat
        query.with(new Sort(Sort.Direction.DESC, "created_date"));

        List<UserRechargeAction> userRechargeActions = mongoTemplate.find(query, UserRechargeAction.class);

        if (userRechargeActions == null || userRechargeActions.size() == 0) {
            return 0;
        }

        for (int i = 0; i < userRechargeActions.size(); i++) {
            UserRechargeAction action = userRechargeActions.get(i);
            if (action.isSuccess() || action.isShouldLockUser() == true) {
                return i;
            }
        }
        return userRechargeActions.size();
    }

    @Override
    public List<UserRechargeAction> findWithFormSearch(UserRechargeActionSearchFormDTO form) {
        List<UserRechargeAction> userRechargeActions = mongoTemplate.find(this.queryWithFormSearch(form), UserRechargeAction.class);
        return userRechargeActions;
    }

    @Override
    public long countWithFormSearch(UserRechargeActionSearchFormDTO form) {
        return mongoTemplate.count(this.queryWithFormSearch(form), UserRechargeAction.class);
    }

    private Query queryWithFormSearch(UserRechargeActionSearchFormDTO form) {
        Query query = new Query();

        if (StringUtils.isNotNull(form.getUserCode())) {
            query.addCriteria(Criteria.where("user_code").is(form.getUserCode()));
        }

        if (StringUtils.isNotNull(form.getCardNumber())) {
            query.addCriteria(Criteria.where("card_number").regex(".*" + form.getCardNumber() +".*"));
        }

        if (StringUtils.isNotNull(form.getSerial())) {
            query.addCriteria(Criteria.where("serial").regex(".*" + form.getSerial() +".*"));
        }

        if (form.getIsSuccess() < 2) {
            query.addCriteria(Criteria.where("is_success").is(form.getIsSuccess() == 1));
        }

        final Pageable pageableRequest = new PageRequest(form.getPage(), form.getPageSize());
        query.with(pageableRequest);

        // Sort Date tu ngay gan nhat den xa nhat
        query.with(new Sort(Sort.Direction.DESC, "created_date"));
        return query;
    }
}
