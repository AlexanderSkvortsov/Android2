package ru.geekbrains.lesson1.net;

import java.util.HashMap;
import java.util.Map;

public class Coord {

    private Double lat;
    private Double lon;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
