package com.migros.courierproducerapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.migros.courierproducerapp.config.KafkaProperties;
import com.migros.courierproducerapp.domain.Courier;
import com.migros.courierproducerapp.service.dto.CourierDTO;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class CourierKafkaService {


    private final Logger log = LoggerFactory.getLogger(CourierKafkaService.class);

    private static final String TOPIC = "topic_courier";

    private final KafkaProperties kafkaProperties;

    private final static Logger logger = LoggerFactory.getLogger(CourierKafkaService.class);
    private KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CourierKafkaService(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void initialize(){
        log.info("Kafka producer initializing...");
        this.producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        log.info("Kafka producer initialized");
    }

    public void alertCourierStatus(Courier courier) {
        try {
            CourierDTO courierDTO = new CourierDTO(courier);
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String message = objectMapper.writeValueAsString(courierDTO);
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, message);
            producer.send(record);
            log.debug("Record has been send: {}", record);
        } catch (JsonProcessingException e) {
            logger.error("Could not send store alert", e);
            throw new CourierServiceException(e);
        }
    }
    @PreDestroy
    public void shutdown() {
        log.info("Shutdown Kafka producer");
        producer.close();
    }
}
