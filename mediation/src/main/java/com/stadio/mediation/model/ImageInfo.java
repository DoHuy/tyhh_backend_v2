package com.stadio.mediation.model;

import lombok.Data;

@Data
public class ImageInfo {

    private String fileName;

    private String fileNameThumb;

    public ImageInfo(String fileName, String fileNameThumb) {
        this.fileName = fileName;
        this.fileNameThumb = fileNameThumb;
    }
}
