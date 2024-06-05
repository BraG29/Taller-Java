package com.traffic.toll.infraestructure.persistence;

import com.traffic.toll.domain.entities.LicensePlate;
import com.traffic.toll.domain.entities.Tag;
import com.traffic.toll.domain.repositories.IdentifierRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

@ApplicationScoped
public class IdentifierRepositoryImpl implements IdentifierRepository {

    @PersistenceContext
    EntityManager session;
    EntityManager em;

    @PostConstruct
    public void setUp(){
        em = session.getEntityManagerFactory().createEntityManager();
    }

    @Override
    public Optional<Tag> findTagById(Long id) {
        return Optional.ofNullable(em.find(Tag.class, id));
    }

    @Override
    public Optional<LicensePlate> findLicensePlateById(Long id) {
        return Optional.ofNullable(em.find(LicensePlate.class, id));
    }
}
