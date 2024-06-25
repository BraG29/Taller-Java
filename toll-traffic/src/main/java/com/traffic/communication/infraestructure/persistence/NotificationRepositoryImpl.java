package com.traffic.communication.infraestructure.persistence;

import com.traffic.communication.domain.repository.NotificationRepository;
import com.traffic.communication.domain.entities.Notification;
import com.traffic.communication.domain.entities.User;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TransactionRequiredException;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class NotificationRepositoryImpl implements NotificationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Optional<Notification> save(Notification notification) {
        try{
            notification = entityManager.merge(notification);
            entityManager.flush();
            return Optional.of(notification);

        }catch (IllegalArgumentException | TransactionRequiredException e){
            System.err.println("Error en " + this.getClass() + ": " + e.getMessage());
            return Optional.empty();
        }
    }
}
