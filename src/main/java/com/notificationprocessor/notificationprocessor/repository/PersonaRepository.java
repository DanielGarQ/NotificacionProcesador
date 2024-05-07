package com.notificationprocessor.notificationprocessor.repository;

import com.notificationprocessor.notificationprocessor.entity.PersonaEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PersonaRepository extends JpaRepository<PersonaEntity, UUID> {
    @Query(value = "SELECT * FROM persona WHERE correo_electronico = ?1",nativeQuery = true)
    PersonaEntity findBycorreoElectronico(String correoElectronico);

    @Modifying
    @Transactional
    @Query(value = "UPDATE public.persona SET  primer_apellido=?1, primer_nombre=?2, segundo_apellido=?3, segundo_nombre=?4 WHERE correo_electronico =?5",nativeQuery = true)
    void updateBycorreoElectronico (String primerApellido,String primerNombre, String segundoApellido, String segundoNombre,String correoElectronico);
}
