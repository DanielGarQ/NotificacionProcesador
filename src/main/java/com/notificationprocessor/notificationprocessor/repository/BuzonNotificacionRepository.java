package com.notificationprocessor.notificationprocessor.repository;

import com.notificationprocessor.notificationprocessor.entity.BuzonNotificacionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface BuzonNotificacionRepository extends JpaRepository<BuzonNotificacionEntity, UUID> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM buzon b WHERE b.persona = ?1",nativeQuery = true)
    void deleteBypersonaIdentificador(UUID identificador);



    @Transactional
    @Query(value = "SELECT b.buzon_identificador,b.nombre,b.persona FROM buzon b WHERE b.persona = ?1",nativeQuery = true)
    BuzonNotificacionEntity findByPropietario(UUID identificador);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM buzon_notificacion b WHERE b.buzon_identificador = ?1",nativeQuery = true)
    void deleteIntermediaByBuzonIdentificador(UUID identificador);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO buzon_notificacion(buzon_identificador, identificador) VALUES (?1, ?2)",nativeQuery = true)
    void saveBuzonNotificacion(UUID identificadorBuzon,UUID identificadorNotificacion);



}
