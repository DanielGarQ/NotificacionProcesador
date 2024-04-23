package com.notificationprocessor.notificationprocessor.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "buzonNotificacion")
public class BuzonNotificacionEntity {

    @Id
    @Column(name = "identificador")
    private UUID identificador;

    @OneToOne
    @JoinColumn(name = "persona")
    private PersonaEntity propietario;

    @Column(name = "nombre", length = 30)
    private String nombre;

    @OneToMany
    @JoinColumn(name = "notificaciones")
    private List<NotificacionEntity> notificaciones;

    public BuzonNotificacionEntity(UUID identificador, PersonaEntity propietario, String nombre, List<NotificacionEntity> notificaciones) {
        this.identificador = identificador;
        this.propietario = propietario;
        this.nombre = nombre;
        this.notificaciones = notificaciones;
    }

    public BuzonNotificacionEntity() {
    }

    public UUID getIdentificador() {
        return identificador;
    }

    public void setIdentificador(UUID identificador) {
        this.identificador = identificador;
    }

    public PersonaEntity getPropietario() {
        return propietario;
    }

    public void setPropietario(PersonaEntity propietario) {
        this.propietario = propietario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<NotificacionEntity> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<NotificacionEntity> notificaciones) {
        this.notificaciones = notificaciones;
    }
}
