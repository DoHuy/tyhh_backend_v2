package com.stadio.model.model;

import com.stadio.model.enu.ActionEvent;
import lombok.Data;

@Data
public class ExamManagerEvent {
    private ActionEvent event;
    private String object;
    private String message;

    public ExamManagerEvent(){
         message = "";
    }

    public ExamManagerEvent(ActionEvent event, String message, String object){
        this.event = event;
        this.message = message;
        this.object = object;
    }
}
