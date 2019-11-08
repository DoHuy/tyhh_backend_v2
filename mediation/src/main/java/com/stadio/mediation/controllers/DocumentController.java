package com.stadio.mediation.controllers;

import com.stadio.common.service.IStorageService;
import com.stadio.common.utils.ResponseCode;
import com.stadio.mediation.response.ResponseResult;
import com.stadio.mediation.service.IDocumentService;
import com.stadio.model.documents.MediaDocument;
import com.stadio.model.dtos.cms.MediaDocumentDTO;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("api/media/document")
public class DocumentController extends BaseController {

    @Autowired
    IStorageService storageService;

    @Value("${domain.mediation}")
    private String domainMedia;

    @Autowired
    IDocumentService documentService;

    private Logger logger = LogManager.getLogger(ImageController.class);

    @GetMapping(value = "/{document:.+}", produces = {"application/docx", "application/pdf", "application/xlsx", "application/xls"})
    public @ResponseBody
    ResponseEntity<Resource> getDocument(@PathVariable String document)
    {
        Resource file = null;
        String extention = "pdf";
        try {
            file = documentService.loadDocumentsAsResource(document,false);
        } catch (Exception e) {
            logger.error("StorageFileNotFoundException: ", e);
        }
        try {
            extention = FilenameUtils.getExtension(file.getFilename());
        } catch (Exception e){}

        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + file.getFilename())
                .body(file);
    }

    @GetMapping(value = "/secret/{document:.+}", produces = {"application/docx", "application/pdf", "application/xlsx", "application/xls"})
    public @ResponseBody
    ResponseEntity<Resource> getSecretDocument(@PathVariable String document)
    {
        Resource file = null;
        String extention = "pdf";
        try {
            file = documentService.loadDocumentsAsResource(document,true);
        } catch (Exception e) {
            logger.error("StorageFileNotFoundException: ", e);
        }
        try {
            extention = FilenameUtils.getExtension(file.getFilename());
        } catch (Exception e){}

        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + file.getFilename())
                .body(file);
    }

    @PostMapping(value = "/upload")
    public ResponseResult singleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isSecret") boolean isSecret
    ) throws Exception {

        if (file.isEmpty())
        {
            return ResponseResult.newInstance(ResponseCode.FILE_NOT_EXIST, getMessage("mediation.file_not_exist"), null);
        }

        String fileInfo = documentService.processSaveToStorage(file, isSecret);

        if (null != fileInfo)
        {
            MediaDocumentDTO itemDTO = new MediaDocumentDTO();

            try
            {
//                String url = this.domainMedia + "/api/media/document";
                String url = "";
                try {
                    if (isSecret) {
                        url = this.domainMedia + "/" + this.getClass().getAnnotation(RequestMapping.class).value()[0] + "/secret/" + fileInfo;
                    } else {
                        url = this.domainMedia + "/" + this.getClass().getAnnotation(RequestMapping.class).value()[0] + "/" + fileInfo;
                    }
                } catch (Exception e) {
                }

                logger.info("Upload URL Builder: {}", url);

                MediaDocument mediaDocument = documentService.processExtractMedia(file, url);

                itemDTO.setNativeInfo(mediaDocument);
                SimpleDateFormat fm = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                itemDTO.setCreatedDate(fm.format(new Date()));
                itemDTO.setUrl(url);
                itemDTO.setId(mediaDocument.getId());
            }
            catch (Exception e)
            {
                logger.error("Insert database exception: ", e);
                return ResponseResult.newInstance(ResponseCode.MULTIPART_EXCEPTION, e.getMessage(), null);
            }

            return ResponseResult.newInstance(ResponseCode.SUCCESS, "", itemDTO);
        }
        else
        {
            return ResponseResult.newInstance(ResponseCode.MULTIPART_EXCEPTION, "Upload fail", null);
        }

    }
}
