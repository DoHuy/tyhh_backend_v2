package com.stadio.model.dtos.mobility;

import com.stadio.common.enu.DeepLink;
import com.stadio.model.documents.Banner;
import lombok.Data;

@Data
public class BannerItemDTO {

    private String id;

    private String imageIdRef;

    private String actionUrl;

    private String imageUrl;

    private double startTime;

    private double endTime;

    private DeepLink deepLink;

    private String payload;

    public static BannerItemDTO newInstance(Banner banner){
        return new BannerItemDTO(banner);
    }

    public BannerItemDTO(Banner banner){
        this.id = banner.getId();
        this.imageIdRef = banner.getImageIdRef();
        this.actionUrl = banner.getActionUrl();
        this.imageUrl = banner.getImageUrl();
        try {
            this.setStartTime(banner.getStartTime().getTime() / 1000);
            this.setEndTime(banner.getEndTime().getTime() / 1000);
        } catch (Exception e) {}

        if(this.actionUrl==null) this.actionUrl="";
        this.deepLink = banner.getDeepLink();
        this.payload = banner.getPayload();
    }

    public BannerItemDTO(){

    }
}
