package com.stadio.mobi.service;

import com.stadio.mobi.response.ResponseResult;
import com.stadio.model.documents.*;import com.hoc68.users.documents.User;

import java.util.List;

/**
 * Created by Andy on 01/09/2018.
 */
public interface ISearchService
{
    List<SearchKeywords> getKeywords();

    ResponseResult processSearch(String keyword);
}
