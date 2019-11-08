package com.stadio.mediation.controllers;

import com.stadio.common.utils.ResponseCode;
import com.stadio.mediation.model.ImageInfo;
import com.stadio.mediation.response.ResponseResult;
import com.stadio.mediation.response.ResponseResultCKEditor;
import com.stadio.mediation.service.IImageService;
import com.stadio.common.service.IStorageService;
import com.stadio.model.documents.Image;
import com.stadio.model.dtos.cms.ImageGalleryDetailDTO;
import com.stadio.model.dtos.cms.ImageItemDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Andy on 12/03/2017.
 */
@RestController
@RequestMapping("api/media")
public class ImageController extends BaseController
{

    @Autowired IStorageService storageService;

    @Value("${domain.mediation}")
    private String domainMedia;

    @Autowired
    IImageService imageService;

    private Logger logger = LogManager.getLogger(ImageController.class);

    @GetMapping(value = "/images/{photo:.+}", produces = {"image/jpg", "image/jpeg", "image/png", "image/gif"})
    public @ResponseBody
    ResponseEntity<Resource> imageView(@PathVariable String photo)
    {

        Resource file = null;
        try
        {
            file = imageService.loadImagesAsResource(photo);
        }
        catch (Exception e)
        {
            logger.error("StorageFileNotFoundException: ", e);
        }
        return ResponseEntity.ok().body(file);
    }

    @PostMapping(value = "/uploadCK")
    public ResponseResultCKEditor singleFileUploadCK(@RequestParam("upload") MultipartFile file)
            throws Exception
    {
        ResponseResult result = this.singleFileUpload(file);
        try
        {
            if (ResponseCode.SUCCESS.equals(result.getErrorCode()) && result != null)
            {
                ImageItemDTO imageItemDTO = (ImageItemDTO) result.getData();
                return ResponseResultCKEditor.newInstance(true, imageItemDTO.getImageUrl(), "", "");
            }
            else
            {
                return ResponseResultCKEditor.newInstance(false, "", "", "");
            }
        }
        catch (Exception e)
        {
            return ResponseResultCKEditor.newInstance(false, "", "", "");

        }
    }

    @PostMapping(value = "/upload-image")
    public ResponseResult singleFileUpload(@RequestParam("file") MultipartFile file)
            throws Exception
    {
        if (file.isEmpty())
        {
            return ResponseResult.newInstance(ResponseCode.FILE_NOT_EXIST, getMessage("mediation.file_not_exist"), null);
        }

        ImageInfo imageInfo = imageService.processSaveToStorage(file);

        if (null != imageInfo.getFileName())
        {
            ImageItemDTO itemDTO = new ImageItemDTO();

            try
            {

                final String url = this.domainMedia + "/api/media/images/" + imageInfo.getFileName();
                final String urlThumbnail = this.domainMedia + "/api/media/images/" + imageInfo.getFileNameThumb();

                logger.info("Upload URL Builder: {}", url);

                Image image = imageService.processExtractImage(file, url, urlThumbnail);

                itemDTO.setImageNative(image);
                SimpleDateFormat fm = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                itemDTO.setCreatedDate(fm.format(new Date()));
                itemDTO.setImageUrl(url);
                itemDTO.setId(image.getId());
                itemDTO.setImageThumbnailUrl(urlThumbnail);
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

    @GetMapping("/images")
    public ResponseResult listImages()
    {
        return ResponseResult.newInstance("00", "", imageService.findTopByCreatedDate());
    }

    @GetMapping("/list")
    public ResponseResult listUploadedFiles() throws IOException
    {

        List<String> items = storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(ImageController.class,
                        "imageView", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList());

        return ResponseResult.newInstance("00", "", items);
    }

    @GetMapping("/list-image")
    public ResponseResult listImageUploaded(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "pageSize") int pageSize
    ) throws IOException
    {

        List<ImageGalleryDetailDTO> imageList = imageService.listImage(page,pageSize);

        return ResponseResult.newInstance("00", "", imageList);
    }

}
