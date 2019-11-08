package com.stadio.cms.dtos.examOnline;

import lombok.Data;

@Data
public class JoinerItemDTO {

    private String id;
    private String userCode;
    private String username;
    private String joinTime;
    private boolean receiveEmail;
    private boolean receiveMessage;

}
