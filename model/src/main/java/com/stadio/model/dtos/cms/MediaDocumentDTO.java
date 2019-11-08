package com.stadio.model.dtos.cms;

import com.stadio.model.documents.Image;
import com.stadio.model.documents.MediaDocument;
import lombok.Data;

/**
 * Created by Andy on 12/03/2017.
 */
@Data
public class MediaDocumentDTO
{
    private String id;
    private MediaDocument nativeInfo;
    private String createdDate;
    private String url;
    private String uri;
}
