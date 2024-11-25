package com.notificationprocessor.notificationprocessor.MessengerService.notificacion;


import com.notificationprocessor.notificationprocessor.crossCutting.Messages.UtilMessagesServices;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.gson.MapperJsonObjetoJackson;
import com.notificationprocessor.notificationprocessor.domain.NotificacionDomain;
import com.notificationprocessor.notificationprocessor.service.NotificacionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ReciverMesssageNotificacion {

    private static final Logger logger = LoggerFactory.getLogger(ReciverMesssageNotificacion.class);

    @Autowired
    private final NotificacionService notificacionService = new NotificacionService();

    private final MapperJsonObjetoJackson mapperJsonObjeto;

    public ReciverMesssageNotificacion(MapperJsonObjetoJackson mapperJsonObjeto) {
        this.mapperJsonObjeto = mapperJsonObjeto;
    }


    @RabbitListener(queues = "cola.notificacion.crear")
    public void receiveMessageCrearNotificacion(String message) {
        obtenerObjetoDeMensaje(message).ifPresentOrElse(
                mensajeRecibido -> {
                    try {
                        notificacionService.saveNotificacion(mensajeRecibido);
                        logger.info(UtilMessagesServices.ReciverMessageNotificacion.NOTIFICACION_GUARDADA);
                    } catch (Exception exception) {
                        logger.error(UtilMessagesServices.ReciverMessageNotificacion.NOTIFICACION_NO_GUARDADA, exception.getMessage(), exception);
                    }
                },
                () -> logger.warn(UtilMessagesServices.ReciverMessageNotificacion.MENSAJE_NO_MAPEADO, message)
        );
    }

    private Optional<NotificacionDomain> obtenerObjetoDeMensaje(String mensaje) {
        return mapperJsonObjeto.ejecutar(mensaje, NotificacionDomain.class);
    }
}
