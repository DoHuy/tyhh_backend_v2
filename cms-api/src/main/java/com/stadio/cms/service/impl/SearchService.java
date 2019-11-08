package com.stadio.cms.service.impl;

import com.stadio.cms.service.ISearchService;
import com.stadio.model.documents.SearchKeywords;
import com.stadio.model.repository.main.SearchKeywordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Andy on 01/09/2018.
 */
@Service
public class SearchService implements ISearchService
{
    @Autowired
    public SearchKeywordsRepository searchKeywordsRepository;

    @Override
    public SearchKeywords processSaveKeyWord(String key)
    {
        SearchKeywords searchKeywords = new SearchKeywords();
        searchKeywords.setKeyword(key);
        searchKeywords.setTotalSearch(0);
        searchKeywordsRepository.save(searchKeywords);
        return searchKeywords;
    }

    @Override
    public List<SearchKeywords> getKeywords()
    {
        return searchKeywordsRepository.findAll();
    }

    @Override
    public void processDeleteKeyword(String id)
    {
        searchKeywordsRepository.delete(id);
    }
}
