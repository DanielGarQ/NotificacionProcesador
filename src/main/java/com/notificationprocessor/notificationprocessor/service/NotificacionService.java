package com.notificationprocessor.notificationprocessor.service;


import com.notificationprocessor.notificationprocessor.MessengerService.notificacion.MessageSenderNotificacion;
import com.notificationprocessor.notificationprocessor.MessengerService.respuesta.MessageRespuestaSenderBroker;
import com.notificationprocessor.notificationprocessor.config.notificacionQueueConfig.NotificacionQueueConfigRespuesta;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilDate;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilUUID;
import com.notificationprocessor.notificationprocessor.domain.BuzonNotificacionDomain;
import com.notificationprocessor.notificationprocessor.domain.NotificacionDomain;
import com.notificationprocessor.notificationprocessor.domain.PersonaDomain;
import com.notificationprocessor.notificationprocessor.entity.NotificacionEntity;
import com.notificationprocessor.notificationprocessor.entity.PersonaEntity;
import com.notificationprocessor.notificationprocessor.repository.NotificacionRepository;
import com.notificationprocessor.notificationprocessor.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Repository
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;


    @Autowired
    private NotificacionQueueConfigRespuesta notificacionQueueConfigRespuesta;

    private MessageSenderNotificacion messageSenderNotificacion;
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

    private PersonaEntity personaToEntity(PersonaDomain domain){
        return new PersonaEntity(domain.getIdentificador(), domain.getPrimerNombre(), domain.getSegundoNombre(), domain.getPrimerApellido(), domain.getSegundoApellido(), domain.getCorreoElectronico());
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
        }catch (JpaSystemException e){
            System.out.println(e);
        }
        try {
            buzonNotificacionService.enviarNotificacion(entity.getIdentificador());
        }catch (NoSuchElementException e){
            System.out.println(e);
        }
        messageRespuestaSenderBroker.execute("Notificacion Enviada con Exito!!!",notificacionQueueConfigRespuesta.getExchangeName(), notificacionQueueConfigRespuesta.getRoutingKeyName(), "330");

    }

    public void deleteNotificacion(UUID identificador){
        notificacionRepository.deleteById(identificador);
    }

    private PersonaEntity registroAutor(PersonaDomain autor){
        var autorEntity = personaRepository.findBycorreoElectronico(autor.getCorreoElectronico());
        return autorEntity;
    }

    private List<PersonaEntity> registroDestinatario(List<PersonaDomain> destinatarios){
        List<PersonaEntity> nuevaLista = new ArrayList<>();
        for(PersonaDomain destinatario: destinatarios){
            nuevaLista.add(personaRepository.findBycorreoElectronico(destinatario.getCorreoElectronico()));
        }
        return nuevaLista;
    }

}
