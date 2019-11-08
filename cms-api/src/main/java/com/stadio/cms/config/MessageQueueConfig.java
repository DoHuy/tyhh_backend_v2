package com.stadio.cms.config;

import com.stadio.common.define.Constant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageQueueConfig {

    //=================================================================================//
    //
    //
    //
    //
    //
    //
    //=================================================================================//
    @Bean
    public TopicExchange exchangeNotification() {
        return new TopicExchange(Constant.EXCHANGE_NAME.NOTIFICATION);
    }

    @Bean
    public Queue queueNotification() {
        return new Queue(Constant.QUEUE_NAME.NOTIFICATION);
    }

    @Bean
    public Binding bindingNotification(Queue queueNotification, TopicExchange exchangeNotification) {
        return BindingBuilder.bind(queueNotification).to(exchangeNotification).with(Constant.QUEUE_NAME.NOTIFICATION);
    }

    //=================================================================================//
    //
    //
    //
    //
    //
    //
    //=================================================================================//
    @Bean
    public TopicExchange exchangeTrackingEvent() {
        return new TopicExchange(Constant.EXCHANGE_NAME.TRACKING_EVENT);
    }

    @Bean
    public Queue queueExamOnline() {
        return new Queue(Constant.QUEUE_NAME.EXAM_ONLINE);
    }

    @Bean
    public Binding bindingTrackingExamOnline(Queue queueExamOnline, TopicExchange exchangeTrackingEvent) {
        return BindingBuilder.bind(queueExamOnline).to(exchangeTrackingEvent).with(".chemistry.trackingEvent.examOnline");
    }

}
