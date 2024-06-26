package com.notificationprocessor.notificationprocessor.config.notificacionQueueConfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "notificacion.respuesta")
public class NotificacionQueueConfigRespuesta extends NotificacionQueueConfig{
}
