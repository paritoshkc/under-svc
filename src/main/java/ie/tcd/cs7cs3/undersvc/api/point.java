package ie.tcd.cs7cs3.undersvc.api;

/**
 * point is a class to represent a geographical location in JSON
 */
public class point {
    private Double lat;
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public point(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
