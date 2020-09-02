package com.migros.courierproducerapp.service.impl;

import com.migros.courierproducerapp.domain.Courier;
import com.migros.courierproducerapp.repository.CourierRepository;
import com.migros.courierproducerapp.service.CourierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Courier}.
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {


    private final Logger log = LoggerFactory.getLogger(CourierServiceImpl.class);

    private  final CourierRepository courierRepository;

    public CourierServiceImpl(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }


    /**
     * Save a courier.
     *
     * @param courier the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Courier save(Courier courier) {
        log.debug("Request to save Courier : {}", courier);
        return courierRepository.save(courier);
    }


    /**
     * Get one courier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Courier> findOne(String id) {
        log.debug("Request to get Courier : {}", id);
        return courierRepository.findById(id);
    }

    /**
     * Delete the courier by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Courier : {}", id);
        courierRepository.deleteById(id);
    }

    /**
     * Get Total Travel Distance of a courier.
     *
     * @param courierID the entity to get total travel distance.
     * @return double distance.
     */
    @Override
    public Double getTotalTravelDistance(String courierID) {
        List<Courier>  couries = courierRepository.findAllByCourierID(courierID);
        Double total = 0.0;
        Courier temp = new Courier();
        for(Courier courier : couries) {
            if(temp.getCourierID() == null) {
                temp = courier;
            } else {
                total += distFrom(temp.getLat(), temp.getLng(), courier.getLat(), courier.getLng());
            }
            temp = courier;
        }
        return total;
    }

    /**
     * Get all the couriers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Courier> findAll(Pageable pageable) {
        log.debug("Request to get all Courier");
        return courierRepository.findAll(pageable);
    }

    /**
     * Calculate distance function and return true if it is less than 100
     *
     * @param lat1 lat of store
     * @param lng1 lng of store
     * @param lat2 lat of courier
     * @param lng2 lng of courier
     */
    private double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = (earthRadius * c);
        return dist;
    }

}
