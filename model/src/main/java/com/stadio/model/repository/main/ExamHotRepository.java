package com.stadio.model.repository.main;

import com.stadio.model.documents.ExamHot;
import com.stadio.model.enu.TopType;
import com.stadio.model.repository.main.custom.ExamHotRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExamHotRepository extends MongoRepository<ExamHot, String>, ExamHotRepositoryCustom
{
    ExamHot findExamHotByExamIdRefAndAndTopType(String examIdRef, TopType topType);

    List<ExamHot> findByTopTypeOrderByPositionAsc(TopType topType);
}
