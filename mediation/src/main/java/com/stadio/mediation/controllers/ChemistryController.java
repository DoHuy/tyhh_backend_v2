package com.stadio.mediation.controllers;

import com.stadio.mediation.service.IChemistryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/chemistry")
public class ChemistryController  extends BaseController {

    @Autowired
    IChemistryService chemistryService;

    private Logger logger = LogManager.getLogger(ChemistryController.class);

    @GetMapping(value = "/periodic/{photo:.+}", produces = {"image/jpg", "image/jpeg", "image/png", "image/gif"})
    public @ResponseBody ResponseEntity<Resource> elements(@PathVariable String photo) {
        Resource file = chemistryService.loadImagesAsResource(photo);
        return ResponseEntity.ok().body(file);
    }

}
