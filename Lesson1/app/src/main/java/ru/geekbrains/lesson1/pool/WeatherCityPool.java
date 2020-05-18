package ru.geekbrains.lesson1.pool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ru.geekbrains.lesson1.net.WeatherWebDetails;

public class WeatherCityPool implements Serializable {
    private Map<String, Integer> citiesPool = new HashMap<>();

    public void setCityWeather(String city, Integer temperature)
    {
        citiesPool.put(city,temperature);
    }

    public int getCityWeather(String city)
    {
        return citiesPool.get(city);
    }

    public WeatherCityPool getCityListWeather()
    {
        return this;
    }

    public String [] getCities(){
        Set<String> keySet = citiesPool.keySet();
        ArrayList<String> listOfKeys = new ArrayList<String>(keySet);
        return listOfKeys.toArray(new String[listOfKeys.size()]);
    }

    public String [] getTemperatures(){
        String [] cityes = getCities();
        String [] temperature = new String[cityes.length];

        for (int i = 0;i<cityes.length; i++)
        {
            temperature[i] = Integer.toString(citiesPool.get(cityes[i]));
        }

        return temperature;
    }

}
