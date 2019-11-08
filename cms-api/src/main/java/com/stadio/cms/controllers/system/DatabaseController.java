package com.stadio.cms.controllers.system;

import com.stadio.cms.controllers.BaseController;
import com.stadio.cms.response.ResponseResult;
import com.stadio.cms.service.IDatabaseService;
import com.stadio.cms.service.IDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/database")
public class DatabaseController extends BaseController
{

    @Autowired
    IDatabaseService databaseService;

    @Autowired
    IDocumentService documentService;

    @GetMapping(value = "/collections")
    public ResponseEntity getCollectionList() {
        ResponseResult result = databaseService.processGetListCollection();
        return ResponseEntity.ok(result);
    }

//    @GetMapping(value = "/updateFeatures")
//    public ResponseEntity updateFeatures() {
//        ResponseResult result = documentService.processUpdateListFeatureToDB();
//        return ResponseEntity.ok(result);
//    }
}
