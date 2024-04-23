package com.notificationprocessor.notificationprocessor.repository;

import com.notificationprocessor.notificationprocessor.entity.BuzonNotificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface BuzonNotificacionRepository extends JpaRepository<BuzonNotificacionEntity, UUID> {
}
