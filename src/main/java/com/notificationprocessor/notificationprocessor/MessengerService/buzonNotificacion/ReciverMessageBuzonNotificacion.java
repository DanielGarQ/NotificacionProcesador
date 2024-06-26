package com.notificationprocessor.notificationprocessor.MessengerService.buzonNotificacion;


import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilEmail;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilUUID;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.gson.MapperJsonObjeto;
import com.notificationprocessor.notificationprocessor.domain.BuzonNotificacionDomain;
import com.notificationprocessor.notificationprocessor.service.BuzonNotificacionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReciverMessageBuzonNotificacion {

    @Autowired
    private final BuzonNotificacionService buzonNotificacionService = new BuzonNotificacionService();

    @Autowired
    private final MapperJsonObjeto mapperJsonObjeto;

    public ReciverMessageBuzonNotificacion(MapperJsonObjeto mapperJsonObjeto) {
        this.mapperJsonObjeto = mapperJsonObjeto;
    }

   @RabbitListener(queues = "cola.buzon.crear")
    public void receiveMessageCrear(String message) {
        var mensajeRecibido = obtenerObjetoDeMensaje(message).get();
        try {
            buzonNotificacionService.saveBuzonNotificacion(mensajeRecibido);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @RabbitListener(queues = "cola.buzon.eliminar")
    public void receiveMessageEliminar(String message) {
        var mensajeRecibido = obtenerObjetoDeMensaje(message).get();
        try {
            if(!mensajeRecibido.getIdentificador().equals(UtilUUID.getUuidDefaultValue())){
                buzonNotificacionService.eliminarNotificacionDeBuzon(mensajeRecibido);
            }else {
                buzonNotificacionService.eliminarBuzonNotificacion(mensajeRecibido);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @RabbitListener(queues = "cola.buzon.consultar")
    public void receiveMessageConsultar(String message) {
        var mensajeRecibido = obtenerObjetoDeMensaje(message).get();
        try {
            if(mensajeRecibido.getPropietario().getCorreoElectronico().equals(UtilEmail.getDefaultValueMail())){
                buzonNotificacionService.getAll();
            }
            buzonNotificacionService.getBuzonNotificacionesPorPropietario(mensajeRecibido);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private Optional<BuzonNotificacionDomain> obtenerObjetoDeMensaje(String mensaje) {
        return mapperJsonObjeto.ejecutar(mensaje, BuzonNotificacionDomain.class);
    }
}
