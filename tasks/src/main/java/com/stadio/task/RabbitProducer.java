package com.stadio.task;

import com.stadio.common.define.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by Andy on 02/26/2018.
 */
@Component
public class RabbitProducer {

    @Autowired RabbitTemplate rabbitTemplate;


    public void sendNotification(String message) {
        rabbitTemplate.setExchange(Constant.EXCHANGE_NAME.NOTIFICATION);
        rabbitTemplate.setQueue(Constant.QUEUE_NAME.NOTIFICATION);
        rabbitTemplate.setRoutingKey(Constant.QUEUE_NAME.NOTIFICATION);
        rabbitTemplate.convertAndSend(message);
    }
}
