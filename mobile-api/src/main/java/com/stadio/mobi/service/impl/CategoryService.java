package com.stadio.mobi.service.impl;

import com.stadio.common.utils.HelperUtils;
import com.stadio.common.utils.JsonUtils;
import com.stadio.common.utils.ResponseCode;
import com.stadio.common.utils.StringUtils;
import com.stadio.mobi.controllers.ExamController;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.ICategoryService;
import com.stadio.mobi.service.IExamService;
import com.stadio.mobi.service.ITransactionService;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;
import com.stadio.model.dtos.mobility.CategoryItemDTO;
import com.stadio.model.dtos.mobility.CategoryDetailsDTO;
import com.stadio.model.dtos.mobility.ExamItemDTO;
import com.stadio.model.enu.*;
import com.stadio.model.redisUtils.CategoryRedisRepository;
import com.stadio.model.repository.main.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService extends BaseService implements ICategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired IExamService examService;

    @Autowired UserExamRepository userExamRepository;

    @Autowired
    CategoryRedisRepository categoryRedisRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    ExamCategoryRepository examCategoryRepository;

    @Autowired
    UserCategoryRepository userCategoryRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    ITransactionService transactionService;

    private Logger logger = LogManager.getLogger(CategoryService.class);

    @Override
    public List<CategoryItemDTO> getListCategory() {
        List<CategoryItemDTO> categoryItemDTOS = new ArrayList<>();

        Map<String, String> mapCategories = categoryRedisRepository.processGetCategory();

        if(!mapCategories.isEmpty()){
            List<String> categoryList = new ArrayList<>(mapCategories.values());
            if (categoryList.size() != 0) {
                categoryList.forEach(category -> {
                    CategoryItemDTO categoryItemDTO = JsonUtils.parse(category,CategoryItemDTO.class);
                    categoryItemDTOS.add(categoryItemDTO);
                });
            }
        } else {
            List<Category> categoryList = categoryRepository.findAll();
            categoryList.forEach(category -> {
                CategoryItemDTO categoryItemDTO = new CategoryItemDTO(category);
                categoryItemDTOS.add(categoryItemDTO);
            });

            categoryRedisRepository.processPutAllCategory(categoryList);
        }
        return categoryItemDTOS;
    }

    @Override
    public ResponseResult processGetDetailCategory(String id, String token) {
        if (StringUtils.isNotNull(id)) {
            Category category = categoryRepository.findOne(id);

            if (category != null) {
                CategoryDetailsDTO categoryDetailsDTO = CategoryDetailsDTO.with(category);

                User user = this.getUserRequesting();

                if (user != null) {
                    Bookmark bookmark = bookmarkRepository.findOneByUserIdAndCategoryId(user.getId(), category.getId());
                    categoryDetailsDTO.setIsBookmarked(bookmark != null);
                }

                categoryDetailsDTO.with(examRepository, examCategoryRepository, 0, Integer.MAX_VALUE);

                categoryDetailsDTO.getExamList().forEach(exam -> {
                    String detailsUrl = MvcUriComponentsBuilder
                            .fromMethodName(ExamController.class, "getExamDetails", exam.getId())
                            .host(host).port(port)
                            .build().toString();
                    exam.getActions().put(ActionBase.DETAILS, detailsUrl);

                    if (user != null && exam.getPrice() > 0) {
                        UserExam userExam = userExamRepository.findByUserIdRefAndExamIdRef(user.getId(), exam.getId());
                        if (userExam != null) {
                            exam.setPrice(-1);
                        }
                    }
                });
                return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("category.success.getDetail"), categoryDetailsDTO);
            }
        }

        return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("category.invalid.id"), null);

    }

    @Override
    public ResponseResult processGetDetail(String id) {
        Category category = categoryRepository.findOne(id);
        if (category == null) {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("category.not.found"), null);
        }

        User user = this.getUserRequesting();

        CategoryDetailsDTO categoryDetailsDTO = CategoryDetailsDTO.with(category);

        UserCategory userCategory;

        if (user != null) {
            //check bookmarked
            Bookmark bookmark = bookmarkRepository.findOneByUserIdAndCategoryId(user.getId(), category.getId());
            categoryDetailsDTO.setIsBookmarked(bookmark != null);

            //registerd or not
            try {
                userCategory = userCategoryRepository.findFirstByUserIdRefAndCategoryIdRefAndAction(user.getId(), category.getId(), UserCategoryAction.REGISTER);
                categoryDetailsDTO.setIsRegistered(userCategory != null);
            } catch (Exception e) {
                logger.error("processGetDetailCategory: register " + "userId: " + user.getId() + " categoryId: " + category.getId(), e);
            }

            //like
            try {
                userCategory = userCategoryRepository.findFirstByUserIdRefAndCategoryIdRefAndAction(user.getId(), category.getId(), UserCategoryAction.LIKE);
                categoryDetailsDTO.setIsLiked(userCategory != null);
            } catch (Exception e) {
                logger.error("processGetDetailCategory: like " + "userId: " + user.getId() + " categoryId: " + category.getId(), e);
            }
        }

        //bookmark count
        try {
            categoryDetailsDTO.setBookmarkCount(bookmarkRepository.countByObjectIdAndBookmarkType(category.getId(), BookmarkType.EXAM_CATEGORY));
        } catch (Exception e) {
            logger.error("processGetDetailCategory: bookmark count " + "userId: " + user.getId() + " categoryId: " + category.getId(), e);
        }

        //like count
        try {
            categoryDetailsDTO.setLikeCount(userCategoryRepository.countByCategoryIdRefAndAction(category.getId(), UserCategoryAction.LIKE));
        } catch (Exception e) {
            logger.error("processGetDetailCategory: like count " + "userId: " + user.getId() + " categoryId: " + category.getId(), e);
        }

        //registerd count
        try {
            categoryDetailsDTO.setRegisteredCount(userCategoryRepository.countByCategoryIdRefAndAction(category.getId(), UserCategoryAction.REGISTER));
        } catch (Exception e) {
            logger.error("processGetDetailCategory: registerd count " + "userId: " + user.getId() + " categoryId: " + category.getId(), e);
        }

        //submit count
        try {
            List<ExamCategory> examCategories = examCategoryRepository.findAllByCategoryId(category.getId(), new PageRequest(0, Integer.MAX_VALUE)).getContent();
            List<String> examIds = examCategories.stream().map(x -> x.getExamId()).collect(Collectors.toList());

            categoryDetailsDTO.setSubmittedCount(userExamRepository.countByExamIdRefIn(examIds));
        } catch (Exception e) {
            logger.error("processGetDetailCategory: submit count " + "userId: " + user.getId() + " categoryId: " + category.getId(), e);
        }

        //Teacher
        categoryDetailsDTO.with(teacherRepository, category.getTeacherId());

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("category.success.getDetail"), categoryDetailsDTO);
    }

    @Override
    public ResponseResult processGetExamInCategory(String categoryId, int page, int pageSize) {

        if (page > 0) {
            page = page - 1;
        }

        Category category = categoryRepository.findOne(categoryId);
        if (category == null) {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("category.not.found"), null);
        }
        CategoryDetailsDTO categoryDetailsDTO = new CategoryDetailsDTO();
        categoryDetailsDTO.setId(categoryId);
        categoryDetailsDTO.with(examRepository, examCategoryRepository, page, pageSize);

        if (getUserRequesting() != null) {
            for (ExamItemDTO examItemDTO: categoryDetailsDTO.getExamList()) {
                examService.setPriceAndDidDone(examItemDTO);
            }
        }

        return ResponseResult.newInstance(ResponseCode.SUCCESS, "succes",categoryDetailsDTO.getExamList());
    }

    @Override
    public ResponseResult likeCategory(String categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        if (category == null) {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("category.not.found"), null);
        }

        UserCategory userCategory = userCategoryRepository.
                findFirstByUserIdRefAndCategoryIdRefAndAction(this.getUserRequesting().getId(), categoryId, UserCategoryAction.LIKE);
        if (userCategory != null) {
            return ResponseResult.newSuccessInstance(null);
        }
        userCategory = new UserCategory();
        userCategory.setUserIdRef(this.getUserRequesting().getId());
        userCategory.setCategoryIdRef(categoryId);
        userCategory.setAction(UserCategoryAction.LIKE);
        userCategoryRepository.save(userCategory);
        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult register(String categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        if (category == null) {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("category.not.found"), null);
        }

        User user = this.getUserRequesting();

        UserCategory userCategory = userCategoryRepository.findFirstByUserIdRefAndCategoryIdRefAndAction(user.getId(), categoryId, UserCategoryAction.REGISTER);

        if (userCategory != null) {
            return ResponseResult.newErrorInstance(ResponseCode.FAIL, getMessage("category.already.bought"));
        }

        if (category.getPrice() > 0) {
            if (user.getBalance() < category.getPrice()) {
                return ResponseResult.newErrorInstance(ResponseCode.BALANCE, this.getMessage("balance.not.enough"));
            } else {
                Transaction transaction = new Transaction();
                transaction.setObjectId(category.getId());
                transaction.setTransContent("Mua Bộ đề [" + category.getName() + "] mã bộ đề [" + category.getId() + "]");
                transaction.setTransType(TransactionType.CATEGORY);
                transaction.setUserIdRef(user.getId());
                transactionService.processDeduction(null, (int) category.getPrice(), transaction);
            }
        }

        userCategory = new UserCategory();
        userCategory.setAction(UserCategoryAction.REGISTER);
        userCategory.setUserIdRef(user.getId());
        userCategory.setCategoryIdRef(categoryId);
        userCategoryRepository.save(userCategory);

        PageRequest pageRequest = new PageRequest(0, Integer.MAX_VALUE);
        List<ExamCategory> examCategories = examCategoryRepository.findAllByCategoryId(categoryId, pageRequest).getContent();

        List<String> examIds = examCategories.stream().map(x -> x.getExamId()).collect(Collectors.toList());
        if (!HelperUtils.isEmptyArray(examIds)) {
            for (String examId: examIds) {
                try {
                    if (!examService.isUserDidDone(examId)) {
                        Exam exam = examRepository.findOne(examId);
                        if (exam != null) {
                            //Set da mua cho exam do
                            UserExam userExam = new UserExam();
                            userExam.setStartTime(new Date());
                            userExam.setUserIdRef(user.getId());
                            userExam.setStatus(PracticeStatus.PROCESS);
                            userExam.setExamIdRef(examId);
                            userExamRepository.save(userExam);
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return ResponseResult.newSuccessInstance(null);
    }

    @Override
    public ResponseResult recommend(String categoryId, int page, int pageSize) {
        if (page > 0) {
            page = page - 1;
        }
        List<CategoryItemDTO> categoryItemDTOS = new ArrayList<>();

        List<String> categoryIdList = new ArrayList<>();

        User user = getUserRequesting();
        if (user != null) {
            List<UserCategory> userCategories = userCategoryRepository.findAllByUserIdRefAndAction(user.getId(), UserCategoryAction.REGISTER);
            if (!HelperUtils.isEmptyArray(userCategories)) {
                categoryIdList = userCategories.stream().map(x -> x.getCategoryIdRef()).collect(Collectors.toList());
            }
        }

        if (StringUtils.isNotNull(categoryId)) {
            categoryIdList.add(categoryId);
        }

        PageRequest pageRequest = new PageRequest(page, pageSize);

        List<Category> categoryList = categoryRepository.findAllByIdNotIn(categoryIdList, pageRequest).getContent();
        categoryList.forEach(category -> {
            CategoryItemDTO categoryItemDTO = new CategoryItemDTO(category);
            categoryItemDTOS.add(categoryItemDTO);
        });
        return ResponseResult.newSuccessInstance(categoryItemDTOS);
    }
}
