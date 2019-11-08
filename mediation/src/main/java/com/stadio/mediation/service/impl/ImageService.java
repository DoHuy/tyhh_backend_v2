package com.stadio.mediation.service.impl;

import com.stadio.mediation.model.ImageInfo;
import com.stadio.mediation.service.IImageService;
import com.stadio.common.enu.FolderName;
import com.stadio.common.service.IStorageService;
import com.stadio.common.utils.StringUtils;
import com.stadio.mediation.utils.ImageResizer;
import com.stadio.model.documents.Image;
import com.stadio.model.dtos.cms.ImageGalleryDetailDTO;
import com.stadio.model.repository.main.ImageRepository;
import com.stadio.model.repository.main.ImageRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andy on 12/04/2017.
 */
@Service
public class ImageService implements IImageService
{
    private Logger logger = LogManager.getLogger(ImageService.class);

    @Autowired private ImageRepository imageRepository;

    @Autowired IStorageService storageService;

    @Override
    public void processSave(Image image)
    {
        imageRepository.save(image);
    }

    public Image processExtractImage(MultipartFile file, String url, String urlThumbnail)
    {
        try
        {
            Image image = new Image();

            BufferedImage img = ImageIO.read(file.getInputStream());
            image.setName(file.getOriginalFilename());
            image.setHeight(img.getHeight());
            image.setWidth(img.getWidth());
            image.setUrl(url);
            image.setUrlThumb(urlThumbnail);
            this.processSave(image);

            return image;
        }
        catch (Exception e)
        {
            logger.debug("Exception processExtractImage: ", e);
        }
        return null;
    }

    @Override
    public List<Image> findTopByCreatedDate()
    {
        return imageRepository.findTopByCreatedDate();
    }

    @Override
    public ImageInfo processSaveToStorage(MultipartFile multipartFile)
    {
        Path imageLocation = storageService.getLocation(FolderName.IMAGES);
        try
        {
            String encodeFileName = StringUtils.md5_hex(multipartFile.getOriginalFilename() + "#" + System.currentTimeMillis());
            String subFolder = encodeFileName.substring(encodeFileName.length() - 2, encodeFileName.length());
            String extentionStr = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            String encodeFile = encodeFileName + "." + extentionStr;

            logger.info("Prepare save file with name: {}", encodeFile);

            Path path = imageLocation.resolve(subFolder);

            storageService.store(path, multipartFile, encodeFile);

            // Store thumbnail
            String fileNameThumb = null;
//            try {
//
//                final String thumbExtention = "thumb";
//
//                fileNameThumb = encodeFileName + "." + thumbExtention;
//
//                BufferedImage cropedImage = new ImageResizer().createThumbnailCopy(multipartFile.getBytes());
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                ImageIO.write( cropedImage, extentionStr, baos );
//                baos.flush();
//
//                MultipartFile thumbnail = new MockMultipartFile(fileNameThumb, baos.toByteArray());
//
//                storageService.store(path, thumbnail, fileNameThumb);
//                baos.close();
//            } catch (Exception e) {
//                logger.error("createThumbnail fail", e);
//            }

            return new ImageInfo(encodeFile, fileNameThumb);
        }
        catch (Exception e)
        {
            logger.error("processSaveToStorage Exception:" , e);
        }
        return null;
    }

    @Override
    public Resource loadImagesAsResource(String photo)
    {
        try
        {
            String fname = FilenameUtils.removeExtension(photo);
            String subFolder = photo.substring(fname.length() - 2, fname.length());
            Path p = storageService.getLocation(FolderName.IMAGES).resolve(subFolder);
            logger.info("Loading image from: " + p.toUri());
            return storageService.loadAsResource(p, photo);
        }
        catch (Exception e)
        {
            logger.error("loadImagesAsResource error md5_hex: ", e);
        }

        return null;
    }

    @Override
    public List<ImageGalleryDetailDTO> listImage(int page, int pagesize) {
        List<Image> imageList =  imageRepository.findAll(new PageRequest(page-1,pagesize)).getContent();
        List<ImageGalleryDetailDTO> imageGalleryDetailDTOList = new LinkedList<>();
        imageList.stream().forEach(image -> {
            imageGalleryDetailDTOList.add(new ImageGalleryDetailDTO(image));
        });
        return imageGalleryDetailDTOList;
    }

}
