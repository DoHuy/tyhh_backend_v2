package com.stadio.mediation.service;

import com.stadio.mediation.model.ImageInfo;
import com.stadio.model.documents.Image;
import com.stadio.model.dtos.cms.ImageGalleryDetailDTO;
import com.stadio.model.dtos.cms.ImageItemDTO;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.util.List;

/**
 * Created by Andy on 12/23/2017.
 */
@Service
public interface IImageService
{
    void processSave(Image image);

    Image processExtractImage(MultipartFile file, String url, String urlThumbnail);

    List<Image> findTopByCreatedDate();

    ImageInfo processSaveToStorage(MultipartFile multipartFile);

    Resource loadImagesAsResource(String photo);

    List<ImageGalleryDetailDTO> listImage(int page, int pagesize);

}
