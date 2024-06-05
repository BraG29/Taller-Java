package com.traffic.toll.infraestructure.persistence;

import com.traffic.toll.domain.entities.*;
import com.traffic.toll.domain.repositories.IdentifierRepository;
import com.traffic.toll.domain.repositories.VehicleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Optional;

@ApplicationScoped
public class VehicleRepositoryImpl implements VehicleRepository {

    @PersistenceContext
    private EntityManager em;
    /**
     * Se inyectara <code>IdentifierRepository</code> para cargar vehiculos de prueba
     * TODO: Eliminar para produccion
     */
    @Inject
    private IdentifierRepository identifierRepository;
//    private EntityManager em;
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<Vehicle> criteriaQuery;
    private Root<Vehicle> root;

    @PostConstruct
    public void setUp(){

        criteriaBuilder = em.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery(Vehicle.class);
        root = criteriaQuery.from(Vehicle.class);

    }

    @Override
    public void save(Vehicle vehicle){
        vehicle.setTag(em.merge(vehicle.getTag()));

        if(vehicle instanceof NationalVehicle){
            ((NationalVehicle) vehicle)
                    .setLicensePlate(em
                            .merge(((NationalVehicle) vehicle)
                                    .getLicensePlate()));
        }
        em.persist(vehicle);
        em.flush();
    }

    @Override
    public Optional<Vehicle> findByTag(Tag tag) {

        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get("tag"), tag));

        try{
            return Optional.of(em.createQuery(criteriaQuery).getSingleResult());

        }catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Vehicle> findByLicensePlate(LicensePlate licensePlate) {

        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get("licensePlate"), licensePlate));

        try{
            return Optional.of(em.createQuery(criteriaQuery).getSingleResult());

        }catch (NoResultException e){
            return Optional.empty();
        }
    }
}
