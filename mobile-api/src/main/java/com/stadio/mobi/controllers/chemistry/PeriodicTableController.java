package com.stadio.mobi.controllers.chemistry;

import com.stadio.mobi.controllers.BaseController;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.model.repository.chemistry.PeriodicTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/periodic")
public class PeriodicTableController extends BaseController {

    @Autowired
    PeriodicTableRepository periodicTableRepository;

    @GetMapping(value = "/table")
    public ResponseEntity periodicTable() {
        ResponseResult result = ResponseResult.newSuccessInstance(periodicTableRepository.findAll());
        return ResponseEntity.ok(result);
    }
}
