package com.stadio.mobi.service.impl;

import com.mongodb.DBObject;
import com.stadio.common.utils.ResponseCode;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IUserExamStatisticService;
import com.stadio.model.documents.*;
import com.stadio.model.model.CorrectAnswerInChapter;
import com.stadio.model.repository.main.*;
import com.stadio.model.repository.user.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.*;

@Service
public class UserExamStatisticService extends BaseService implements IUserExamStatisticService {

    @Autowired
    ExamRepository examRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserExamRepository userExamRepository;

    @Autowired
    ChapterRepository chapterRepository;

    @Autowired
    UserExamStatisticsRepository userExamStatisticsRepository;

    private Logger logger = LogManager.getLogger(UserExamStatisticService.class);

    @Override
    public ResponseResult type1(String userId, String examId) {
        UserExamStatistics userExamStatistics = userExamStatisticsRepository.findByExamIdRefAndUserIdRef(examId,userId);
        if(userExamStatistics!=null && userExamStatistics.getType1()!=null){
            String result = userExamStatistics.getType1();
            return ResponseResult.newSuccessInstance(result);
        }
        else{
            UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(userId,examId);
            if(userExam!=null){
                int correctNumber = userExam.getCorrectNumber();
                long userJoin = userExamRepository.countByExamIdRef(examId);
                long quantityGreaterThan = userExamRepository.countByExamIdRefIsAndCorrectNumberLessThan(examId,correctNumber);
                String result = quantityGreaterThan+"/"+userJoin;
                if(userExamStatistics==null){
                    userExamStatistics = new UserExamStatistics();
                    userExamStatistics.setExamIdRef(examId);
                    userExamStatistics.setUserIdRef(userId);
                }
                userExamStatistics.setType1(result);
                userExamStatisticsRepository.save(userExamStatistics);
                return ResponseResult.newSuccessInstance(result);
            }
            else return ResponseResult.newErrorInstance(ResponseCode.INVALID_VALUE,"userId or examId invalid");
        }
    }

    @Override
    public ResponseResult type2(String userId,String examId) {
        UserExamStatistics userExamStatistics = userExamStatisticsRepository.findFirstByExamIdRef(examId);
        if(userExamStatistics!=null && userExamStatistics.getType2()!=null){
            Map<Integer,Integer> statisticScore = userExamStatistics.getType2();
            return ResponseResult.newSuccessInstance(statisticScore);
        }else{
            try
            {
                UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(userId,examId);
                if(userExam!=null){
                    int totalQuestion = userExam.getTotal();
                    Iterable<DBObject> firstBatchCorrectNumber = userExamRepository.groupByCorrectNumber(examId);
                    Map<Integer,Integer> statisticCorrectNumber = convertIteratorToDTO(firstBatchCorrectNumber);
                    Map<Integer,Integer> statisticScore = new HashMap<>();
                    int levelScore = 10;
                    for(int pos =0;pos<=levelScore;pos++){
                        statisticScore.put(pos,0);
                    }
                    for(int pos =0;pos<=totalQuestion;pos++){
                        int score = (int)(pos*10*1.0/totalQuestion);
                        Integer userCorrectNumber = statisticCorrectNumber.get(pos);

                        int userCorrectNumberTotal = statisticScore.get(score);
                        if(userCorrectNumber!=null){
                            userCorrectNumberTotal+= userCorrectNumber;
                            statisticScore.put(score,userCorrectNumberTotal);
                        }
                    }

                    if(userExamStatistics==null){
                        userExamStatistics = new UserExamStatistics();
                        userExamStatistics.setExamIdRef(examId);
                        userExamStatistics.setUserIdRef(userId);
                    }
                    userExamStatistics.setType2(statisticScore);
                    userExamStatisticsRepository.save(userExamStatistics);
                    return ResponseResult.newSuccessInstance(statisticScore);
                }
                else return ResponseResult.newErrorInstance(ResponseCode.INVALID_VALUE,"userId or examId invalid");
            }catch (Exception e)
            {
                logger.error("process group correct number exception: ", e);
                return ResponseResult.newErrorInstance(ResponseCode.INVALID_VALUE,"exeption failure");
            }
        }
    }

