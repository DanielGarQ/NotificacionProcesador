package com.notificationprocessor.notificationprocessor.service;


import com.notificationprocessor.notificationprocessor.MessengerService.notificacion.MessageSenderNotificacion;
import com.notificationprocessor.notificationprocessor.MessengerService.respuesta.MessageRespuestaSenderBroker;
import com.notificationprocessor.notificationprocessor.config.notificacionQueueConfig.NotificacionQueueConfigRespuesta;
import com.notificationprocessor.notificationprocessor.crossCutting.Messages.UtilMessagesServices;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilDate;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilUUID;
import com.notificationprocessor.notificationprocessor.domain.BuzonNotificacionDomain;
import com.notificationprocessor.notificationprocessor.domain.NotificacionDomain;
import com.notificationprocessor.notificationprocessor.domain.PersonaDomain;
import com.notificationprocessor.notificationprocessor.entity.NotificacionEntity;
import com.notificationprocessor.notificationprocessor.entity.PersonaEntity;
import com.notificationprocessor.notificationprocessor.repository.NotificacionRepository;
import com.notificationprocessor.notificationprocessor.repository.PersonaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Repository
public class NotificacionService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);

    @Autowired
    private NotificacionRepository notificacionRepository;


    @Autowired
    private NotificacionQueueConfigRespuesta notificacionQueueConfigRespuesta;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private BuzonNotificacionService buzonNotificacionService;

    @Autowired
    private MessageRespuestaSenderBroker messageRespuestaSenderBroker;


    public List<NotificacionDomain> findAll(){
        return notificacionRepository.findAll().stream().map(new NotificacionService()::toDomain).toList();
    }

    public List<NotificacionDomain> getNotificacionesPorDestinatario(String correo){
        var entities = notificacionRepository.findAll();
        var notificaciones = entities.stream().filter(notificacion->notificacion.getDestinatario().stream().anyMatch(destinatario->destinatario.getCorreoElectronico().equals(correo))).toList();
        return notificaciones.stream().map(new NotificacionService()::toDomain).toList();
    }

    private NotificacionDomain toDomain(NotificacionEntity entity){
        var autor = new PersonaDomain(entity.getAutor().getIdentificador(), entity.getAutor().getPrimerNombre(), entity.getAutor().getSegundoNombre(), entity.getAutor().getPrimerApellido(), entity.getAutor().getSegundoApellido(), entity.getAutor().getCorreoElectronico());
        return new NotificacionDomain(entity.getIdentificador(), autor, entity.getTitulo(), entity.getContenido(), entity.getFechaCreacion(), entity.getEstado(), entity.getFechaProgramada(), entity.getTipoEntrega(), entity.getDestinatario().stream().map(new NotificacionService()::personaToDomain).toList());
    }

    private PersonaDomain personaToDomain(PersonaEntity entity){
        return new PersonaDomain(entity.getIdentificador(), entity.getPrimerNombre(), entity.getSegundoNombre(), entity.getPrimerApellido(), entity.getSegundoApellido(), entity.getCorreoElectronico());
    }

    public NotificacionDomain findById(UUID identificador){
        var entity = notificacionRepository.findById(identificador).orElse(null);
        assert entity != null;
        return toDomain(entity);
    }

    public void saveNotificacion(NotificacionDomain notificacion){
        var entity = new NotificacionEntity(UtilUUID.newUuid(notificacionRepository), registroAutor(notificacion.getAutor()), notificacion.getTitulo(), notificacion.getContenido(), notificacion.getFechaCreacion(),EstadoNotificacion.Entregado.toString(), notificacion.getFechaProgramada(),TipoEntrega.Inmediata.toString(),registroDestinatario(notificacion.getDestinatario()));
        try{
            notificacionRepository.save(entity);
        }catch (JpaSystemException exception){
            logger.error(UtilMessagesServices.NotificacionService.NOTIFICACION_NO_GUARDADA, exception.getMessage(), exception);
        }
        try {
            buzonNotificacionService.enviarNotificacion(entity.getIdentificador());
        }catch (NoSuchElementException exception){
            logger.error(UtilMessagesServices.NotificacionService.NOTIFICACION_NO_GUARDADA, exception.getMessage(), exception);
        }
        messageRespuestaSenderBroker.execute("Notificacion Enviada con Exito!!!",notificacionQueueConfigRespuesta.getExchangeName(), notificacionQueueConfigRespuesta.getRoutingKeyName(), "330");

    }

    private PersonaEntity registroAutor(PersonaDomain autor){
        return personaRepository.findBycorreoElectronico(autor.getCorreoElectronico());
    }

    private List<PersonaEntity> registroDestinatario(List<PersonaDomain> destinatarios){
        List<PersonaEntity> nuevaLista = new ArrayList<>();
        for(PersonaDomain destinatario: destinatarios){
            nuevaLista.add(personaRepository.findBycorreoElectronico(destinatario.getCorreoElectronico()));
        }
        return nuevaLista;
    }

}
