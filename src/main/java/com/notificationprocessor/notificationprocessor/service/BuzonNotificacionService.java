package com.notificationprocessor.notificationprocessor.service;


import com.notificationprocessor.notificationprocessor.domain.BuzonNotificacionDomain;
import com.notificationprocessor.notificationprocessor.domain.NotificacionDomain;
import com.notificationprocessor.notificationprocessor.domain.PersonaDomain;
import com.notificationprocessor.notificationprocessor.entity.BuzonNotificacionEntity;
import com.notificationprocessor.notificationprocessor.entity.NotificacionEntity;
import com.notificationprocessor.notificationprocessor.entity.PersonaEntity;
import com.notificationprocessor.notificationprocessor.repository.BuzonNotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class BuzonNotificacionService {

    @Autowired
    private BuzonNotificacionRepository buzonNotificacionRepository;

    public List<BuzonNotificacionDomain> getBuzonNotificacionesPorPropietario(String correo){
        var entities = buzonNotificacionRepository.findAll();
        var notificaciones = entities.stream().filter(notificacion->notificacion.getPropietario().getCorreoElectronico().equals(correo)).toList();
        return notificaciones.stream().map(new BuzonNotificacionService()::getDomain).toList();
    }

    public BuzonNotificacionDomain getDomain(BuzonNotificacionEntity entity){
        var propietario = new PersonaDomain(entity.getPropietario().getIdentificador(),entity.getPropietario().getPrimerNombre(), entity.getPropietario().getSegundoNombre(), entity.getPropietario().getPrimerApellido(), entity.getPropietario().getSegundoApellido(), entity.getPropietario().getCorreoElectronico());
        return new BuzonNotificacionDomain(entity.getIdentificador(), propietario, entity.getNombre(), getNotificaciones(entity.getNotificaciones()));
    }

    private List<NotificacionDomain> getNotificaciones(List<NotificacionEntity> notificaciones){
        return notificaciones.stream().map(new BuzonNotificacionService()::getNotificacion).toList();
    }

    private NotificacionDomain getNotificacion(NotificacionEntity notificacion){
        var usuario = new PersonaDomain(notificacion.getAutor().getIdentificador(), notificacion.getAutor().getPrimerNombre(), notificacion.getAutor().getSegundoNombre(), notificacion.getAutor().getPrimerApellido(), notificacion.getAutor().getSegundoApellido(), notificacion.getAutor().getCorreoElectronico());
        return new NotificacionDomain(notificacion.getIdentificador(), usuario, notificacion.getTitulo(), notificacion.getContenido(), notificacion.getFechaCreacion(), notificacion.getEstado(), notificacion.getFechaProgramada(), notificacion.getTipoEntrega(), notificacion.getDestinatario().stream().map(new BuzonNotificacionService()::personaToDomain).toList());
    }

    public List<BuzonNotificacionDomain> findAll(){

        return buzonNotificacionRepository.findAll().stream().map(new BuzonNotificacionService()::getDomain).toList();
    }

    public BuzonNotificacionDomain findById(UUID identificador){
        var entity = buzonNotificacionRepository.findById(identificador).orElse(null);
        var propietario = new PersonaDomain(entity.getPropietario().getIdentificador(), entity.getPropietario().getPrimerNombre(), entity.getPropietario().getSegundoNombre(), entity.getPropietario().getPrimerApellido(), entity.getPropietario().getSegundoApellido(), entity.getPropietario().getCorreoElectronico());
        assert entity != null;
        return new BuzonNotificacionDomain(entity.getIdentificador(), propietario, entity.getNombre(), getNotificaciones(entity.getNotificaciones()));
    }

    public void saveBuzonNotificacion(BuzonNotificacionDomain buzonNotificacion){
        var propietario = new PersonaEntity(buzonNotificacion.getPropietario().getIdentificador(), buzonNotificacion.getPropietario().getPrimerNombre(), buzonNotificacion.getPropietario().getSegundoNombre(), buzonNotificacion.getPropietario().getPrimerApellido(), buzonNotificacion.getPropietario().getSegundoApellido(), buzonNotificacion.getPropietario().getCorreoElectronico());
        var entity = new BuzonNotificacionEntity(buzonNotificacion.getIdentificador(), propietario, buzonNotificacion.getNombre(), getNotificacionesEntity(buzonNotificacion.getNotificaciones()));
         buzonNotificacionRepository.save(entity);
    }

    private List<NotificacionEntity> getNotificacionesEntity(List<NotificacionDomain> notificaciones){
        return notificaciones.stream().map(new BuzonNotificacionService()::getNotificacionEntity).toList();
    }

    private NotificacionEntity getNotificacionEntity(NotificacionDomain notificacion){
        var usuario = new PersonaEntity(notificacion.getAutor().getIdentificador(), notificacion.getAutor().getPrimerNombre(), notificacion.getAutor().getSegundoNombre(), notificacion.getAutor().getPrimerApellido(), notificacion.getAutor().getSegundoApellido(), notificacion.getAutor().getCorreoElectronico());
        return new NotificacionEntity(notificacion.getIdentificador(), usuario, notificacion.getTitulo(), notificacion.getContenido(), notificacion.getFechaCreacion(), notificacion.getEstado(), notificacion.getFechaProgramada(), notificacion.getTipoEntrega(), notificacion.getDestinatario().stream().map(new BuzonNotificacionService()::personaToEntity).toList());
    }

    private PersonaDomain personaToDomain(PersonaEntity entity){
        return new PersonaDomain(entity.getIdentificador(), entity.getPrimerNombre(), entity.getSegundoNombre(), entity.getPrimerApellido(), entity.getSegundoApellido(), entity.getCorreoElectronico());
    }

    private PersonaEntity personaToEntity(PersonaDomain domain){
        return new PersonaEntity(domain.getIdentificador(), domain.getPrimerNombre(), domain.getSegundoNombre(), domain.getPrimerApellido(), domain.getSegundoApellido(), domain.getCorreoElectronico());
    }


}
