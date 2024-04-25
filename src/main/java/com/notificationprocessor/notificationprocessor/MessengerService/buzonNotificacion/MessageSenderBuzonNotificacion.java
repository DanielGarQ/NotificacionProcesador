package com.notificationprocessor.notificationprocessor.MessengerService.buzonNotificacion;


import com.notificationprocessor.notificationprocessor.config.BuzonNotificacionQueueConfig;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.MessageSender;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.gson.MapperJsonObjeto;
import com.notificationprocessor.notificationprocessor.domain.BuzonNotificacionDomain;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class MessageSenderBuzonNotificacion implements MessageSender<BuzonNotificacionDomain> {


    @Autowired
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    private final MapperJsonObjeto mapperJsonObjeto;

    @Autowired
    private final BuzonNotificacionQueueConfig buzonNotificacionQueueConfig;


    public MessageSenderBuzonNotificacion(RabbitTemplate rabbitTemplate, MapperJsonObjeto mapperJsonObjeto, BuzonNotificacionQueueConfig buzonNotificacionQueueConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapperJsonObjeto = mapperJsonObjeto;
        this.buzonNotificacionQueueConfig = buzonNotificacionQueueConfig;
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
    public void execute(BuzonNotificacionDomain message, String idMessage) {
        MessageProperties propiedadesMensaje = generarPropiedadesMensaje(idMessage);

        Optional<Message> cuerpoMensaje = obtenerCuerpoMensaje(message, propiedadesMensaje);
        if (!cuerpoMensaje.isPresent()) {
            return;
        }
        rabbitTemplate.convertAndSend(buzonNotificacionQueueConfig.getExchangeName(), buzonNotificacionQueueConfig.getRoutingKeyName(), cuerpoMensaje.get());
    }

}
