package com.stadio.transaction.controllers;

import com.stadio.transaction.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/import")
public class ImportController {

    @Autowired
    TransactionService transactionService;

    @GetMapping(value = "/user20k")
    public ResponseEntity requestUser20k() {
        transactionService.import20kToUser();
        return ResponseEntity.ok("OK");
    }

}
