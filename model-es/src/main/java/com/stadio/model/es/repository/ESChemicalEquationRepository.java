package com.stadio.model.es.repository;

import com.stadio.model.es.documents.ESChemicalEquation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ESChemicalEquationRepository extends ElasticsearchRepository<ESChemicalEquation,String> {
    Page<ESChemicalEquation> findByContentMatches(String content, Pageable pageable);

    List<ESChemicalEquation> findByContent(String content);
}
