package com.stadio.cms.service;

import com.stadio.cms.model.ApiDocument;
import com.stadio.cms.response.ResponseResult;
import com.stadio.common.utils.TypedMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 11/10/2017.
 */
public interface IDocumentService
{
    Map<String, List<ApiDocument>> processGetListApi(String pkgName);

    Map<String, List<ApiDocument>> processGetListApi(String pkgName, TypedMapper<ApiDocument> tf);

    ResponseResult processGetListFeature();

    ResponseResult processUpdateListFeatureToDB();

}
