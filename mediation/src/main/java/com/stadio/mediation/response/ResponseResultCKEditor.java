package com.stadio.mediation.response;

import lombok.Data;

@Data
public class ResponseResultCKEditor {
    private Integer uploaded;
    private String fileName;
    private String url;
    private String error;

    public ResponseResultCKEditor(Boolean uploaded, String url, String fileName, String error ) {
        this.uploaded = uploaded ? 1 : 0;
        this.url = url;
        this.fileName = fileName;
    }

    public static <DTO> ResponseResultCKEditor newInstance(Boolean uploaded, String url, String fileName, String error ) {
        return new ResponseResultCKEditor(uploaded, url, fileName, error);
    }

}