package com.traffic.communication.infraestructure.persistence;

import com.traffic.communication.domain.entities.Notification;
import com.traffic.communication.domain.entities.User;
import com.traffic.communication.domain.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TransactionRequiredException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<User> criteriaQuery;
    private Root<User> root;

    @PostConstruct
    public void setUp(){
        criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery(User.class);
        root = criteriaQuery.from(User.class);
    }

    @Override
    @Transactional
    public Optional<User> save(User user) {
        try{
            user = entityManager.merge(user);
            entityManager.flush();
            return Optional.of(user);

        }catch (IllegalArgumentException | TransactionRequiredException e){
            System.err.println("Error en " + this.getClass() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(Long userId) {
        try {
            return Optional.of(entityManager.find(User.class, userId));

        }catch (IllegalArgumentException e){
            System.err.println("Error en " + this.getClass() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        try {
            criteriaQuery.select(root);

            return entityManager.createQuery(criteriaQuery).getResultList();

        }catch (Exception e){
            System.err.printf(("""
                    Error en %s, de tipo %s: %s
                    """),
                    this.getClass(),
                    e.getClass(),
                    e.getMessage()
                    );
            return List.of();
        }
    }
}
