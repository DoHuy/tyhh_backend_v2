package com.stadio.cms.service;

import com.stadio.model.documents.SearchKeywords;

import java.util.List;

/**
 * Created by Andy on 01/09/2018.
 */
public interface ISearchService
{
    SearchKeywords processSaveKeyWord(String keyword);

    List<SearchKeywords> getKeywords();

    void processDeleteKeyword(String id);
}
