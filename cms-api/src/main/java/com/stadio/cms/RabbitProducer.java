package com.stadio.cms;

import com.stadio.common.define.Constant;
import com.stadio.model.enu.ActionEvent;
import com.stadio.model.model.ExamManagerEvent;
import com.stadio.model.model.TrackingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andy on 02/26/2018.
 */
@Component
public class RabbitProducer
{
    @Autowired RabbitTemplate rabbitTemplate;

    private Logger logger = LogManager.getLogger(RabbitProducer.class);

    private ObjectMapper mapper = new ObjectMapper();

    private SimpleDateFormat fm = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

    public void sendNotification(String message) {
        rabbitTemplate.setExchange(Constant.EXCHANGE_NAME.NOTIFICATION);
        rabbitTemplate.setQueue(Constant.QUEUE_NAME.NOTIFICATION);
        rabbitTemplate.setRoutingKey(Constant.QUEUE_NAME.NOTIFICATION);
        rabbitTemplate.convertAndSend(message);
    }

    public void sendTrackingEvent(String queue, ActionEvent event, String objectId) {
        try {

            TrackingEvent trackingEvent = new TrackingEvent();
            trackingEvent.setEvent(event);
            trackingEvent.setMessage(event.name() + " -> " + queue + " -> " + fm.format(new Date()));
            trackingEvent.setObjectId(objectId);
            String msg = mapper.writeValueAsString(trackingEvent);
            logger.info("Tracking Event: " + msg);
            rabbitTemplate.setExchange(Constant.EXCHANGE_NAME.TRACKING_EVENT);
            rabbitTemplate.setQueue(queue);
            rabbitTemplate.convertAndSend(".chemistry.trackingEvent.examOnline", msg);
        } catch (IOException e) {
            logger.error("sendTrackingEvent: ", e);
        }

    }

    public void sendExamManagerEvent(ActionEvent event,String object){
        try{
            final String queue = Constant.QUEUE_NAME.EXAM_MANAGER;
            ExamManagerEvent examManagerEvent = new ExamManagerEvent();
            examManagerEvent.setEvent(event);
            examManagerEvent.setObject(object);
            examManagerEvent.setMessage(event.name() + " -> " + queue + " -> " + fm.format(new Date()));
            String msg = mapper.writeValueAsString(examManagerEvent);
            logger.info("Tracking Event: " + msg);
            rabbitTemplate.setExchange(Constant.EXCHANGE_NAME.EXAM_MANAGER);
            rabbitTemplate.setQueue(queue);
            rabbitTemplate.convertAndSend(msg);
        }catch (Exception e){
            logger.error("examManagerEvent: "+e);
        }
    }

}
