package com.migros.courierproducerapp.repository;

import com.migros.courierproducerapp.domain.Courier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data Mongo repository for the Courier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourierRepository extends MongoRepository<Courier, String> {

    List<Courier> findAllByCourierID(String courierID);

    Optional<Courier> findOneByCourierID(String courierID);
}
