package com.notificationprocessor.notificationprocessor.domain;



import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilDate;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilDefaultObject;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilText;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilUUID;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class NotificacionDomain {

    private UUID identificador;
    private PersonaDomain autor;
    private String titulo;
    private String contenido;
    private Date fechaCreacion;
    private String estado;
    private Date fechaProgramada;
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

    public NotificacionDomain(UUID identificador, PersonaDomain autor, String titulo, String contenido, Date fechaCreacion, String estado, Date fechaProgramada, String tipoEntrega, List<PersonaDomain> destinatario) {
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
        this.identificador = (UUID) UtilDefaultObject.defaultValue(identificador,UtilUUID.getUuidDefaultValue());
    }

    public PersonaDomain getAutor() {
        return autor;
    }

    public void setAutor(PersonaDomain autor) {
        this.autor = (PersonaDomain) UtilDefaultObject.defaultValue(autor,new PersonaDomain());
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {

        this.titulo = (String) UtilDefaultObject.defaultValue(titulo,UtilText.getDefaultTextValue());
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = (String) UtilDefaultObject.defaultValue(contenido,UtilText.getDefaultTextValue());
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = (Date) UtilDefaultObject.defaultValue(fechaCreacion,UtilDate.getDefaultValueDate());
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = (String) UtilDefaultObject.defaultValue(estado,UtilText.getDefaultTextValue());
    }

    public Date getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(Date fechaProgramada) {
        this.fechaProgramada = (Date) UtilDefaultObject.defaultValue(fechaProgramada, UtilDate.getDefaultValueDate());
    }

    public String getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(String tipoEntrega) {

        this.tipoEntrega = (String) UtilDefaultObject.defaultValue(tipoEntrega,UtilText.getDefaultTextValue());
    }

    public List<PersonaDomain> getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(List<PersonaDomain> destinatario) {
        this.destinatario = destinatario;
    }
}
