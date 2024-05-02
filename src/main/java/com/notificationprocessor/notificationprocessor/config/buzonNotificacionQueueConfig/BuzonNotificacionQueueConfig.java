package com.notificationprocessor.notificationprocessor.config.buzonNotificacionQueueConfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public abstract class BuzonNotificacionQueueConfig {
    public String exchangeName;
    public String routingKeyName;
    public String queueName;
}
