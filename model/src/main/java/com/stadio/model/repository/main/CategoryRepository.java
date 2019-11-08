package com.stadio.model.repository.main;

import com.stadio.model.documents.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String>
{
    List<Category> findAllByExamIdsIsNotNull();

    Page<Category> findAllByIdNotIn(List<String> ids, Pageable pageable);

}
