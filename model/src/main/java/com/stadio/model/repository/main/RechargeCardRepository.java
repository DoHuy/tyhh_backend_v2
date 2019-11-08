package com.stadio.model.repository.main;


import com.stadio.model.documents.RechargeCard;
import com.stadio.model.repository.main.custom.RechargeCardRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RechargeCardRepository extends MongoRepository<RechargeCard, String>, RechargeCardRepositoryCustom {

//    RechargeCard findFirstByCardNumberSuffix(long suffix);

    RechargeCard findFirstByCardNumberAndIsEnableIsTrue(String cardNumber);

    RechargeCard findFirstByCardNumber(String cardNumber);

    RechargeCard findFirstBySerialAndIsEnable(String serial, Boolean enable);

    RechargeCard findFirstBySerial(String serial);

    List<RechargeCard> findAllByValueIsAndIsPrintedIs(long value, boolean printed, Pageable pageable);

    long countByValueIs(long value);

    int countByValueIsAndIsPrinted(long value, boolean printed);

    long countByValueIsGreaterThan(long value);

    long countByIsPrinted(boolean printed);

    long countByUserIdUsedIsNotNull();

}
