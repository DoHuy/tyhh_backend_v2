package com.stadio.model.repository.main;

import com.stadio.model.documents.Transaction;
import com.stadio.model.enu.TransactionType;
import com.stadio.model.repository.main.custom.TransactionRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;


/**
 * Created by Andy on 11/10/2017.
 */
public interface TransactionRepository extends MongoRepository<Transaction, String>, TransactionRepositoryCustom
{
    List<Transaction> findByCreatedDateBetween(Date startDate, Date endDate);

    long countByUserIdRef(String userId);

    @Query("{ 'user_id_ref' : ?0 }")
    Page<Transaction> findByLastest(String userId, Pageable pageable);

    List<Transaction> findAllByUserIdRefAndTransTypeOrderByCreatedDateDesc(String userId, TransactionType type);

}
