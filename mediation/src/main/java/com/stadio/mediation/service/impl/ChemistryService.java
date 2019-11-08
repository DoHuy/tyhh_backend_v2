package com.stadio.mediation.service.impl;

import com.stadio.common.enu.FolderName;
import com.stadio.common.service.impl.StorageService;
import com.stadio.mediation.service.IChemistryService;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.sql.rowset.BaseRowSet;
import java.nio.file.Path;

@Service
public class ChemistryService extends BaseRowSet implements IChemistryService {

    @Autowired
    StorageService storageService;

    private Logger logger = LogManager.getLogger(ChemistryService.class);

    @Override
    public Resource loadImagesAsResource(String photo)
    {
        try
        {
            Path p = storageService.getLocation(FolderName.DOCUMENTS).resolve("periodic");
            logger.info("Loading image from: " + p.toUri());
            return storageService.loadAsResource(p, photo);
        }
        catch (Exception e)
        {
            logger.error("loadImagesAsResource error md5_hex: ", e);
        }

        return null;
    }

}
