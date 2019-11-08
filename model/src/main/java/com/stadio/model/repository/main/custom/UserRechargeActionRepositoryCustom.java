package com.stadio.model.repository.main.custom;

import com.stadio.model.documents.UserRechargeAction;
import com.stadio.model.dtos.cms.recharge.UserRechargeActionSearchFormDTO;

import java.util.List;

public interface UserRechargeActionRepositoryCustom {

    long countLastRechargeFailureIn24H(String userId);

    List<UserRechargeAction> findWithFormSearch(UserRechargeActionSearchFormDTO form);

    long countWithFormSearch(UserRechargeActionSearchFormDTO form);

}
