package com.stadio.model.es.dtos;

import com.stadio.common.utils.JsonUtils;
import com.stadio.model.es.model.Category;
import com.stadio.model.es.model.Exam;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
    private List<ExamItemDTO> examList = new ArrayList<>();

    public static CategoryDetailsDTO with(String json) {
        Category category = JsonUtils.parse(json, Category.class);

        CategoryDetailsDTO categoryDetailsDTO = new CategoryDetailsDTO();
        categoryDetailsDTO.setId(category.getId());
        categoryDetailsDTO.setName(category.getName());
        categoryDetailsDTO.setSummary(category.getSummary());
        categoryDetailsDTO.setPosition(category.getPosition());
        categoryDetailsDTO.setImageUrl(category.getImageUrl());

        if (category.getExamIds() != null) {
            for (Exam exam : category.getExamIds()) {
                if (exam == null) continue;
                ExamItemDTO examItemDTO = ExamItemDTO.with(JsonUtils.writeValue(exam));
                categoryDetailsDTO.getExamList().add(examItemDTO);
            }
        }
        return categoryDetailsDTO;
    }
}
