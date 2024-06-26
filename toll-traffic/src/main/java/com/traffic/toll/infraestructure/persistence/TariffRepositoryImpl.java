package com.traffic.toll.infraestructure.persistence;

import com.traffic.toll.domain.entities.CommonTariff;
import com.traffic.toll.domain.entities.PreferentialTariff;
import com.traffic.toll.domain.entities.Tariff;
import com.traffic.toll.domain.repositories.TariffRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
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
public class TariffRepositoryImpl implements TariffRepository {

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder criteriaBuilder;
    private List<Tariff> tariffList;

    @PostConstruct
    public void initTariffList(){
        criteriaBuilder = em.getCriteriaBuilder();

    }

    @Override
    public <T extends Tariff> Optional<Tariff> findTariff(Class<T> tariffClass){
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(tariffClass);
        Root<T> root = criteriaQuery.from(tariffClass);

        criteriaQuery.select(root);

        try{
            return Optional.of(em.createQuery(criteriaQuery).getSingleResult());

        }catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<Tariff> save(Tariff tariff) {
        return Optional.ofNullable(em.merge(tariff));
    }
}
