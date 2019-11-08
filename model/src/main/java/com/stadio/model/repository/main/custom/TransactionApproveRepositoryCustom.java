package com.stadio.model.repository.main.custom;

import com.stadio.model.documents.TransactionApprove;
import com.stadio.model.dtos.cms.transaction.TransactionApproveFromSearchDTO;

import java.util.List;

public interface TransactionApproveRepositoryCustom {

    List<TransactionApprove> findWithFormSearch(TransactionApproveFromSearchDTO form);

    long countWithFormSearch(TransactionApproveFromSearchDTO form);

}
