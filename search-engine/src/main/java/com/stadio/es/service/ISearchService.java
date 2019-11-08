package com.stadio.es.service;

import com.stadio.es.response.ResponseResult;
import org.springframework.stereotype.Service;


public interface ISearchService {
    ResponseResult processSearchExam(String text);

    ResponseResult processSearchChemicalEquation(String content);
}
