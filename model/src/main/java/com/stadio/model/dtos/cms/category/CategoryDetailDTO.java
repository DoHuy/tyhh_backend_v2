package com.stadio.model.dtos.cms.category;

import com.stadio.model.documents.Category;
import com.stadio.model.documents.Exam;
import com.stadio.model.documents.ExamCategory;
import com.stadio.model.repository.main.ExamCategoryRepository;
import com.stadio.model.repository.main.ExamRepository;
import lombok.Data;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CategoryDetailDTO {

    private String id;

    private String name;

    private String summary;

    private int position;

    private String imageUrl;

    private String createdBy;

    private String updatedBy;

    private List<Exam> exams;

    private Date createdDate;

    private Date updatedDate;

    private long price;

    private long priceOld;

    private Boolean hasCorrectionDetail;

    private String teacherId;

    public CategoryDetailDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.summary = category.getSummary();
        this.position = category.getPosition();
        this.imageUrl = category.getImageUrl();
        this.createdBy = category.getCreatedBy();
        this.updatedBy = category.getUpdatedBy();
        this.createdDate = category.getCreatedDate();
        this.updatedDate = category.getUpdatedDate();
        this.price = category.getPrice();
        this.priceOld = category.getPriceOld();
        this.hasCorrectionDetail = category.getHasCorrectionDetail();
        this.teacherId = category.getTeacherId();
    }

    public void with(ExamCategoryRepository examCategoryRepository, ExamRepository examRepository) {
        List<Exam> exams = new ArrayList<>();
        List<ExamCategory> examCategories = examCategoryRepository.findAllByCategoryId(this.getId(), new PageRequest(0, Integer.MAX_VALUE)).getContent();
        for (ExamCategory examCategory: examCategories) {
            Exam exam = examRepository.findOne(examCategory.getExamId());
            if (exam != null) {
                exams.add(exam);
            }
        }
        this.exams = exams;
    }

}
