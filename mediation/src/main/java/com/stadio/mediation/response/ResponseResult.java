package com.stadio.mediation.response;

import lombok.Data;

/**
 * Created by Andy on 11/08/2017.
 */
@Data
public class ResponseResult<DTO>
{
    private String requestId;
    private String errorCode;
    private String message;
    private DTO data;

    public ResponseResult() {
    }

    public ResponseResult(String errorCode, String msg) {
        this.requestId = "";
        this.errorCode = errorCode;
        this.message = msg;
    }

    public ResponseResult(String errorCode, String msg, DTO data) {
        this.requestId = "";
        this.errorCode = errorCode;
        this.message = msg;
        this.data = data;
    }


    public static<DTO> ResponseResult newInstance(String errorCode, String msg, DTO data) {
        return new ResponseResult<>(errorCode, msg, data);
    }


    public static<DTO> ResponseResult newSuccessInstance(DTO data)
    {
        return new ResponseResult<>("00", "SUCCESS", data);
    }

    public static ResponseResult newErrorInstance(String errorCode, String msg)
    {
        return newInstance(errorCode, msg, null);
    }

}
