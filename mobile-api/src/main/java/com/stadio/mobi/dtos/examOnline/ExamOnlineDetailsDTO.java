package com.stadio.mobi.dtos.examOnline;

import lombok.Data;

import java.util.*;

@Data
public class ExamOnlineDetailsDTO {

    private String id;
    private String name;
    private String description;
    private String avatar;
    private String code;
    private int price;
    private Date startTime;
    private Date endTime;
    private long remainingTime;
    private int totalJoin;
    private boolean isJoined;
    private long likes;
    private boolean isLiked;
    private int questionQuantity;
    private long totalComment;
    private String examId;

    private List<JoinerDTO> userList = new ArrayList<>();

    private Map<String, String> action = new HashMap<>();
}
