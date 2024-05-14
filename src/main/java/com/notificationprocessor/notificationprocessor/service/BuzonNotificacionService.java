package com.notificationprocessor.notificationprocessor.service;



import com.notificationprocessor.notificationprocessor.MessengerService.buzonNotificacion.MessageSenderBuzonNotificacion;
import com.notificationprocessor.notificationprocessor.config.buzonNotificacionQueueConfig.BuzonNotificacionQueueConfigLista;
import com.notificationprocessor.notificationprocessor.domain.BuzonNotificacionDomain;
import com.notificationprocessor.notificationprocessor.domain.NotificacionDomain;
import com.notificationprocessor.notificationprocessor.domain.PersonaDomain;
import com.notificationprocessor.notificationprocessor.entity.BuzonNotificacionEntity;
import com.notificationprocessor.notificationprocessor.entity.NotificacionEntity;
import com.notificationprocessor.notificationprocessor.entity.PersonaEntity;
import com.notificationprocessor.notificationprocessor.repository.BuzonNotificacionRepository;
import com.notificationprocessor.notificationprocessor.repository.NotificacionRepository;
import com.notificationprocessor.notificationprocessor.repository.PersonaRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Repository
public class BuzonNotificacionService {


    @Autowired
    private BuzonNotificacionRepository buzonNotificacionRepository;

    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private MessageSenderBuzonNotificacion messageSenderBuzonNotificacion;

    @Autowired
    private BuzonNotificacionQueueConfigLista buzonNotificacionQueueConfigLista;
    @Autowired
    private NotificacionRepository notificacionRepository;

    @Transactional
   public void getBuzonNotificacionesPorPropietario(BuzonNotificacionDomain buzonNotificacionDomain) {
        var entities = buzonNotificacionRepository.findAll();
        var notificaciones = entities.stream().filter(notificacion->notificacion.getPropietario().getCorreoElectronico().equals(buzonNotificacionDomain.getPropietario().getCorreoElectronico())).toList();
        List<BuzonNotificacionDomain> buzon = notificaciones.stream().map(new BuzonNotificacionService()::getDomain).toList();
        messageSenderBuzonNotificacion.execute(buzon, buzonNotificacionQueueConfigLista.getExchangeName(), buzonNotificacionQueueConfigLista.getRoutingKeyName(),"32");

    }
    @Transactional
    public void getAll(){
        List<BuzonNotificacionDomain> buzones = buzonNotificacionRepository.findAll().stream().map(new BuzonNotificacionService()::getDomain).toList();
        messageSenderBuzonNotificacion.execute(buzones, buzonNotificacionQueueConfigLista.getExchangeName(), buzonNotificacionQueueConfigLista.getRoutingKeyName(),"32");
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

    public void saveBuzonNotificacion(BuzonNotificacionDomain buzonNotificacion){
        var propietario = new PersonaEntity(buzonNotificacion.getPropietario().getIdentificador(), buzonNotificacion.getPropietario().getPrimerNombre(), buzonNotificacion.getPropietario().getSegundoNombre(), buzonNotificacion.getPropietario().getPrimerApellido(), buzonNotificacion.getPropietario().getSegundoApellido(), buzonNotificacion.getPropietario().getCorreoElectronico());
        personaRepository.save(propietario);
        var entity = new BuzonNotificacionEntity(buzonNotificacion.getIdentificador(),personaRepository.findBycorreoElectronico(propietario.getCorreoElectronico()),setNombreBuzon(propietario.getPrimerNombre()), getNotificacionesEntity(buzonNotificacion.getNotificaciones()));
         buzonNotificacionRepository.save(entity);
    }

    public void eliminarBuzonNotificacion(BuzonNotificacionDomain buzonNotificacion){
        PersonaDomain propietario = personaToDomain(personaRepository.findBycorreoElectronico(buzonNotificacion.getPropietario().getCorreoElectronico()));
        buzonNotificacionRepository.deleteBypersonaIdentificador(propietario.getIdentificador());
        personaRepository.deleteById(propietario.getIdentificador());

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

    private String setNombreBuzon(String nombre){
        return "Buzon de "+nombre;
    }

    @Transactional
    public void enviarNotificacion(UUID id){
        NotificacionEntity entity = notificacionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notificación no encontrada"));
        Hibernate.initialize(entity.getDestinatario());
        enviarNotificacionABuzones(entity);
    }

    private void enviarNotificacionABuzones(NotificacionEntity entity) {
        for (PersonaEntity personaEntity : entity.getDestinatario()){
            var buzon = buzonNotificacionRepository.findByPropietario(personaEntity.getIdentificador());
            buzonNotificacionRepository.saveBuzonNotificacion(buzon.getIdentificador(),entity.getIdentificador());
        }
    }
}