    @Override
    public ResponseResult type3(String userId, String examId) {
        UserExamStatistics userExamStatistics = userExamStatisticsRepository.findByExamIdRefAndUserIdRef(examId,userId);
        if(userExamStatistics!=null && userExamStatistics.getType3()!=null){
            List<Chapter> chapterList = chapterRepository.findAll();
            Map<String,CorrectAnswerInChapter> mapResult = new HashMap<>();
            Map<String,CorrectAnswerInChapter> mapChapter = userExamStatistics.getType3();
            chapterList.forEach(chapter -> {
                String chapterId = chapter.getId();
                CorrectAnswerInChapter correctAnswerInChapter = mapChapter.get(chapterId);
                mapResult.put(chapter.getName(),correctAnswerInChapter);
            });
            return ResponseResult.newSuccessInstance(mapResult);
        }
        else{
            UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(userId,examId);
            if(userExam!=null){
                List<Chapter> chapterList = chapterRepository.findAll();
                Map<String,CorrectAnswerInChapter> mapResult = new HashMap<>();
                Map<String,CorrectAnswerInChapter> mapChapter = new HashMap<>();
                chapterList.forEach(chapter -> {
                    mapChapter.put(chapter.getId(),new CorrectAnswerInChapter());
                });

                JSONParser parser = new JSONParser();
                try {

                    //tuy vao cau truc thu muc server ma cau truc lai thu muc
                    Object obj = parser.parse(new FileReader(
                            "/opt/hoc68-storage/chemistry/USERS/save_by_feature/submit_exam"));

                    JSONObject jsonObject = (JSONObject) obj;

                    JSONArray resultList = (JSONArray) jsonObject.get("results");

                    Iterator<JSONObject> iterator = resultList.iterator();
                    while (iterator.hasNext()) {
                        JSONObject resultQuestion = iterator.next();
                        String idQuestion = (String) resultQuestion.get("id");
                        Question question = questionRepository.findOne(idQuestion);
                        String idChapter = question.getChapterId();
                        CorrectAnswerInChapter correctAnswerInChapter = mapChapter.get(idChapter);

                        String choose = (String) resultQuestion.get("choose");
                        String correctAnswer = (String) resultQuestion.get("correctAnswer");
                        if(choose!=null && correctAnswer.equals(choose)){
                            correctAnswerInChapter.correctAnswer+=1;
                        }
                        correctAnswerInChapter.totalQuestion+=1;
                    }
                    chapterList.forEach(chapter -> {
                        String chapterId = chapter.getId();
                        CorrectAnswerInChapter correctAnswerInChapter = mapChapter.get(chapterId);
                        mapResult.put(chapter.getName(),correctAnswerInChapter);
                    });
                    if(userExamStatistics==null){
                        userExamStatistics = new UserExamStatistics();
                        userExamStatistics.setExamIdRef(examId);
                        userExamStatistics.setUserIdRef(userId);
                    }
                    userExamStatistics.setType3(mapChapter);
                    userExamStatisticsRepository.save(userExamStatistics);
                    return ResponseResult.newSuccessInstance(mapResult);

                } catch (Exception e) {
                    logger.error("process read file or convert json exception: ", e);
                    return ResponseResult.newErrorInstance(ResponseCode.INVALID_VALUE,"exeption failure");
                }

            }
            else return ResponseResult.newErrorInstance(ResponseCode.INVALID_VALUE,"userId or examId invalid");
        }
    }

    Map<Integer,Integer> convertIteratorToDTO(Iterable<DBObject> firstBatch){
        Map<Integer,Integer> correctNumberStatisticResultList = new HashMap<>();

        Iterator var7 = firstBatch.iterator();

        while(var7.hasNext()) {
            DBObject dbObject = (DBObject)var7.next();
            Integer correctNumber = (Integer)dbObject.get("_id");
            Integer userCorrectNumber = (Integer)dbObject.get("userCorrectNumber");
            correctNumberStatisticResultList.put(correctNumber,userCorrectNumber);
        }

        return correctNumberStatisticResultList;
    }
}