package com.notificationprocessor.notificationprocessor.MessengerService.notificacion;


import com.notificationprocessor.notificationprocessor.config.NotificacionQueueConfig;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.MessageSender;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.gson.MapperJsonObjeto;
import com.notificationprocessor.notificationprocessor.domain.NotificacionDomain;
import org.aspectj.weaver.ast.Not;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageSenderNotificacion implements MessageSender<NotificacionDomain> {

    @Autowired
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    private final MapperJsonObjeto mapperJsonObjeto;

    @Autowired
    private final NotificacionQueueConfig notificacionQueueConfig;


    public MessageSenderNotificacion(RabbitTemplate rabbitTemplate, MapperJsonObjeto mapperJsonObjeto, NotificacionQueueConfig notificacionQueueConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapperJsonObjeto = mapperJsonObjeto;
        this.notificacionQueueConfig = notificacionQueueConfig;
    }

    private MessageProperties generarPropiedadesMensaje(String idMessageSender ) {
        return MessagePropertiesBuilder.newInstance()
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setHeader("idMensaje", idMessageSender)
                .build();
    }

    private Optional<Message> obtenerCuerpoMensaje(Object mensaje, MessageProperties propiedadesMensaje) {
        Optional<String> textoMensaje = mapperJsonObjeto.ejecutarGson(mensaje);

        return textoMensaje.map(msg -> MessageBuilder
                .withBody(msg.getBytes())
                .andProperties(propiedadesMensaje)
                .build());

    }

    @Override
    public void execute(NotificacionDomain message, String idMessage) {
        MessageProperties propiedadesMensaje = generarPropiedadesMensaje(idMessage);

        Optional<Message> cuerpoMensaje = obtenerCuerpoMensaje(message, propiedadesMensaje);
        if (!cuerpoMensaje.isPresent()) {
            return;
        }

        rabbitTemplate.convertAndSend(notificacionQueueConfig.getExchangeName(), notificacionQueueConfig.getRoutingKeyName(), cuerpoMensaje.get());
    }
}
