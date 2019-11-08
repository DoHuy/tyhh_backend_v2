package com.stadio.model.dtos.cms;

import com.stadio.model.documents.Image;
import lombok.Data;

@Data
public class ImageGalleryDetailDTO {

    private String id;

    private String name;

    private String url;

    public ImageGalleryDetailDTO(Image image){
        this.id = image.getId();
        this.name = image.getName();
        this.url = image.getUrl();
    }
}
