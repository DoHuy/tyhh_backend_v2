package com.stadio.model.dtos.cms;

import com.stadio.model.documents.Image;
import lombok.Data;

/**
 * Created by Andy on 12/03/2017.
 */
@Data
public class ImageItemDTO
{
    private String id;
    private Image imageNative;
    private Image imageThumbnail;
    private String createdDate;
    private String imageUrl;
    private String imageUri;
    private String imageThumbnailUrl;
}
