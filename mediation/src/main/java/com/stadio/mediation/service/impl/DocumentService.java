package com.stadio.mediation.service.impl;

import com.stadio.common.enu.FolderName;
import com.stadio.common.service.IStorageService;
import com.stadio.common.utils.StringUtils;
import com.stadio.mediation.service.IDocumentService;
import com.stadio.model.documents.MediaDocument;
import com.stadio.model.repository.main.MediaDocumentRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Path;

@Service
public class DocumentService implements IDocumentService {

    private Logger logger = LogManager.getLogger(DocumentService.class);

    @Autowired
    IStorageService storageService;

    @Autowired
    MediaDocumentRepository mediaDocumentRepository;

    private static String secretPath = "secret";

    @Override
    public Resource loadDocumentsAsResource(String doc, boolean isSecret)
    {
        try
        {
            String fname = FilenameUtils.removeExtension(doc);
            String subFolder = doc.substring(fname.length() - 2, fname.length());
            Path p = storageService.getLocation(FolderName.DOCUMENTS).resolve(subFolder);

            if (isSecret) {
                p = storageService.getLocation(FolderName.DOCUMENTS).resolve(secretPath).resolve(subFolder);
            }

            logger.info("Loading document from: " + p.toUri());
            return storageService.loadAsResource(p, doc);
        }
        catch (Exception e)
        {
            logger.error("loadDocumentsAsResource error md5_hex: ", e);
        }

        return null;
    }


    @Override
    public String processSaveToStorage(MultipartFile multipartFile, boolean isSecret)
    {
        Path documentLocation = storageService.getLocation(FolderName.DOCUMENTS);
        try
        {
            String encodeFileName = StringUtils.md5_hex(multipartFile.getOriginalFilename() + "#" + System.currentTimeMillis());
            String subFolder = encodeFileName.substring(encodeFileName.length() - 2, encodeFileName.length());
            String extentionStr = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            String encodeFile = encodeFileName + "." + extentionStr;

            logger.info("Prepare save file with name: {}", encodeFile);

            Path path = documentLocation.resolve(subFolder);
            if (isSecret) {
                path = documentLocation.resolve(secretPath).resolve(subFolder);
            }

            storageService.store(path, multipartFile, encodeFile);

            return encodeFile;
        }
        catch (Exception e)
        {
            logger.error("processSaveToStorage Exception:" , e);
        }
        return null;
    }

    public MediaDocument processExtractMedia(MultipartFile file, String url)
    {
        try
        {
            MediaDocument doc = new MediaDocument();

            BufferedImage img = ImageIO.read(file.getInputStream());
            doc.setName(file.getOriginalFilename());
            doc.setUrl(url);

            mediaDocumentRepository.save(doc);

            return doc;
        }
        catch (Exception e)
        {
            logger.debug("Exception processExtractMedia: ", e);
        }
        return null;
    }

}
