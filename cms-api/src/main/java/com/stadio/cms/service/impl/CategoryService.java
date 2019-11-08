package com.stadio.cms.service.impl;

import com.stadio.common.utils.HelperUtils;
import com.stadio.common.utils.ResponseCode;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.ICategoryService;
import com.stadio.cms.service.IExamService;
import com.stadio.cms.service.IManagerService;
import com.stadio.common.utils.JsonUtils;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Category;
import com.stadio.model.documents.Exam;
import com.hoc68.users.documents.Manager;
import com.stadio.model.documents.ExamCategory;
import com.stadio.model.documents.UserCategory;
import com.stadio.model.dtos.cms.CategoryFormDTO;
import com.stadio.model.dtos.cms.CategoryListDTO;
import com.stadio.model.dtos.cms.category.CategoryDetailDTO;
import com.stadio.model.redisUtils.CategoryRedisRepository;
import com.stadio.model.repository.main.CategoryRepository;
import com.stadio.model.repository.main.ExamCategoryRepository;
import com.stadio.model.repository.main.ExamRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService extends BaseService implements ICategoryService
{

    @Autowired CategoryRepository categoryRepository;

    @Autowired IManagerService managerService;

    @Autowired IExamService examService;

    @Autowired ExamRepository examRepository;

    @Autowired
    ExamCategoryRepository examCategoryRepository;

    @Autowired
    CategoryRedisRepository categoryRedisRepository;

    private Logger logger = LogManager.getLogger(CategoryService.class);

    @Override
    public ResponseResult processCreateCategory(CategoryFormDTO categoryFormDTO, String token)
    {
        return this.processUpdateCategory(categoryFormDTO, null);
    }

    @Override
    public ResponseResult processUpdateCategory(CategoryFormDTO categoryFormDTO, String token)
    {

        if (!StringUtils.isNotNull(categoryFormDTO.getName()))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("category.invalid.name"), null);

        }

        Manager manager = managerService.getManagerRequesting();

        Category category = new Category();

        if (StringUtils.isNotNull(categoryFormDTO.getId())) {
            category = categoryRepository.findOne(categoryFormDTO.getId());

            if (category == null) {
                return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("category.invalid.id"), null);
            }

            category.setUpdatedBy(manager.getId());
            category.setUpdatedDate(new Date());
        } else {

            category.setCreatedBy(manager.getId());
        }

        category.setImageUrl(categoryFormDTO.getImageUrl());
        category.setName(categoryFormDTO.getName());
        category.setSummary(categoryFormDTO.getSummary());
        category.setPosition(categoryFormDTO.getPosition());
        category.setPrice(categoryFormDTO.getPrice());
        category.setPriceOld(categoryFormDTO.getPriceOld());
        category.setHasCorrectionDetail(categoryFormDTO.getHasCorrectionDetail());
        category.setTeacherId(categoryFormDTO.getTeacherId());

        Category categorysave = categoryRepository.save(category);

        if (categorysave != null) {
            categoryRedisRepository.processPutCategory(categorysave);
        }

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("category.success.update"), new CategoryListDTO(category));
    }

    @Override
    public ResponseResult processDeleteCategory(String id)
    {
        if (!StringUtils.isNotNull(id))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("category.invalid.id"), null);
        }

        Category category = categoryRepository.findOne(id);

        if (category == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("category.invalid.id"), null);
        }
        else
        {
            categoryRepository.delete(id);
            categoryRedisRepository.processDeleteCategory(id);
            return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("category.success.delete"), null);
        }
    }

    @Override
    public ResponseResult processAddExamToCategory(String categoryId, String examID, String examCode)
    {
        if (!StringUtils.isNotNull(categoryId))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("category.invalid.id"), null);
        }

        Category category = categoryRepository.findOne(categoryId);

        if (category == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("category.invalid.id"), null);
        }

        if (!StringUtils.isNotNull(examID) && !StringUtils.isNotNull(examCode))
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("exam.invalid.id"), null);
        }

        Exam exam = null;
        if (StringUtils.isNotNull(examID))
        {
            exam = examService.findOne(examID);
        }

        if (StringUtils.isNotNull(examCode))
        {
            exam = examRepository.findExamByCode(examCode);
        }

        if (exam == null)
        {
            return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("exam.invalid.id"), null);
        }

        List<ExamCategory> examCategories = examCategoryRepository.findAllByCategoryId(categoryId, new PageRequest(0, Integer.MAX_VALUE)).getContent();

        List<String> examIdList = examCategories.stream().map(x -> x.getExamId()).collect(Collectors.toList());

        if (examIdList.contains(exam.getId())) {
            return ResponseResult.newInstance(ResponseCode.FAIL, getMessage("exam.invalid.contains"), null);
        }

        ExamCategory newExamCategory = new ExamCategory();
        newExamCategory.setExamId(exam.getId());
        newExamCategory.setCategoryId(categoryId);
        examCategoryRepository.save(newExamCategory);

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("category.success.addExam"), exam);
    }

    @Override
    public ResponseResult processGetListCategory()
    {
        List<Category> categoryList = categoryRepository.findAll();
        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("category.success.getList"), categoryList);
    }

    @Override
    public ResponseResult processGetDetailCategory(String id)
    {
        if (StringUtils.isNotNull(id))
        {
            Category category = categoryRepository.findOne(id);
            CategoryDetailDTO categoryDetailDTO = new CategoryDetailDTO(category);
            categoryDetailDTO.with(examCategoryRepository, examRepository);

            if (category != null) {
                return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("category.success.getDetail"), categoryDetailDTO);
            }
        }

        return ResponseResult.newInstance(ResponseCode.MISSING_PARAM, getMessage("category.invalid.id"), null);
    }

    @Override
    public ResponseResult processRemoveExamFromCategory(String categoryId, String examId)
    {

        List<ExamCategory> examCategories = examCategoryRepository.findAllByCategoryIdAndExamId(categoryId, examId);
        if (!HelperUtils.isEmptyArray(examCategories)) {
            for (ExamCategory examCategory: examCategories) {
                examCategoryRepository.delete(examCategory);
            }
        }
//        Exam exam = examService.findOne(examId);
//        if (exam != null)
//        {
//            List<Category> categoryList = exam.getCategoryIds();
//            logger.info("Find: " + JsonUtils.pretty(categoryList) + " \n");
//
//            for (int i = 0; i < categoryList.size(); i++)
//            {
//                if (categoryList.get(i).getId().equals(categoryId))
//                {
//                    categoryList.remove(i);
//                }
//            }
//            exam.setCategoryIds(categoryList);
//            examRepository.save(exam);
//        }
//
//        Category category = categoryRepository.findOne(categoryId);
//        if (category != null)
//        {
//            List<Exam> examList = category.getExamIds();
//            for (int i = 0; i < examList.size(); i++)
//            {
//                if (examList.get(i).getId().equals(examId))
//                {
//                    examList.remove(i);
//                }
//            }
//            category.setExamIds(examList);
//            Category categorysave = categoryRepository.save(category);
//
//            if(categorysave!=null){
//                categoryRedisRepository.processPutCategory(categorysave);
//            }
//        }

        return ResponseResult.newInstance(ResponseCode.SUCCESS, getMessage("category.success.getDetail"), null);
    }

}
