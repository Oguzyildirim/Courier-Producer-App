package com.migros.courierproducerapp.web.rest;


import com.migros.courierproducerapp.domain.Courier;
import com.migros.courierproducerapp.repository.CourierRepository;
import com.migros.courierproducerapp.service.CourierKafkaService;
import com.migros.courierproducerapp.service.CourierService;
import com.migros.courierproducerapp.util.HeaderUtil;
import com.migros.courierproducerapp.util.PaginationUtil;
import com.migros.courierproducerapp.util.ResponseUtil;
import com.migros.courierproducerapp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * REST controller for managing {@link com.migros.courierproducerapp.domain.Courier}.
 */
@RestController
@RequestMapping("/api")
public class CourierController {

    private final Logger log = LoggerFactory.getLogger(CourierController.class);

    private static final String ENTITY_NAME = "courierCourier";

    @Value("${migros.clientApp.name}")
    private String applicationName;

    private final CourierRepository courierRepository;

    private final CourierKafkaService courierKafkaService;

    private final CourierService courierService;

    public CourierController(CourierRepository courierRepository, CourierKafkaService courierKafkaService, CourierService courierService) {
        this.courierRepository = courierRepository;
        this.courierKafkaService = courierKafkaService;
        this.courierService = courierService;
    }

    /**
     * {@code POST  /couriers} : Create a new corier.
     *
     * @param courier the courier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courier, or with status {@code 400 (Bad Request)} if the courier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/couriers")
    public ResponseEntity<Courier> createCourier(@Valid @RequestBody Courier courier) throws URISyntaxException {
        log.debug("REST request to save Courier : {}", courier);
        if (courier.getId() != null) {
            throw new BadRequestAlertException("A new courier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        courier.setTimestamp(LocalDateTime.now());
        Courier result = courierRepository.save(courier);
        log.debug("SEND courier alert for Courier: {}", courier);
        courierKafkaService.alertCourierStatus(result);
        return ResponseEntity.created(new URI("/api/coruier/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /couriers} : Updates an existing courier.
     *
     * @param courier the courier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courier,
     * or with status {@code 400 (Bad Request)} if the courier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/couriers")
    public ResponseEntity<Courier> updateCoruier(@Valid @RequestBody Courier courier) throws URISyntaxException {
        log.debug("REST request to update courier : {}", courier);
        if (courier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        courier.setTimestamp(LocalDateTime.now());
        Courier result = courierRepository.save(courier);

        log.debug("SEND courier alert for Courier: {}", courier);
        courierKafkaService.alertCourierStatus(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courier.getId()))
            .body(result);
    }


    /**
     * {@code GET  /couriers} : get all the couriers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of couriers in body.
     */
    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getCouriers(Pageable pageable) {
        log.debug("REST request to get a page of Couriers");
        Page<Courier> page = courierService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /coruiers/:id} : get the "id" courier.
     *
     * @param id the id of the coruier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/couriers/{id}")
    public ResponseEntity<Courier> getCourier(@PathVariable String id) {
        log.debug("REST request to get Courier : {}", id);
        Optional<Courier> courier = courierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courier);
    }

    /**
     * {@code DELETE  /coruiers/:id} : delete the "id" coruier.
     *
     * @param id the id of the coruier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/coruiers/{id}")
    public ResponseEntity<Void> deleteCourier(@PathVariable String id) {
        log.debug("REST request to delete Coruier : {}", id);
        courierService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code GET  /couriers} : get a courier distance by courierID.
     * @param courierID the courier courierID code.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body double distance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/couriers/getCourierDistanceByCourierID")
    public ResponseEntity<Double> getCourierDistanceByCourierID(@RequestParam(value = "courierID") String courierID) {
        log.debug("REST request to get a total distance of Courier by courierID");
        if (courierRepository.findAllByCourierID(courierID) == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "no courier with this courierID");
        }
        Double dist = courierService.getTotalTravelDistance(courierID);
        return ResponseEntity.ok()
                .body(dist);
    }
}
