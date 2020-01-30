package ie.tcd.cs7cs3.undersvc.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * point is a class to represent a geographical location in JSON
 */
public class point {
    private Double lat;
    private Double lng;

    public point() {
        // jackson
    }

    public point(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        point point = (point) o;
        return lat.equals(point.lat) &&
                lng.equals(point.lng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lng);
    }

    @JsonProperty
    public Double getLat() {
        return lat;
    }

    @JsonProperty
    public Double getLng() {
        return lng;
    }
}
