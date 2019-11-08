package com.stadio.mobi.dtos;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ChapterItemDTO
{
    private String id;
    private String chapterName;
    private Map<String, String> actions = new HashMap<>();
}
