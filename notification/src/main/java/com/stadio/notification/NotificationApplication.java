package com.stadio.notification;

import com.stadio.common.define.Constant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//Config to use com.stadio.moel.dbconfig, uncomment this will switch to default mongo spring config
@ComponentScan(basePackages = {"com.stadio"})
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,
        MongoRepositoriesAutoConfiguration.class, EmbeddedMongoAutoConfiguration.class
})
public class NotificationApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(NotificationApplication.class);
    }

    @Bean
    Queue queue()
    {
        return new Queue(Constant.QUEUE_NAME.NOTIFICATION, true);
    }

    @Bean
    TopicExchange exchange()
    {
        return new TopicExchange(Constant.EXCHANGE_NAME.NOTIFICATION);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constant.QUEUE_NAME.NOTIFICATION);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(Constant.QUEUE_NAME.NOTIFICATION);
        container.setMessageListener(listenerAdapter);
        container.setReceiveTimeout(1000);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(ReliableReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }


}
