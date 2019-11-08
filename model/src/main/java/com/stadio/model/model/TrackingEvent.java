package com.stadio.model.model;

import com.stadio.model.enu.ActionEvent;
import lombok.Data;

@Data
public class TrackingEvent {

    private ActionEvent event;
    private String message;
    private String objectId;

    public TrackingEvent() {}

    public TrackingEvent(ActionEvent event, String message, String objectId) {
        this.event = event;
        this.message = message;
        this.objectId = objectId;
    }
}
