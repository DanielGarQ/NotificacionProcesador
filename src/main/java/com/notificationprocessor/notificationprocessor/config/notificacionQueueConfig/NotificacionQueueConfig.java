package com.notificationprocessor.notificationprocessor.config.notificacionQueueConfig;


import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public abstract class NotificacionQueueConfig {
    public String exchangeName;
    public String routingKeyName;
    public String queueName;



}
