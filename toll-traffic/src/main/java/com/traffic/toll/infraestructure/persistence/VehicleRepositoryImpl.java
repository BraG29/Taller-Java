package com.traffic.toll.infraestructure.persistence;

import com.traffic.dtos.vehicle.LicensePlateDTO;
import com.traffic.dtos.vehicle.TagDTO;
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
import jakarta.transaction.Transactional;

import java.util.List;
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

    private List<Vehicle> vehicles;

    @PostConstruct
    public void initVehicles(){

        vehicles = List.of(
                new NationalVehicle(null,
                        identifierRepository.findTagById(1L).orElseThrow(),
                        identifierRepository.findLicensePlateById(1L).orElseThrow()),
                new ForeignVehicle(null,
                        identifierRepository.findTagById(2L).orElseThrow()),
                new NationalVehicle(null,
                        identifierRepository.findTagById(3L).orElseThrow(),
                        identifierRepository.findLicensePlateById(2L).orElseThrow()),
                new ForeignVehicle(null,
                        identifierRepository.findTagById(4L).orElseThrow()),
                new NationalVehicle(null,
                        identifierRepository.findTagById(5L).orElseThrow(),
                        identifierRepository.findLicensePlateById(3L).orElseThrow()),
                new ForeignVehicle(null,
                        identifierRepository.findTagById(6L).orElseThrow())
        );

        criteriaBuilder = em.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery(Vehicle.class);
        root = criteriaQuery.from(Vehicle.class);

        for(Vehicle v : vehicles){
            this.save(v);
        }

    }

    public void save(Vehicle vehicle){
        em.merge(vehicle);
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
