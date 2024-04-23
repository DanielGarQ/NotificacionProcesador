package com.notificationprocessor.notificationprocessor.repository;


import com.notificationprocessor.notificationprocessor.entity.NotificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface NotificacionRepository extends JpaRepository<NotificacionEntity, UUID>{

}
