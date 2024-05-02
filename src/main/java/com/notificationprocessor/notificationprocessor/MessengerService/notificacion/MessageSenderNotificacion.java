package com.notificationprocessor.notificationprocessor.MessengerService.notificacion;


import com.notificationprocessor.notificationprocessor.crossCutting.utils.MessageSender;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.gson.MapperJsonObjeto;
import com.notificationprocessor.notificationprocessor.domain.NotificacionDomain;
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

    public MessageSenderNotificacion(RabbitTemplate rabbitTemplate, MapperJsonObjeto mapperJsonObjeto) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapperJsonObjeto = mapperJsonObjeto;
    }


    private MessageProperties generarPropiedadesMensaje(Long idMensajeEmisor ) {
        return MessagePropertiesBuilder.newInstance()
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setHeader("idMensaje", String.valueOf(idMensajeEmisor))
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
    public void execute(NotificacionDomain message, String exchange, String routingKey, String idMessage) {
        MessageProperties propiedadesMensaje = generarPropiedadesMensaje(Long.valueOf(idMessage));

        Optional<Message> cuerpoMensaje = obtenerCuerpoMensaje(message, propiedadesMensaje);
        if (!cuerpoMensaje.isPresent()) {
            return;
        }

        rabbitTemplate.convertAndSend(exchange, routingKey, cuerpoMensaje.get());
    }
}
