package com.notificationprocessor.notificationprocessor.MessengerService.notificacion;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.gson.MapperJsonObjeto;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.gson.MapperJsonObjetoJackson;
import com.notificationprocessor.notificationprocessor.domain.NotificacionDomain;
import com.notificationprocessor.notificationprocessor.entity.NotificacionEntity;
import com.notificationprocessor.notificationprocessor.service.NotificacionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReciverMesssageNotificacion {

    private final NotificacionService notificacionService = new NotificacionService();

    @Autowired
    private final MapperJsonObjetoJackson mapperJsonObjeto;

    @Autowired private ObjectMapper objectMapper;

    public ReciverMesssageNotificacion(MapperJsonObjetoJackson mapperJsonObjeto) {
        this.mapperJsonObjeto = mapperJsonObjeto;
    }


    @RabbitListener(queues = "cola.notificacion.crear")
    public void receiveMessageCrearNotificacion(String message) throws JsonProcessingException {
       var mensajeRecibido = obtenerObjetoDeMensaje(message).get();
        try {
            System.out.println(mensajeRecibido);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
   //@RabbitListener(queues = "colaNotificacionConsultar")
    public void receiveMessageConsultarNotificacion(String message) {
       var mensajeRecibido = obtenerObjetoDeMensaje(message).get();
        try {
            System.out.println(mensajeRecibido);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

   // @RabbitListener(queues = "colaNotificacionEliminar")
    public void receiveMessageEliminarNotificacion(String message) {
        var mensajeRecibido = obtenerObjetoDeMensaje(message).get();
        try {
           System.out.println(mensajeRecibido);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private Optional<NotificacionDomain> obtenerObjetoDeMensaje(String mensaje) {
        return mapperJsonObjeto.ejecutar(mensaje, NotificacionDomain.class);
    }
}
