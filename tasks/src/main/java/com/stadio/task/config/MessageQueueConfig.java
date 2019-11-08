package com.stadio.task.config;


import com.stadio.common.define.Constant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageQueueConfig {

    /**
     *  TOPIC EXCHANGE TRACKING EVENT
     */
    @Bean
    public TopicExchange exchangeTrackingEvent() {
        return new TopicExchange(Constant.EXCHANGE_NAME.TRACKING_EVENT);
    }

    @Bean
    public Queue queueTrackingExamOnline() {
        return new Queue(Constant.QUEUE_NAME.EXAM_ONLINE);
    }

    @Bean
    public Binding bindingTracking(Queue queueTrackingExamOnline, TopicExchange exchangeTrackingEvent) {
        return BindingBuilder.bind(queueTrackingExamOnline).to(exchangeTrackingEvent).with(".chemistry.trackingEvent.examOnline");
    }

    /**
     * TOPIC EXCHANGE LISTENER COMMUNICATION FROM DEVICES ON MOBILE USING MQTT.
     */

    @Bean
    public TopicExchange exchangeAMQTopic() {
        return new TopicExchange("amq.topic");
    }

    /**
     * COMMENT FEATURE
     */

    @Bean
    public Queue queueExamOnlineComment() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding bindingChemistryExamOnlineComment(Queue queueExamOnlineComment, TopicExchange exchangeAMQTopic) {
        return BindingBuilder.bind(queueExamOnlineComment).to(exchangeAMQTopic).with(".chemistry.examOnline.*.comment");
    }

    /**
     * SUBMIT FEATURE
     */
    @Bean
    public Queue queueExamOnlineSubmit() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding bindingChemistryExamOnlineSubmit(Queue queueExamOnlineSubmit, TopicExchange exchangeAMQTopic) {
        return BindingBuilder.bind(queueExamOnlineSubmit).to(exchangeAMQTopic).with(".chemistry.examOnline.*.submit");
    }

//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(rabbitTemplate.getConnectionFactory());
//        factory.setConcurrentConsumers(3);
//        factory.setMaxConcurrentConsumers(10);
//
//        return factory;
//    }

    /**
     * TOPIC EXCHANGE COMMUNICATION WITH NOTIFICATION SERVICE.
     */
    @Bean
    public TopicExchange exchangeNotification() {
        return new TopicExchange(Constant.EXCHANGE_NAME.NOTIFICATION);
    }


    /**
     *  Manager Exam sync data mongodb, elastic, redis
     */

    @Bean
    public Queue queueExamManager() {
        return new Queue(Constant.QUEUE_NAME.EXAM_MANAGER);
    }

}
