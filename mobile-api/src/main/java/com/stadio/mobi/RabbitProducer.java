package com.stadio.mobi;

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

    private Logger logger = LogManager.getLogger(RabbitProducer.class);

    private ObjectMapper mapper = new ObjectMapper();

//    public void sendMessage(String routingKey, ChatMessage message) {
//        logger.info("Sending message: " + message);
//        logger.info("Routing Key: " + routingKey);
//        try {
//            rabbitTemplate.convertAndSend(routingKey, mapper.writeValueAsString(message));
//        } catch (IOException e) {
//            logger.error("convertAndSend Exception: ", e);
//        }
//    }

    public void sendNotification(String message) {
        rabbitTemplate.convertAndSend(Constant.QUEUE_NAME.NOTIFICATION, message);
    }
}
