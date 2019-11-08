package com.stadio.model.repository.main.custom;

import com.stadio.model.documents.RechargeCardExport;
import com.stadio.model.dtos.cms.recharge.ExportRechargeSearchFormDTO;

import java.util.List;

public interface RechargeCardExportRepositoryCustom {

    List<RechargeCardExport> findWithFormSearch(ExportRechargeSearchFormDTO form);

    long countWithFormSearch(ExportRechargeSearchFormDTO form);

}
