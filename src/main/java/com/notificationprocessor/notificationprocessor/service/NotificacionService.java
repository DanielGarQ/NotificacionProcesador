package com.notificationprocessor.notificationprocessor.service;


import com.notificationprocessor.notificationprocessor.domain.NotificacionDomain;
import com.notificationprocessor.notificationprocessor.domain.PersonaDomain;
import com.notificationprocessor.notificationprocessor.entity.NotificacionEntity;
import com.notificationprocessor.notificationprocessor.entity.PersonaEntity;
import com.notificationprocessor.notificationprocessor.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

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

    public UUID saveNotificacion(NotificacionDomain notificacion){
        var autor = new PersonaEntity(notificacion.getAutor().getIdentificador(), notificacion.getAutor().getPrimerNombre(), notificacion.getAutor().getSegundoNombre(), notificacion.getAutor().getPrimerApellido(), notificacion.getAutor().getSegundoApellido(), notificacion.getAutor().getCorreoElectronico());
        var entity = new NotificacionEntity(notificacion.getIdentificador(), autor, notificacion.getTitulo(), notificacion.getContenido(), notificacion.getFechaCreacion(), notificacion.getEstado(), notificacion.getFechaProgramada(), notificacion.getTipoEntrega(), notificacion.getDestinatario().stream().map(new NotificacionService()::personaToEntity).toList());
        return notificacionRepository.save(entity).getIdentificador();
    }

    public void deleteNotificacion(UUID identificador){
        notificacionRepository.deleteById(identificador);
    }

}
