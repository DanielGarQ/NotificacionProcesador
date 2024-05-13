package com.notificationprocessor.notificationprocessor.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "buzon")
public class BuzonNotificacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "buzon_identificador")
    private UUID identificador;

    @OneToOne
    @JoinColumn(name = "persona")
    private PersonaEntity propietario;

    @Column(name = "nombre", length = 30)
    private String nombre;

    @ManyToMany
    @JoinTable(
            name = "buzon_notificacion",
            joinColumns = @JoinColumn(name = "buzon_identificador"),
            inverseJoinColumns = @JoinColumn(name = "identificador")
    )
    private List<NotificacionEntity> buzon;

    public BuzonNotificacionEntity(UUID identificador, PersonaEntity propietario, String nombre, List<NotificacionEntity> buzon) {
        this.identificador = identificador;
        this.propietario = propietario;
        this.nombre = nombre;
        this.buzon = buzon;
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
        return buzon;
    }

    public void setNotificaciones(List<NotificacionEntity> notificaciones) {
        this.buzon = buzon;
    }
}
