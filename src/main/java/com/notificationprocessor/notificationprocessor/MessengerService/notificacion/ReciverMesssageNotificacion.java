package com.notificationprocessor.notificationprocessor.MessengerService.notificacion;



import com.notificationprocessor.notificationprocessor.crossCutting.utils.gson.MapperJsonObjeto;
import com.notificationprocessor.notificationprocessor.domain.NotificacionDomain;
import com.notificationprocessor.notificationprocessor.service.NotificacionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReciverMesssageNotificacion {

    private final NotificacionService notificacionService = new NotificacionService();

    @Autowired
    private final MapperJsonObjeto mapperJsonObjeto;

    public ReciverMesssageNotificacion(MapperJsonObjeto mapperJsonObjeto) {
        this.mapperJsonObjeto = mapperJsonObjeto;
    }


    @RabbitListener(queues = "${notificacion.recibir.queue.name}")
    public void receiveMessageCrearNotificacion(String message) {
        try {
            System.out.println(obtenerObjetoDeMensaje(message).get());
            notificacionService.saveNotificacion(obtenerObjetoDeMensaje(message).get());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
   // @RabbitListener(queues = "notifiacion.to.processor.consultar")
    public void receiveMessageConsultarNotificacion(String message) {
       var mensajeRecibido = obtenerObjetoDeMensaje(message).get();
        try {
            if(mensajeRecibido.equals(null)){
                notificacionService.findAll();
            }
            notificacionService.getNotificacionesPorDestinatario(String.valueOf(mensajeRecibido));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private Optional<NotificacionDomain> obtenerObjetoDeMensaje(String mensaje) {
        return mapperJsonObjeto.ejecutar(mensaje, NotificacionDomain.class);
    }
}
