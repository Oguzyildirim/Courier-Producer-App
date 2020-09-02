package com.migros.courierproducerapp.service;

import com.migros.courierproducerapp.domain.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


/**
 * Service Interface for managing {@link Courier}.
 */
public interface CourierService {

    /**
     * Save a courier.
     *
     * @param courier the entity to save.
     * @return the persisted entity.
     */
    Courier save(Courier courier);

    /**
     * Get Total Travel Distance of a courier.
     *
     * @param courierID the entity to get total travel distance.
     * @return double distance
     */
    Double getTotalTravelDistance(String courierID);

    /**
     * Get all the couriers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Courier> findAll(Pageable pageable);

    /**
     * Get the "id" courier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Courier> findOne(String id);

    /**
     * Delete the "id" courier.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
