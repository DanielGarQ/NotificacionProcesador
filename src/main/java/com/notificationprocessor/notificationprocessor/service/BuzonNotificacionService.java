package com.notificationprocessor.notificationprocessor.service;


import com.notificationprocessor.notificationprocessor.MessengerService.buzonNotificacion.MessageSenderBuzonNotificacion;
import com.notificationprocessor.notificationprocessor.MessengerService.respuesta.MessageRespuestaSenderBroker;
import com.notificationprocessor.notificationprocessor.config.buzonNotificacionQueueConfig.BuzonNotificacionQueueConfigLista;
import com.notificationprocessor.notificationprocessor.config.buzonNotificacionQueueConfig.BuzonNotificacionQueueConfigRespuesta;
import com.notificationprocessor.notificationprocessor.crossCutting.Messages.UtilMessagesServices;
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
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class BuzonNotificacionService {

    private static final Logger logger = LoggerFactory.getLogger(BuzonNotificacionService.class);

    @Autowired
    private BuzonNotificacionRepository buzonNotificacionRepository;

    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private MessageSenderBuzonNotificacion messageSenderBuzonNotificacion;

    @Autowired
    private BuzonNotificacionQueueConfigLista buzonNotificacionQueueConfigLista;

    @Autowired
    private BuzonNotificacionQueueConfigRespuesta buzonNotificacionQueueConfigRespuesta;

    @Autowired
    private MessageRespuestaSenderBroker messageRespuestaSenderBroker;

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
         messageRespuestaSenderBroker.execute(UtilMessagesServices.BuzonNotificacionService.BUZON_GUARDADO,buzonNotificacionQueueConfigRespuesta.getExchangeName(),buzonNotificacionQueueConfigRespuesta.getRoutingKeyName(),"2323");
    }

    public void eliminarBuzonNotificacion(BuzonNotificacionDomain buzonNotificacion){
        PersonaDomain propietario = personaToDomain(personaRepository.findBycorreoElectronico(buzonNotificacion.getPropietario().getCorreoElectronico()));
        eliminarNotificacionUsuarioEliminado(propietario);//Primero se encarga de eliminar los registros de destinatario,de notificaciones enviadas y por ultimo de las notificaciones del usuario a eliminar
        eliminarNotificacionesDeBuzon(propietario);//del buzon del usuario a eliminar borra todas las notificaciones que tenga ahi
        buzonNotificacionRepository.deleteByPersonaIdentificador(propietario.getIdentificador());//borra el buzon del usuario a eliminar
        personaRepository.deleteById(propietario.getIdentificador());//borra el usuario
        messageRespuestaSenderBroker.execute(UtilMessagesServices.BuzonNotificacionService.BUZON_NOTIFICACION_ELIMINADO,buzonNotificacionQueueConfigRespuesta.getExchangeName(),buzonNotificacionQueueConfigRespuesta.getRoutingKeyName(),"2323");
    }
    public void eliminarNotificacionDeBuzon(BuzonNotificacionDomain buzonNotificacion){
        try {
            eliminarNotificacion(buzonNotificacion.getNotificaciones());
        }catch (JpaSystemException E){
            messageRespuestaSenderBroker.execute(UtilMessagesServices.BuzonNotificacionService.NOTIFICACION_ELIMINADA,buzonNotificacionQueueConfigRespuesta.getExchangeName(),buzonNotificacionQueueConfigRespuesta.getRoutingKeyName(),"2323");
        }
    }
    private void eliminarNotificacionesDeBuzon(PersonaDomain domain) {
        try{
          var buzon =   buzonNotificacionRepository.findByPersona(domain.getIdentificador());
          buzonNotificacionRepository.deleteIntermediaByBuzonIdentificador(buzon.getIdentificador());
        } catch (JpaSystemException exception){
            logger.error(UtilMessagesServices.BuzonNotificacionService.NOTIFICACION_NO_ELIMINADA, exception.getMessage(), exception);
        }
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
            var buzon = buzonNotificacionRepository.findByPersona(personaEntity.getIdentificador());
            buzonNotificacionRepository.saveBuzonNotificacion(buzon.getIdentificador(),entity.getIdentificador());
        }
    }
    private void eliminarNotificacion(List<NotificacionDomain> notificaciones){
        try {
            for (NotificacionDomain notificacion : notificaciones) {
                buzonNotificacionRepository.deleteNotificacionDeBuzon(notificacion.getIdentificador());
            }
        }catch (JpaSystemException exception){
            logger.error(UtilMessagesServices.BuzonNotificacionService.NOTIFICACION_NO_ELIMINADA, exception.getMessage(), exception);
        }
    }
//Metodos originales de notificacionService, se traen aca por error de circularidad de inyeccion de independencias
    private void eliminarNotificacionUsuarioEliminado(PersonaDomain domain){
        try{
            eliminarDestinatario(domain);
            eliminarNotificacionesEnBuzones(domain);
            eliminarDestinatariosDeNotificaciones(domain);
            notificacionRepository.eliminarNotificacionByAutor(domain.getIdentificador());
        } catch (JpaSystemException exception){
            logger.error(UtilMessagesServices.BuzonNotificacionService.NOTIFICACION_NO_ELIMINADA, exception.getMessage(), exception);
        }
    }

    private void eliminarDestinatario(PersonaDomain domain){
        try {
            notificacionRepository.eliminarDestinatarioById(domain.getIdentificador());
        } catch (JpaSystemException exception){
            logger.error(UtilMessagesServices.BuzonNotificacionService.DESTINATARIO_NO_ELIMINADO, exception.getMessage(), exception);
        }
    }

    private void eliminarNotificacionesEnBuzones(PersonaDomain domain){
        List<NotificacionEntity> notificacionesDeUsuarioAEliminar = notificacionRepository.findByAutor(domain.getIdentificador());
        for(NotificacionEntity notificacion: notificacionesDeUsuarioAEliminar){
            notificacionRepository.eliminarNotificacionDeBuzon(notificacion.getIdentificador());
        }
    }
    private void eliminarDestinatariosDeNotificaciones(PersonaDomain domain){
        List<NotificacionEntity> notificacionesDeUsuarioAEliminar = notificacionRepository.findByAutor(domain.getIdentificador());
        for(NotificacionEntity notificacion: notificacionesDeUsuarioAEliminar){
            notificacionRepository.eliminarDestinatarioByNotificacion(notificacion.getIdentificador());
        }
    }
}
