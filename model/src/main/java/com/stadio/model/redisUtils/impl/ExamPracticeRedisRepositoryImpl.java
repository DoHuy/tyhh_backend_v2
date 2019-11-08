package com.stadio.model.redisUtils.impl;

import com.stadio.common.utils.JsonUtils;
import com.stadio.model.documents.QuestionInExam;
import com.stadio.model.dtos.mobility.QuestionItemDTO;
import com.stadio.model.redisUtils.ExamPracticeRedisRepository;
import com.stadio.model.redisUtils.RedisConst;
import com.stadio.model.redisUtils.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ExamPracticeRedisRepositoryImpl implements ExamPracticeRedisRepository {

    @Autowired
    RedisRepository redisRepository;

    @Override
    public Map<String, String> processGetExamPractice(String keys) {
        redisRepository.select(RedisConst.DB_USER);

        keys = RedisConst.USER_REQUEST_EXAM + ":" + keys;
        return redisRepository.hgetAll(keys);
    }

    @Override
    public List<QuestionItemDTO> processPutExamPractice(String keys, List<QuestionInExam> questionInExamList) {
        redisRepository.select(RedisConst.DB_USER);

        List<QuestionItemDTO> questionDTOList = new ArrayList<>();

        Map<String, String> data = new LinkedHashMap<>();
        for(int pos = 0;pos<questionInExamList.size();pos++)
        {
            QuestionInExam q = questionInExamList.get(pos);
            QuestionItemDTO questionItem = QuestionItemDTO.with(q.getQuestion());
            questionDTOList.add(questionItem);
            String hash = (q.getPosition()<10)
                    ? pos+"_"+q.getQuestion().getId()
                    :"_"+pos+"_"+q.getQuestion().getId();
            data.put(hash, JsonUtils.writeValue(q.getQuestion()));
        };

        keys = RedisConst.USER_REQUEST_EXAM + ":" + keys;

        redisRepository.hmset(keys, data);

        redisRepository.expire(keys, RedisConst.TIME_TO_LIVE_SHORT);

        return questionDTOList;
    }
}
