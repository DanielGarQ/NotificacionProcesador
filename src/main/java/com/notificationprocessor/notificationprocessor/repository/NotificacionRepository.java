package com.notificationprocessor.notificationprocessor.repository;


import com.notificationprocessor.notificationprocessor.entity.NotificacionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface NotificacionRepository extends JpaRepository<NotificacionEntity, UUID>{

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM destinatario_notificacion  WHERE identificador=?1",nativeQuery = true)
    void eliminarDestinatarioById(UUID identificador);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM destinatario_notificacion  WHERE notificacion_identificador=?1",nativeQuery = true)
    void eliminarDestinatarioByNotificacion(UUID identificador);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM notificacion  WHERE persona=?1",nativeQuery = true)
    void eliminarNotificacionByAutor(UUID identificador);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM buzon_notificacion WHERE identificador =?1",nativeQuery = true)
    void eliminarNotificacionDeBuzon(UUID identificador);

    @Transactional
    @Query(value = "SELECT * FROM notificacion WHERE persona =?1",nativeQuery = true)
    List<NotificacionEntity> findByAutor(UUID persona);
}
