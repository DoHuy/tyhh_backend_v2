package com.stadio.model.repository.main.custom;

import com.stadio.model.documents.CODOrder;
import com.stadio.model.dtos.cms.recharge.CODOrderSearchFormDTO;

import java.util.List;

public interface CODOrderRepositoryCustom{

    List<CODOrder> findWithFormSearch(CODOrderSearchFormDTO form);

    long countWithFormSearch(CODOrderSearchFormDTO form);

    void saveNew(CODOrder order);

}
