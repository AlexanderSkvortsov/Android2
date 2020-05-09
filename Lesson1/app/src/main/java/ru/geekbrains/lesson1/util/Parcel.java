package ru.geekbrains.lesson1.util;

import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;

import ru.geekbrains.lesson1.net.WeatherWebDetails;

public class Parcel implements Serializable {

    private final  ArrayList<WeatherWebDetails> temperature5Days;
    private final String cityName;

    public ArrayList<WeatherWebDetails>  getTemperatureOf5Days() {
        return  temperature5Days;
    }

    public int getTemperatureOf1Day() {
        return  temperature5Days.get(0).getTemperature();
    }

    public int getWindOf1Day() {
        return  temperature5Days.get(0).getWind();
    }

    public String getCityName() {
        return cityName;
    }

    public Parcel(String cityName, ArrayList<WeatherWebDetails> temperature5Days) {
        this.temperature5Days = temperature5Days;
        this.cityName = cityName;
    }
}
