package com.stadio.model.dtos.cms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stadio.common.enu.DeepLink;
import com.stadio.model.documents.Banner;
import lombok.Data;

import java.util.Date;

@Data
public class BannerListDTO
{

    private String id;

    private String name;

    private String summary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date updatedDate;

    private String createdBy;

    private String updatedBy;

    private String imageIdRef;

    private String detailUrl;

    private String imageUrl;

    private Boolean isShow;

    private long startTime;

    private long endTime;

    private DeepLink deepLink;

    private String payload;

    public static BannerListDTO newInstance(Banner banner) {
        return new BannerListDTO(banner);
    }

    public BannerListDTO(Banner banner) {
        this.setId(banner.getId());
        this.setName(banner.getName());
        this.setImageIdRef(banner.getImageIdRef());
        this.setImageUrl(banner.getImageUrl());
        this.setUpdatedDate(banner.getUpdatedDate());
        this.setCreatedDate(banner.getCreatedDate());
        try {
            this.setStartTime(banner.getStartTime().getTime());
            this.setEndTime(banner.getEndTime().getTime());
        } catch (Exception e) {}
        this.deepLink = banner.getDeepLink();
        this.payload = banner.getPayload();
        this.isShow = banner.isEnable();
    }
}
