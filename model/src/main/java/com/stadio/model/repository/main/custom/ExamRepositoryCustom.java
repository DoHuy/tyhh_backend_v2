package com.stadio.model.repository.main.custom;

import com.stadio.model.documents.Exam;
import java.util.List;
import java.util.Map;

/**
 * Created by sm on 12/7/17.
 */
public interface ExamRepositoryCustom {

    Long searchExamQuantity(Map examSearch);

    List<Exam> findExamByPage(Integer page, Integer pageSize,Map examSearch);

    List<Exam> findExamByPage(Integer page, Integer pageSize);

    List<Exam> fullTextSearch(String q, int limit, int offset);
}
