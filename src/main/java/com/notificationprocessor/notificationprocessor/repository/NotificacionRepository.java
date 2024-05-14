package com.notificationprocessor.notificationprocessor.repository;


import com.notificationprocessor.notificationprocessor.entity.NotificacionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;


public interface NotificacionRepository extends JpaRepository<NotificacionEntity, UUID>{

}
