package com.stadio.model.repository.main.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Transaction;
import com.stadio.model.dtos.cms.TransactionAmountGroupByTimeDTO;
import com.stadio.model.dtos.cms.TransactionFormDTO;
import com.stadio.model.repository.main.custom.TransactionRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TransactionRepositoryImpl implements TransactionRepositoryCustom {

    private static final SimpleDateFormat fm = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Transaction> search(TransactionFormDTO transactionFormDTO,String userId, Integer page, Integer pageSize) {
        String fromDate = transactionFormDTO.getFromDate();
        String toDate = transactionFormDTO.getToDate();

        Query query = new Query();

        if(StringUtils.isValid(toDate)&& StringUtils.isValid(toDate)){
            try{
                Date startDate = fm.parse(fromDate);
                Date endDate = fm.parse(toDate);
                query.addCriteria(Criteria.where("createdDate").gte(startDate).lte(endDate));
            }catch (Exception e){}
        }

        if(StringUtils.isValid(userId)){
            query.addCriteria(Criteria.where("userIdRef").is(userId));
        }

        final Pageable pageableRequest = new PageRequest(page-1, pageSize,new Sort(Sort.Direction.DESC, "created_time"));
        query.with(pageableRequest);
        return mongoTemplate.find(query, Transaction.class);
    }

    @Override
    public long countSearch(TransactionFormDTO transactionFormDTO,String userId, Integer page, Integer pageSize) {
        String fromDate = transactionFormDTO.getFromDate();
        String toDate = transactionFormDTO.getToDate();

        Query query = new Query();

        if(StringUtils.isValid(toDate)&& StringUtils.isValid(toDate)){
            try{
                Date startDate = fm.parse(fromDate);
                Date endDate = fm.parse(toDate);
                query.addCriteria(Criteria.where("createdDate").gte(startDate).lte(endDate));
            }catch (Exception e){}
        }

        if(StringUtils.isValid(userId)){
            query.addCriteria(Criteria.where("userIdRef").is(userId));
        }

        return mongoTemplate.count(query, Transaction.class);
    }

    @Override
    public List findTransactionsByPage(String userId, Integer page, Integer pageSize) {

        if (page > 0) {
            page--;
        }

        Query query = new Query();
        final Pageable pageableRequest = new PageRequest(page, pageSize);
        query.with(pageableRequest);
        query.addCriteria(Criteria.where("user_id_ref").is(userId));
        query.with(new Sort(Sort.Direction.ASC, "created_date"));

        return mongoTemplate.find(query, Transaction.class);
    }

    @Override
    public void createNew(Transaction transaction) {
        transaction.setTransTypeInt(transaction.getTransType().toInt());

        Date createDate = new Date();
        transaction.setCreatedDate(createDate);

        DateFormat df = new SimpleDateFormat("ddMMyy");
        df.setTimeZone(TimeZone.getTimeZone("GMT+7:00"));
        String strDate = df.format(new Date());
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "id"));
        query.limit(1);

        Transaction lastTransaction = null;
        String strDateLastTranasaction = null;
        List<Transaction> lastTransactionList = mongoTemplate.find(query, Transaction.class);
        if (lastTransactionList != null && lastTransactionList.size() > 0) {
            lastTransaction = lastTransactionList.get(0);
            strDateLastTranasaction = df.format(lastTransaction.getCreatedDate());
        }

        int tranOrder = 0;
        if (strDate.equals(strDateLastTranasaction)) {
            tranOrder = lastTransaction.getTransOrderInDay() + 1;
        }
        transaction.setTransOrderInDay(tranOrder);
        transaction.setTransNo(strDate + "." + String.valueOf(transaction.getTransTypeInt())+ "." +
                String.valueOf(tranOrder));

        mongoTemplate.save(transaction);
    }

    @Override
    public Iterable<DBObject> groupTransactionRechargeByDay(int month, int year) {

        BasicDBObject basicDBObject = new BasicDBObject();

        TypedAggregation<Transaction> agg = Aggregation.newAggregation(Transaction.class,
                Aggregation.match(Criteria.where("amount").gt(0)),
                Aggregation.project("createdDate","amount")
                        .andExpression("year(createdDate)").as("year")
                        .andExpression("month(createdDate)").as("month")
                        .andExpression("dayOfMonth(createdDate)").as("day"),
                Aggregation.match(Criteria.where("year").is(year)),
                Aggregation.match(Criteria.where("month").is(month)),
//                Aggregation.group(Aggregation.fields().and("year").and("month").and("day")).sum("amount").as("amount")
                Aggregation.group("day").sum("amount").as("amount")
        ).withOptions(new AggregationOptions(false,false,basicDBObject));
        AggregationResults<TransactionAmountGroupByTimeDTO> result = mongoTemplate.aggregate(agg,TransactionAmountGroupByTimeDTO.class);

        DBObject dbObjectResult = result.getRawResults();
        BasicDBObject cursor =  ((BasicDBObject)dbObjectResult.get("cursor"));
        Iterable<DBObject> firstBatch = (Iterable)cursor.get("firstBatch");

        return firstBatch;
    }

    @Override
    public Iterable<DBObject> groupTransactionBuyByDay(int month, int year) {

        BasicDBObject basicDBObject = new BasicDBObject();

        TypedAggregation<Transaction> agg = Aggregation.newAggregation(Transaction.class,
                Aggregation.match(Criteria.where("amount").lt(0)),
                Aggregation.project("createdDate","amount")
                        .andExpression("year(createdDate)").as("year")
                        .andExpression("month(createdDate)").as("month")
                        .andExpression("dayOfMonth(createdDate)").as("day"),
                Aggregation.match(Criteria.where("year").is(year)),
                Aggregation.match(Criteria.where("month").is(month)),
                Aggregation.group("day").sum("amount").as("amount")
        ).withOptions(new AggregationOptions(false,false,basicDBObject));
        AggregationResults<TransactionAmountGroupByTimeDTO> result = mongoTemplate.aggregate(agg,TransactionAmountGroupByTimeDTO.class);

        DBObject dbObjectResult = result.getRawResults();
        BasicDBObject cursor =  ((BasicDBObject)dbObjectResult.get("cursor"));
        Iterable<DBObject> firstBatch = (Iterable)cursor.get("firstBatch");

        return firstBatch;
    }

    @Override
    public Iterable<DBObject> groupTransactionRechargeByMonth(int year) {

        BasicDBObject basicDBObject = new BasicDBObject();

        TypedAggregation<Transaction> agg = Aggregation.newAggregation(Transaction.class,
                Aggregation.match(Criteria.where("amount").gt(0)),
                Aggregation.project("createdDate","amount")
                        .andExpression("year(createdDate)").as("year")
                        .andExpression("month(createdDate)").as("month"),
                Aggregation.match(Criteria.where("year").is(year)),
                Aggregation.group("month").sum("amount").as("amount")
        ).withOptions(new AggregationOptions(false,false,basicDBObject));
        AggregationResults<TransactionAmountGroupByTimeDTO> result = mongoTemplate.aggregate(agg,TransactionAmountGroupByTimeDTO.class);

        DBObject dbObjectResult = result.getRawResults();
        BasicDBObject cursor =  ((BasicDBObject)dbObjectResult.get("cursor"));
        Iterable<DBObject> firstBatch = (Iterable)cursor.get("firstBatch");

        return firstBatch;
    }

    @Override
    public Iterable<DBObject> groupTransactionBuyByMonth(int year) {
        BasicDBObject basicDBObject = new BasicDBObject();

        TypedAggregation<Transaction> agg = Aggregation.newAggregation(Transaction.class,
                Aggregation.match(Criteria.where("amount").lt(0)),
                Aggregation.project("createdDate","amount")
                        .andExpression("year(createdDate)").as("year")
                        .andExpression("month(createdDate)").as("month"),
                Aggregation.match(Criteria.where("year").is(year)),
                Aggregation.group("month").sum("amount").as("amount")
        ).withOptions(new AggregationOptions(false,false,basicDBObject));
        AggregationResults<TransactionAmountGroupByTimeDTO> result = mongoTemplate.aggregate(agg,TransactionAmountGroupByTimeDTO.class);

        DBObject dbObjectResult = result.getRawResults();
        BasicDBObject cursor =  ((BasicDBObject)dbObjectResult.get("cursor"));
        Iterable<DBObject> firstBatch = (Iterable)cursor.get("firstBatch");

        return firstBatch;
    }

    @Override
    public Iterable<DBObject> groupTransactionRechargeByYear() {
        BasicDBObject basicDBObject = new BasicDBObject();

        TypedAggregation<Transaction> agg = Aggregation.newAggregation(Transaction.class,
                Aggregation.match(Criteria.where("amount").gt(0)),
                Aggregation.project("createdDate","amount")
                        .andExpression("year(createdDate)").as("year"),
                Aggregation.group("year").sum("amount").as("amount")
        ).withOptions(new AggregationOptions(false,false,basicDBObject));
        AggregationResults<TransactionAmountGroupByTimeDTO> result = mongoTemplate.aggregate(agg,TransactionAmountGroupByTimeDTO.class);

        DBObject dbObjectResult = result.getRawResults();
        BasicDBObject cursor =  ((BasicDBObject)dbObjectResult.get("cursor"));
        Iterable<DBObject> firstBatch = (Iterable)cursor.get("firstBatch");

        return firstBatch;
    }

    @Override
    public Iterable<DBObject> groupTransactionBuyByYear() {
        BasicDBObject basicDBObject = new BasicDBObject();

        TypedAggregation<Transaction> agg = Aggregation.newAggregation(Transaction.class,
                Aggregation.match(Criteria.where("amount").lt(0)),
                Aggregation.project("createdDate","amount")
                        .andExpression("year(createdDate)").as("year"),
                Aggregation.group("year").sum("amount").as("amount")
        ).withOptions(new AggregationOptions(false,false,basicDBObject));
        AggregationResults<TransactionAmountGroupByTimeDTO> result = mongoTemplate.aggregate(agg,TransactionAmountGroupByTimeDTO.class);

        DBObject dbObjectResult = result.getRawResults();
        BasicDBObject cursor =  ((BasicDBObject)dbObjectResult.get("cursor"));
        Iterable<DBObject> firstBatch = (Iterable)cursor.get("firstBatch");

        return firstBatch;
    }
}
