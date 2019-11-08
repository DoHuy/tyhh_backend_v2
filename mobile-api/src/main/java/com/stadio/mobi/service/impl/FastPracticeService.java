package com.stadio.mobi.service.impl;

import com.stadio.common.enu.FolderName;
import com.stadio.common.service.impl.StorageService;
import com.stadio.common.utils.ResponseCode;
import com.stadio.mobi.controllers.PracticeController;
import com.stadio.mobi.dtos.FastPracticeResponseDTO;
import com.stadio.mobi.dtos.UserPointFormDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IFastPracticeService;
import com.stadio.mobi.service.IUserPointService;
import com.stadio.model.documents.*;
import com.hoc68.users.documents.User;
import com.stadio.model.enu.ActionBase;
import com.stadio.model.enu.ConfigKey;
import com.stadio.model.enu.TransactionType;
import com.stadio.model.enu.UserPointType;
import com.stadio.model.model.UserAnswer;
import com.stadio.model.repository.main.ConfigRepository;
import com.stadio.model.repository.main.FastPracticeRepository;
import com.stadio.model.repository.main.QuestionRepository;
import com.stadio.model.repository.main.TransactionRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created by Andy on 03/02/2018.
 */
@Service
public class FastPracticeService extends BaseService implements IFastPracticeService
{

    //private static final int MAX_FAST_PRACTICE = 5;

    @Autowired QuestionRepository questionRepository;

    @Autowired FastPracticeRepository fastPracticeRepository;

    @Autowired StorageService storageService;

    @Autowired UserService userService;

    @Autowired ConfigRepository configRepository;

    @Autowired TransactionService transactionService;

    @Autowired
    IUserPointService userPointService;

    @Autowired
    TransactionRepository transactionRepository;

    private Logger logger = LogManager.getLogger(FastPracticeService.class);

