package com.stadio.es.service;

import com.stadio.es.response.ResponseResult;
import org.springframework.stereotype.Service;


public interface IChemicalEquationService {
    ResponseResult balancer(String chemicalEquation);

    ResponseResult create(String content);

    ResponseResult remove(String id);
}
