package com.migros.courierproducerapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


/**
 * A Courier.
 */
@Document(collection = "courier")
public class Courier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("courierID")
    private String courierID;

    @NotNull
    @Field("lat")
    private Double lat;

    @NotNull
    @Field("lng")
    private Double lng;

    @Field("timestamp")
    private LocalDateTime timestamp;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourierID() {
        return courierID;
    }

    public void setCourierID(String courierID) {
        this.courierID = courierID;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Courier)) return false;
        Courier courier = (Courier) o;
        return getId().equals(courier.getId()) &&
                getCourierID().equals(courier.getCourierID()) &&
                getLat().equals(courier.getLat()) &&
                getLng().equals(courier.getLng()) &&
                getTimestamp().equals(courier.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCourierID(), getLat(), getLng(), getTimestamp());
    }

    @Override
    public String toString() {
        return "Courier{" +
                "id=" + id +
                ", courierID='" + courierID + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", timestamp=" + timestamp +
                '}';
    }
}