    private ZonedDateTime getToday(){
        return ZonedDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT, ZoneId.systemDefault());
    }

    private ZonedDateTime getTomorrow(){
        return getToday().plusDays(1);
    }

    @Override
    public ResponseResult processGetRandomQuestion(String token, String sessionId)
    {
        User user = this.getUserRequesting();
        FastPractice fastPractice = fastPracticeRepository.findOne(sessionId);
        ZonedDateTime today = getToday();
        ZonedDateTime tomorrow = getTomorrow();

        int totalInDay = fastPracticeRepository.countByUserIdAndCreatedDateBetween(user.getId(), Date.from(today.toInstant()), Date.from(tomorrow.toInstant()));

        if (!StringUtils.isNotBlank(sessionId)){
            totalInDay += 1;
        }

        int maxFastPractice = getMaxPracticeTimes();

        if (fastPractice != null && fastPractice.getEndTime() == null)
        {
            return actionFastPractice(fastPractice, user.getId());
        }

        logger.info("[" + user.getCode() + "] (" + today.toInstant() + "-" + tomorrow.toInstant() + ") Checking totalInDay: " + totalInDay + " with maxFastPractice:" + maxFastPractice);

        if (totalInDay > maxFastPractice || (fastPractice != null && fastPractice.getEndTime() != null))
        {
            Map<String, String> action = new HashMap<>();
            try
            {
                String paymentPractice = MvcUriComponentsBuilder.fromMethodName(PracticeController.class, "fastPracticePayment", "")
                        .host(host).port(port)
                        .build().toString();

                action.put(ActionBase.PAYMENT_FAST_PRACTICE, paymentPractice);
            }
            catch (Exception e)
            {
                logger.error("Payment fast practice exception: ", e);
            }

            return ResponseResult.newInstance(ResponseCode.OUT_OF_MOVE, this.getMessage("practice.limited"), action);
        }


        return actionFastPractice(fastPractice, user.getId());

    }

    private int getMaxPracticeTimes()
    {
        int maxFastPractice = 5;
        try
        {
            Config config = configRepository.findConfigByKey(ConfigKey.MAX_FAST_PRACTICE.name());
            maxFastPractice = Integer.parseInt(config.getValue());
        }
        catch (Exception e)
        {
            logger.error("maxFastPractice Exception: ", e);
        }
        return maxFastPractice;
    }

    private ResponseResult actionFastPractice(FastPractice fastPractice, String userId)
    {
        //FastPractice fastPractice = fastPracticeRepository.findOne(sessionId);
        if (fastPractice == null)
        {
            fastPractice = new FastPractice();
            fastPractice.setStartTime(new Date());
            fastPractice.setNumberOfQuestion(0);
            fastPractice.setUserId(userId);
            fastPracticeRepository.save(fastPractice);
        }

        long count = questionRepository.count();
        int limit = 3;
        Random random = new Random();
        int idx = random.nextInt(Integer.parseInt(String.valueOf(count / limit)));
        List<Question> questionList = questionRepository.findQuestionByPage(idx, limit);

        FastPracticeResponseDTO responseDTO = new FastPracticeResponseDTO();
        responseDTO.setSessionId(fastPractice.getId());
        String nextUrl = MvcUriComponentsBuilder
                .fromMethodName(PracticeController.class, "fastPracticeNormal", null, fastPractice.getId())
                .host(host).port(port)
                .build().toString();
        responseDTO.getActions().put(ActionBase.NEXT_QUESTION, nextUrl);
        responseDTO.setQuestionList(questionList);

        return ResponseResult.newSuccessInstance(responseDTO);
    }

    @Override
    public ResponseResult processSubmitQuestion(String token, String sessionId, List<UserAnswer> answers)
    {
        User user = this.getUserRequesting();

        FastPractice fastPractice = fastPracticeRepository.findOne(sessionId);
        if (fastPractice == null)
        {
            return ResponseResult.newErrorInstance("01", this.getMessage("practice.session.not_exist"));
        }

        Path p = storageService.getLocation(FolderName.USERS)
                .resolve("save_by_feature")
                .resolve("fast_practice")
                .resolve(user.getId());

        if (!p.getParent().toFile().exists()) {
            try {
                Files.createDirectories(p);
            } catch (IOException e) {
                logger.error("createDirectories exception: ", e);
            }
        }

        ResponseResult result = new ResponseResult();
        try
        {
            ZonedDateTime today = getToday();
            ZonedDateTime tomorrow = getTomorrow();

            if (!p.toFile().exists())
            {
                Files.createDirectories(p);
            }
            String fname = sessionId + "_" + System.currentTimeMillis();
            File f = p.resolve(fname).toFile();
            mapper.writeValue(f, answers);
            fastPractice.setPathResult(f.getPath());
            fastPractice.setNumberOfQuestion(answers.size());
            fastPractice.setEndTime(new Date());
            fastPracticeRepository.save(fastPractice);

            int maxFastPractice = getMaxPracticeTimes();
            int totalInDay = fastPracticeRepository.countByUserIdAndCreatedDateBetween(user.getId(), Date.from(today.toInstant()), Date.from(tomorrow.toInstant()));
            if (!StringUtils.isNotBlank(sessionId)){
                totalInDay += 1;
            }
            int offset = maxFastPractice - totalInDay;

            if (totalInDay <= maxFastPractice){
                updateUserPoint(user.getId(), answers, totalInDay);
            }

            logger.info("[" + user.getCode() + "] (" + today.toInstant() + "-" + tomorrow.toInstant() + ")Remaining: "  + offset);
            Map<String, Object> body = new HashMap<>();
            body.put(ActionBase.FAST_PRACTICE_REMAIN, offset);

            result.setErrorCode(ResponseCode.SUCCESS);
            result.setMessage("SUCCESS");
            result.setData(body);
        }
        catch (Exception e)
        {
            result.setErrorCode("02");
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Async
    public void updateUserPoint(String userId, List<UserAnswer> answers, Integer turn){
        Double total = 0.0;

        for(UserAnswer userAnswer : answers){
            Question question = questionRepository.findOne(userAnswer.getQuestionId());
            if (question.getCorrectAnswer().equals(userAnswer.getCode())){
                total = total + 1;
            }
        }

        if (total == 0){
            return;
        }

        UserPointFormDTO userPointFormDTO = new UserPointFormDTO();

        userPointFormDTO.setSourceType(UserPointType.FAST_PRACTICE);

        userPointFormDTO.setSourceName("Luyện nhanh lần " + turn);

        userPointFormDTO.setUserId(userId);

        userPointFormDTO.setPoint(total);

        userPointService.createUserPoint(userPointFormDTO);
    }

    @Override
    public ResponseResult processGetRandomQuestionWithPayment(String token)
    {
        User user = this.getUserRequesting();
        Config config = configRepository.findConfigByKey(ConfigKey.PRICE_FAST_PRACTICE.name());

        int priceDefault = 1000;
        if (config != null && StringUtils.isNumeric(config.getValue()))
        {
            Integer value = Integer.parseInt(config.getValue());
            if (value > 0)
            {
                priceDefault = value;
            }
        }

        long balance = user.getBalance();
        if (balance < priceDefault)
        {
            return ResponseResult.newErrorInstance(ResponseCode.BALANCE, this.getMessage("balance.not.enough"));
        }

        Transaction transaction = transactionService.processDeduction(token, priceDefault, TransactionType.FAST_PRACTICE);
        transaction.setTransContent("Mua thêm lượt luyện nhanh");
        transactionRepository.createNew(transaction);

        return this.actionFastPractice(null, user.getId());
    }
}
