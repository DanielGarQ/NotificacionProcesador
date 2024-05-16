package com.notificationprocessor.notificationprocessor.repository;



import java.util.List;
import com.notificationprocessor.notificationprocessor.entity.BuzonNotificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

public interface BuzonNotificacionRepository extends JpaRepository<BuzonNotificacionEntity, UUID> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM buzon b WHERE b.persona = ?1", nativeQuery = true)
    void deleteByPersonaIdentificador(UUID identificador);

    @Transactional
    @Query(value = "SELECT * FROM buzon b WHERE b.persona = ?1", nativeQuery = true)
    BuzonNotificacionEntity findByPersona(UUID identificador);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM buzon_notificacion b WHERE b.buzon_identificador = ?1", nativeQuery = true)
    void deleteIntermediaByBuzonIdentificador(UUID identificador);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM buzon_notificacion b WHERE b.identificador = ?1", nativeQuery = true)
    void deleteNotificacionDeBuzon(UUID identificador);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO buzon_notificacion(buzon_identificador, identificador) VALUES (?1, ?2)", nativeQuery = true)
    void saveBuzonNotificacion(UUID identificadorBuzon, UUID identificadorNotificacion);
}
