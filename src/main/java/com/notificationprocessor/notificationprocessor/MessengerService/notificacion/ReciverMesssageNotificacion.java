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

    @Autowired
    private final NotificacionService notificacionService = new NotificacionService();

    private final MapperJsonObjetoJackson mapperJsonObjeto;

    public ReciverMesssageNotificacion(MapperJsonObjetoJackson mapperJsonObjeto) {
        this.mapperJsonObjeto = mapperJsonObjeto;
    }


    @RabbitListener(queues = "cola.notificacion.crear")
    public void receiveMessageCrearNotificacion(String message) throws JsonProcessingException {
        System.out.println(message);

        var mensajeRecibido = obtenerObjetoDeMensaje(message).get();
        try {
            System.out.println(mensajeRecibido);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
   @RabbitListener(queues = "cola.notificacion.consultar")
    public void receiveMessageConsultarNotificacion(String message) {
       System.out.println(message);

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
