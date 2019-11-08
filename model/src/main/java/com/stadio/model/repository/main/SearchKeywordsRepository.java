package com.stadio.model.repository.main;

import com.stadio.model.documents.SearchKeywords;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Andy on 01/09/2018.
 */
public interface SearchKeywordsRepository extends MongoRepository<SearchKeywords, String>
{
}
