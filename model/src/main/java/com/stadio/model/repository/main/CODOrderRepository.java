package com.stadio.model.repository.main;

import com.stadio.model.documents.CODOrder;
import com.stadio.model.repository.main.custom.CODOrderRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface CODOrderRepository extends MongoRepository<CODOrder, String>, CODOrderRepositoryCustom {

    Page<CODOrder> findAllByUserIdIs(String userId, Pageable pageable);

    CODOrder findFirstByRechargeCardId(String rechargeCardId);

    int countByUserIdAndCreatedDateGreaterThan(String userId, Date date);

}
