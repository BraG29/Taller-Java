package com.traffic.toll.domain.repositories;

import com.traffic.toll.domain.entities.Tariff;

import java.util.Optional;

public interface TariffRepository {

//    public void initTariffList();
    /**
     * Busca por una tarifa
     * @param tariffClass clase de la tarifa a encontrar, puede ser de dos tipos:
     *                    <ul>
     *                      <li>
     *                          {@link com.traffic.toll.domain.entities.CommonTariff}
     *                      </li>
     *                      <li>
     *                          {@link com.traffic.toll.domain.entities.PreferentialTariff}
     *                      </li>
     *                    </ul>
     * @return un <code>Optional</code> con el objeto <code>Tariff</code>
     */
    public <T extends Tariff> Optional<Tariff> findTariff(Class<T> tariffClass);
    public Optional<Tariff> save(Tariff tariff);

}
