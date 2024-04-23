package com.notificationprocessor.notificationprocessor.controller;



import com.notificationprocessor.notificationprocessor.domain.BuzonNotificacionDomain;
import com.notificationprocessor.notificationprocessor.service.BuzonNotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@RestController
@RequestMapping("/v1")

public class BuzonNotificacionController {

    @Autowired
    private BuzonNotificacionService buzonNotificacionService;

    @GetMapping("/dummy_buzonnotificacion")
    BuzonNotificacionDomain dummy(){
        return new BuzonNotificacionDomain();
    }

    @GetMapping("/buzonnotificacion")
    public List<BuzonNotificacionDomain> get(@RequestParam(required = true) String nombre){return new ArrayList<>();}

    @PostMapping("/buzonnotificacion")
    public ResponseEntity<UUID> create(@Validated @RequestBody BuzonNotificacionDomain buzonNotificacion){
        return new ResponseEntity<>(buzonNotificacionService.saveBuzonNotificacion(buzonNotificacion), HttpStatus.OK);
    }

    @PutMapping("/buzonnotificacion")
    public List<ResponseEntity<BuzonNotificacionDomain>> update(@RequestParam(required = true) String persona,@Validated @RequestBody BuzonNotificacionDomain nombre){
        return new ArrayList<>();
    }

    @DeleteMapping("/buzonnotificacion")
    public List<ResponseEntity<BuzonNotificacionDomain>> delete(@RequestParam(required = true) UUID identificador){
        return new ArrayList<>();
    }

}
