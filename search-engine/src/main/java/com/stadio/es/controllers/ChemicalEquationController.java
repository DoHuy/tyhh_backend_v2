package com.stadio.es.controllers;

import com.stadio.es.response.ResponseResult;
import com.stadio.es.service.IChemicalEquationService;
import com.stadio.es.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/chemical-equation")
public class ChemicalEquationController {

    @Autowired
    ISearchService searchService;

    @Autowired
    IChemicalEquationService chemicalEquationService;

    @GetMapping(value = "/balancer")
    public ResponseResult balancer(@RequestParam(value = "chemicalEquation") String chemicalEquation){
        return chemicalEquationService.balancer(chemicalEquation);
    }

    @PostMapping(value = "/create")
    public ResponseResult create(@RequestParam(value = "content") String content ){
        return chemicalEquationService.create(content);
    }

    @PostMapping(value = "/remove")
    public ResponseResult remove(@RequestParam(value = "id") String id ){
        return chemicalEquationService.remove(id);
    }

}
