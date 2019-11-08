package com.stadio.mobi.service.impl;

import com.hoc68.users.documents.User;
import com.stadio.common.utils.ResponseCode;
import com.stadio.common.utils.StringUtils;
import com.stadio.mobi.controllers.CategoryController;
import com.stadio.mobi.controllers.ExamController;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.IExamService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.hoc68.users.documents.User;
import com.stadio.model.dtos.mobility.CategoryItemDTO;
import com.stadio.model.dtos.mobility.ExamDetailsDTO;
import com.stadio.model.dtos.mobility.ExamItemDTO;
import com.stadio.model.enu.ActionBase;
import com.stadio.model.repository.main.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.*;

@Service
public class ExamService extends BaseService implements IExamService
{
    private Logger logger = LogManager.getLogger(ExamService.class);

    @Autowired
    ExamRepository examRepository;

    @Autowired
    QuestionInExamRepository questionInExamRepository;

    @Autowired
    ExamHotRepository examHotRepository;

    @Autowired CategoryService categoryService;

    @Autowired ExamLikesRepository examLikesRepository;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    UserExamRepository userExamRepository;

    @Override
    public ResponseResult processGetExamDetailForMobile(String id)
    {

        if (!StringUtils.isNotNull(id))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("exam.invalid.id"), null);
        }

        Exam exam = examRepository.findOne(id);

        if (exam == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("exam.invalid.id"), null);
        }

        ExamDetailsDTO examDetailsDTO = ExamDetailsDTO.with(exam);

        User user = getUserRequesting();

        if (user != null){

            Bookmark bookmark = bookmarkRepository.findOneByUserIdAndExamId(user.getId(), exam.getId());

            examDetailsDTO.setIsBookmarked(bookmark != null);

            ExamLikes examLikes = examLikesRepository.findByExamIdAndUserId(exam.getId(), user.getId());

            examDetailsDTO.setIsLiked(examLikes != null);

            UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(user.getId(), exam.getId());

            examDetailsDTO.setDidDone(userExam != null);

            if (userExam != null && examDetailsDTO.getPrice() > 0) {
                examDetailsDTO.setPrice(-1);
            }

        }

        try
        {

            examDetailsDTO.setViews(exam.getViews() + 1);
            examDetailsDTO.setQuantity(exam.getQuestionQuantity());

            exam.setViews(examDetailsDTO.getViews());
            examRepository.save(exam);
        }
        catch (Exception e)
        {
            logger.error("Redis exception: " + e.getMessage());
        }

        String practiceUrl = MvcUriComponentsBuilder
                .fromMethodName(ExamController.class, "getQuestionByExam", null, exam.getId())
                .host(host).port(port)
                .build().toString();
        examDetailsDTO.getActions().put(ActionBase.PRACTICE, practiceUrl);

        String likeUrl = MvcUriComponentsBuilder
                .fromMethodName(ExamController.class, "actionLike", null, exam.getId())
                .host(host).port(port)
                .build().toString();
        examDetailsDTO.getActions().put(ActionBase.LIKE, likeUrl);

        String answerUrl = MvcUriComponentsBuilder.fromMethodName(ExamController.class, "getAnswers", null, exam.getId())
                .host(host).port(port)
                .build().toString();
        examDetailsDTO.getActions().put(ActionBase.ANSWERS, answerUrl);
        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("exam.success.getDetail"), examDetailsDTO);
    }

    @Override
    public void setPriceAndDidDone(ExamItemDTO examItemDTO) {

        User user = this.getUserRequesting();

        if (user != null) {

            UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(user.getId(), examItemDTO.getId());

            examItemDTO.setDidDone(userExam != null);

            if (userExam != null && examItemDTO.getPrice() > 0) {
                examItemDTO.setPrice(-1);
            }
        }
    }

    @Override
    public boolean isUserDidDone(String examId) {
        User user = this.getUserRequesting();
        if (user != null) {
            UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(user.getId(), examId);
            return (userExam != null);
        }
        return false;
    }


    @Override
    public ResponseResult processGetHighlight(String token)
    {
        List<CategoryItemDTO> categoryItemDTOS = categoryService.getListCategory();
        categoryItemDTOS.forEach(category ->
        {
            String detailsUrl = MvcUriComponentsBuilder
                    .fromMethodName(CategoryController.class, "getCategoryDetails", category.getId(), null)
                    .host(host).port(port)
                    .build().toString();
            category.getActions().put(ActionBase.DETAILS, detailsUrl);
        });
        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("home.success.getHighlight"), categoryItemDTOS);

    }

    @Override
    public ResponseResult processGetRecommend(String token)
    {
        long count = examRepository.countByEnableAndDeleted(true, false);
        int limit = 30;

        User user = this.getUserRequesting();

        List<Exam> examList = new ArrayList<>();
        if (count < limit)
        {
            examList = examRepository.findAllByEnableAndDeleted(true,false);
        }
        else
        {
            Random random = new Random();
            int idx = random.nextInt(Integer.parseInt(String.valueOf(count / limit)));
            PageRequest request = new PageRequest(idx, limit, new Sort(Sort.Direction.DESC, "created_date"));
            if (user != null && user.getClazzId() != null)
            {
                //
                examList = examRepository.findExamByClazzIdOrderByCreatedByDesc(user.getClazzId(), request).getContent();
            }

            if (examList.isEmpty())
            {
                examList = examRepository.findExamNewest(request).getContent();
            }

        }

        List<ExamItemDTO> examItemDTOList = getListExamItemDTO(examList, token);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("home.success.getSuggest"), examItemDTOList);
    }

    public List<ExamItemDTO> getListExamItemDTO(List<Exam> examList, String token)
    {

        List<ExamItemDTO> examItemDTOList = new ArrayList<>();

        examList.stream().forEach(exam ->
        {
            ExamItemDTO examItemDTO = ExamItemDTO.with(exam);
            String detailsUrl = MvcUriComponentsBuilder
                    .fromMethodName(ExamController.class, "getExamDetails", exam.getId())
                    .host(host).port(port)
                    .build().toString();
            examItemDTO.getActions().put(ActionBase.DETAILS, detailsUrl);

            setPriceAndDidDone(examItemDTO);

            examItemDTOList.add(examItemDTO);
        });

        return examItemDTOList;
    }

    @Override
    public ResponseResult processGetNewestExam(String token, Integer page, Integer limit)
    {
        PageRequest request = new PageRequest(page - 1, limit, new Sort(Sort.Direction.DESC, "created_date"));
        List<Exam> examList = examRepository.findExamNewest(request).getContent();

        List<ExamItemDTO> examItemDTOList = new ArrayList<>();
        examList.forEach(exam ->
        {
            ExamItemDTO examItemDTO = ExamItemDTO.with(exam);

            String detailsUrl = MvcUriComponentsBuilder
                    .fromMethodName(ExamController.class, "getExamDetails", exam.getId())
                    .host(host).port(port)
                    .build().toString();

            examItemDTO.getActions().put(ActionBase.DETAILS, detailsUrl);

            setPriceAndDidDone(examItemDTO);
            examItemDTOList.add(examItemDTO);

        });

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("home.success.getSuggest"), examItemDTOList);
    }

    @Override
    public ResponseResult processUserActionLike(String token, String examId)
    {
        Exam exam = examRepository.findOne(examId);
        if (exam == null)
        {
            return ResponseResult.newErrorInstance("01", this.getMessage("examHot.invalid.notfound"));
        }

        User user = this.getUserRequesting();

        ExamLikes examLike = examLikesRepository.findByExamIdAndUserId(examId, user.getId());
        if (examLike == null)
        {
            examLike = new ExamLikes();
            examLike.setUserId(user.getId());
            examLike.setExamId(examId);
            examLikesRepository.save(examLike);

            exam.setLikes(exam.getLikes() + 1);
            examRepository.save(exam);
        }

        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult processGetExamTopSubmit(String token, int page, int limit)
    {
        PageRequest request = new PageRequest(page - 1, limit, new Sort(Sort.Direction.DESC, "submit_count"));
        List<Exam> examList = examRepository.findExamOrderBySubmitCountDesc(request).getContent();

        List<ExamItemDTO> examItemDTOList = getListExamItemDTO(examList, token);

        return ResponseResult.newSuccessInstance(examItemDTOList);
    }

//    public void updateStatistic(ExamItemDTO examItemDTO)
//    {
//
//        Map<String, String> examStatistics = examRepository.getViewsOnRedis(examItemDTO.getId());
//        try
//        {
//            if (examStatistics.get(RedisConst.EXAM_KEY.QUESTION) != null)
//            {
//                logger.info("### Statistic: " + examStatistics.toString());
//                long quantity = Long.parseLong(examStatistics.get(RedisConst.EXAM_KEY.QUESTION));
//                examItemDTO.setQuantity(quantity);
//            }
//            else
//            {
//                long quantity = questionInExamRepository.getQuestionQuantityOfExam(examItemDTO.getId());
//                examStatistics.put(RedisConst.EXAM_KEY.QUESTION, String.valueOf(quantity));
//                examRepository.updateViewsOnRedis(examItemDTO.getId(), examStatistics);
//                logger.info("Update exam:" + examStatistics);
//                examItemDTO.setQuantity(quantity);
//            }
//        }
//        catch (Exception e)
//        {
//            logger.error(e);
//        }
//    }
}


