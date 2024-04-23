package com.notificationprocessor.notificationprocessor.controller;

import com.notificationprocessor.notificationprocessor.domain.NotificacionDomain;
import com.notificationprocessor.notificationprocessor.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/v1")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/dummy_notificacion")
    public NotificacionDomain dummy(){
        return new NotificacionDomain();
    }

    @GetMapping("/notificacion")
    public List<NotificacionDomain> get(){

        return notificacionService.findAll();
    }

    @GetMapping("/notificacion/uuid")
    public NotificacionDomain getNotificacion(UUID identificador){
        return notificacionService.findById(identificador);
    }

    @PostMapping("/notificacion")
    public ResponseEntity<UUID> create(@Validated @RequestBody NotificacionDomain notificacion){
        return new ResponseEntity<>(notificacionService.saveNotificacion(notificacion), HttpStatus.OK);
    }

    @DeleteMapping("/notificacion")
    public void delete(@RequestParam(required = true) UUID identificador){
        notificacionService.deleteNotificacion(identificador);
    }


}
