package com.notificationprocessor.notificationprocessor.domain;



import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilDate;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilDefaultObject;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilText;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilUUID;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class NotificacionDomain {

    private UUID identificador;
    private PersonaDomain autor;
    private String titulo;
    private String contenido;
    private LocalDate fechaCreacion;
    private String estado;
    private LocalDate fechaProgramada;
    private String tipoEntrega;

    private List<PersonaDomain> destinatario;


    public NotificacionDomain() {
        setIdentificador(UtilUUID.getUuidDefaultValue());
        setAutor(new PersonaDomain());
        setTitulo(UtilText.getDefaultTextValue());
        setContenido(UtilText.getDefaultTextValue());
        setFechaCreacion(UtilDate.getDefaultValueDate());
        setEstado(UtilText.getDefaultTextValue());
        setFechaProgramada(UtilDate.getDefaultValueDate());
        setTipoEntrega(UtilText.getDefaultTextValue());
        setDestinatario(new ArrayList<>());
    }

    public NotificacionDomain(UUID identificador, PersonaDomain autor, String titulo, String contenido, LocalDate fechaCreacion, String estado, LocalDate fechaProgramada, String tipoEntrega, List<PersonaDomain> destinatario) {
        this.identificador = identificador;
        this.autor = autor;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.fechaProgramada = fechaProgramada;
        this.tipoEntrega = tipoEntrega;
        this.destinatario = destinatario;
    }

    public UUID getIdentificador() {
        return identificador;
    }

    public void setIdentificador(UUID identificador) {
        this.identificador = identificador;
    }

    public PersonaDomain getAutor() {
        return autor;
    }

    public void setAutor(PersonaDomain autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(LocalDate fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public String getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(String tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public List<PersonaDomain> getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(List<PersonaDomain> destinatario) {
        this.destinatario = destinatario;
    }
}
