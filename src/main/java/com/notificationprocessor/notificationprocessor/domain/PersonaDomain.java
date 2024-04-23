package com.notificationprocessor.notificationprocessor.domain;


import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilDefaultObject;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilEmail;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilText;
import com.notificationprocessor.notificationprocessor.crossCutting.utils.UtilUUID;

import java.util.UUID;



public class PersonaDomain {

    private UUID identificador;

    private String primerNombre;

    private String segundoNombre;

    private String primerApellido;

    private String segundoApellido;

    private String correoElectronico;


    public PersonaDomain(UUID identificador, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String correoElectronico) {
        this.identificador = identificador;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.correoElectronico = correoElectronico;
    }

    public PersonaDomain(){
        setIdentificador(UtilUUID.getUuidDefaultValue());
        setPrimerNombre(UtilText.getDefaultTextValue());
        setSegundoNombre(UtilText.getDefaultTextValue());
        setPrimerApellido(UtilText.getDefaultTextValue());
        setSegundoApellido(UtilText.getDefaultTextValue());
        setCorreoElectronico(UtilEmail.getDefaultValueMail());

    }
    public UUID getIdentificador() {
        return identificador;
    }

    public void setIdentificador(UUID identificador) {
        this.identificador = (UUID) UtilDefaultObject.defaultValue(identificador,UtilUUID.getUuidDefaultValue());
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = (String) UtilDefaultObject.defaultValue(segundoNombre,UtilText.getDefaultTextValue());
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = (String) UtilDefaultObject.defaultValue(primerApellido,UtilText.getDefaultTextValue());
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = (String) UtilDefaultObject.defaultValue(segundoApellido,UtilText.getDefaultTextValue());
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = (String) UtilDefaultObject.defaultValue(correoElectronico, UtilEmail.getDefaultValueMail());
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public PersonaDomain setPrimerNombre(String primerNombre) {
        this.primerNombre = (String) UtilDefaultObject.defaultValue(primerNombre,UtilText.getDefaultTextValue());
        return null;
    }

    @Override
    public String toString() {
        return "PersonaDomain{" +
                "identificador=" + identificador +
                ", primerNombre='" + primerNombre + '\'' +
                ", segundoNombre='" + segundoNombre + '\'' +
                ", primerApellido='" + primerApellido + '\'' +
                ", segundoApellido='" + segundoApellido + '\'' +
                ", correoElectronico='" + correoElectronico + '\''+
                '}';
    }
}
