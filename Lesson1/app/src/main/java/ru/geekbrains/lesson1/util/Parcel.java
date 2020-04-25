package ru.geekbrains.lesson1.util;

import java.io.Serializable;
import java.util.ArrayList;

public class Parcel implements Serializable {

    private final int[] temperatureOfWeek;
    private final String cityName;

    public int[]  getTemperatureOfWeek() {
        return  temperatureOfWeek;
    }

    public String getCityName() {
        return cityName;
    }

    public Parcel(String cityName,int[] temperatureOfWeek) {
        this.temperatureOfWeek = temperatureOfWeek;
        this.cityName = cityName;
    }
}
