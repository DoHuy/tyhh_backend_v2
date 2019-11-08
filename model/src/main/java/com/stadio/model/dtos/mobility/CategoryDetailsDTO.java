package com.stadio.model.dtos.mobility;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.common.utils.HelperUtils;
import com.stadio.common.utils.StringUtils;
import com.stadio.model.documents.Category;
import com.stadio.model.documents.Exam;
import com.stadio.model.documents.ExamCategory;
import com.stadio.model.documents.Teacher;
import com.stadio.model.dtos.mobility.teacher.TeacherDTO;
import com.stadio.model.repository.main.ExamCategoryRepository;
import com.stadio.model.repository.main.ExamRepository;
import com.stadio.model.repository.main.TeacherRepository;
import lombok.Data;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Andy on 02/08/2018.
 */
@Data
public class CategoryDetailsDTO {

    private String id;
    private String name;
    private String summary;
    private int position;
    private String imageUrl;
    private Boolean isBookmarked;
    private long bookmarkCount;
    private List<ExamItemDTO> examList = new ArrayList<>();
    private long price;
    private long priceOld;
    private Boolean isRegistered;
    private Boolean isLiked;
    private long likeCount;
    private long registeredCount;
    private Boolean hasCorrectionDetail;
    private long submittedCount;
    private TeacherDTO teacher;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "GMT+7")
    private Date createdDate;

    public static CategoryDetailsDTO with(Category category) {

        CategoryDetailsDTO categoryDetailsDTO = new CategoryDetailsDTO();
        categoryDetailsDTO.setId(category.getId());
        categoryDetailsDTO.setName(category.getName());
        categoryDetailsDTO.setSummary(category.getSummary());
        categoryDetailsDTO.setPosition(category.getPosition());
        categoryDetailsDTO.setImageUrl(category.getImageUrl());
        categoryDetailsDTO.setPrice(category.getPrice());
        categoryDetailsDTO.setPriceOld(category.getPriceOld());
        categoryDetailsDTO.setCreatedDate(category.getCreatedDate());
        categoryDetailsDTO.setHasCorrectionDetail(category.getHasCorrectionDetail());

        return categoryDetailsDTO;
    }

    public void with(ExamRepository examRepository, ExamCategoryRepository examCategoryRepository, int page, int pageSize) {

        if (examCategoryRepository == null) {
            return;
        }

        List<ExamItemDTO> examList = new ArrayList<>();

        PageRequest pageRequest = new PageRequest(page, pageSize);
        List<ExamCategory> examCategories = examCategoryRepository.findAllByCategoryId(this.getId(), pageRequest).getContent();

        List<String> examIds = examCategories.stream().map(x -> x.getExamId()).collect(Collectors.toList());
        if (!HelperUtils.isEmptyArray(examIds)) {
            for (String examId: examIds) {
                Exam exam = examRepository.findOne(examId);
                if (exam != null) {
                    examList.add(ExamItemDTO.with(exam));
                }
            }
        }

        this.examList = examList;
    }

    public void with(TeacherRepository teacherRepository, String teacherId) {

        if (teacherRepository == null || !StringUtils.isNotNull(teacherId)) {
            return;
        }

        Teacher teacher = teacherRepository.findOne(teacherId);
        if (teacher != null) {
            this.teacher = new TeacherDTO(teacher);
        }

        this.examList = examList;
    }


    public CategoryDetailsDTO() {
        this.isRegistered = Boolean.FALSE;
        this.isLiked = Boolean.FALSE;
        this.hasCorrectionDetail = Boolean.FALSE;
    }
}
