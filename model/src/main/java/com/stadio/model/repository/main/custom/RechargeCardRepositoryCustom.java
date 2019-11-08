package com.stadio.model.repository.main.custom;

import com.stadio.model.documents.RechargeCard;
import com.stadio.model.dtos.cms.recharge.RechargeCardSearchFormDTO;

import java.util.List;

public interface RechargeCardRepositoryCustom {

    List<RechargeCard> findNoValueCards(Integer page, Integer pageSize);

    List<RechargeCard> findWithFormSearch(RechargeCardSearchFormDTO form);

    long countWithFormSearch(RechargeCardSearchFormDTO form);

    long sumRevenue();

}
