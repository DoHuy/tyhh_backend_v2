package com.stadio.model.redisUtils.impl;

import com.stadio.common.utils.JsonUtils;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Exam;
import com.stadio.model.documents.ExamHot;
import com.stadio.model.dtos.mobility.ExamItemDTO;
import com.stadio.model.enu.TopType;
import com.stadio.model.redisUtils.HotExamRedisRepository;
import com.stadio.model.redisUtils.RedisConst;
import com.stadio.model.redisUtils.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class HotExamRedisRepositoryImpl  implements HotExamRedisRepository{

    @Autowired
    RedisRepository redisRepository;

    @Override
    public void processPutHotExam(ExamHot examHot, Exam exam) {
        redisRepository.select(RedisConst.DB_EXAM);

        String key = examHot.getTopType().toString();
        String hash = (examHot.getPosition() < 10)
                ? examHot.getPosition()+"_"+examHot.getId()
                : "_" + examHot.getPosition() + "_" + examHot.getId();
        redisRepository.hput(key,hash, JsonUtils.pretty(ExamItemDTO.with(exam)));

        redisRepository.expire(key,RedisConst.TIME_TO_LIVE_LONG);
    }

    @Override
    public void processPutAllHotExam(Map<ExamHot,Exam> examHotExamMap,String topType) {
        redisRepository.select(RedisConst.DB_EXAM);
        String key = topType;

        examHotExamMap.keySet().forEach(examHot -> {
            Exam exam = examHotExamMap.get(examHot);
            String hash = (examHot.getPosition()<10)
                    ? examHot.getPosition()+"_"+examHot.getId()
                    : "_"+examHot.getPosition()+"_"+examHot.getId();
            redisRepository.hput(key,hash, JsonUtils.pretty(ExamItemDTO.with(exam)));
        });

        redisRepository.expire(key,RedisConst.TIME_TO_LIVE_LONG);
    }

    @Override
    public void processDeleteHotExam(ExamHot examHot) {
        redisRepository.select(RedisConst.DB_EXAM);

        String key = examHot.getTopType().toString();
        String hash = (examHot.getPosition()<10)
                ? examHot.getPosition()+"_"+examHot.getId()
                : "_"+examHot.getPosition()+"_"+examHot.getId();
        redisRepository.hdelete(key,hash);
    }

    @Override
    public Map<String, String> processGetHotExam(String topType) {
        redisRepository.select(RedisConst.DB_EXAM);

        Map<String,String> mapExamHotList =(StringUtils.isNotNull(topType))
                ? redisRepository.hgetAll(TopType.valueOf(topType).toString())
                : redisRepository.hgetAll(TopType.HOME_SCREEN.toString());
        return mapExamHotList;
    }
}
