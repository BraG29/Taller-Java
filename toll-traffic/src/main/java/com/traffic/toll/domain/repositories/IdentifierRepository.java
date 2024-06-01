package com.traffic.toll.domain.repositories;

import com.traffic.toll.domain.entities.LicensePlate;
import com.traffic.toll.domain.entities.Tag;

import java.util.Optional;

public interface IdentifierRepository {

    public Optional<Tag> findTagById(Long id);
    public Optional<LicensePlate> findLicensePlateById(Long id);
}
