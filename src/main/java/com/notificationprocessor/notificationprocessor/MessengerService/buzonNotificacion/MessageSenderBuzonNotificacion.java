package com.notificationprocessor.notificationprocessor.MessengerService.buzonNotificacion;


import com.notificationprocessor.notificationprocessor.crossCutting.utils.MessageSender;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.gson.MapperJsonObjeto;
import com.notificationprocessor.notificationprocessor.domain.BuzonNotificacionDomain;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;
@Configuration

public class MessageSenderBuzonNotificacion implements MessageSender<List<BuzonNotificacionDomain>> {


    @Autowired
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    private final MapperJsonObjeto mapperJsonObjeto;

    public MessageSenderBuzonNotificacion(RabbitTemplate rabbitTemplate, MapperJsonObjeto mapperJsonObjeto) {
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
    public void execute(List<BuzonNotificacionDomain> message, String exchange, String routingKey, String idMessage) {
        MessageProperties propiedadesMensaje = generarPropiedadesMensaje(Long.valueOf(idMessage));

        Optional<Message> cuerpoMensaje = obtenerCuerpoMensaje(message, propiedadesMensaje);
        if (!cuerpoMensaje.isPresent()) {
            return;
        }

        rabbitTemplate.convertAndSend(exchange, routingKey, cuerpoMensaje.get());
    }
}
