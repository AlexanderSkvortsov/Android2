package ru.geekbrains.lesson1.util;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import ru.geekbrains.lesson1.net.City;
import ru.geekbrains.lesson1.net.WeatherRequest5Days;
import ru.geekbrains.lesson1.net.WeatherWebDetails;

public class CitiesPool implements Serializable {
    private Map<String, ArrayList<WeatherWebDetails>> citiesPool = new HashMap<>();

    private int getWeatherState() {
        int min = 1;
        int max = 12;
        int diff = max - min;
        Random random = new Random();
        int i = random.nextInt(diff + 1);
        i += min;
        return i;
    }

    private ArrayList<WeatherWebDetails> detailsCreate(WeatherRequest5Days request5Days)
    {
        ArrayList<WeatherWebDetails> weatherDetails = new ArrayList<>();

        if (request5Days == null)
        {
            for (int i =0 ;i< 7;i++) {
                WeatherWebDetails weatherWebDetails = new WeatherWebDetails(0,
                        0,
                        0,
                        getWeatherState(),
                        0
                );
                weatherDetails.add(weatherWebDetails);
            }
        }
        else {
            for (ru.geekbrains.lesson1.net.List ls : request5Days.getList()) {
                WeatherWebDetails weatherWebDetails = new WeatherWebDetails(ls.getMain().getTempCelsius(),
                        ls.getMain().getPressureR(),
                        0,  // TODO
                        getWeatherState(), // TODO
                        ls.getDt()
                );
                weatherDetails.add(weatherWebDetails);
            }
        }
      return    weatherDetails;
    }

    public CitiesPool(String cityIs ,WeatherRequest5Days request5Days) {
        addCity(cityIs, request5Days);
    }

    public String getCity(int cityIndex){
        return  (String) citiesPool.keySet().toArray()[cityIndex];
    }

    public boolean checkCity(String cityName){
        try {
            ArrayList<WeatherWebDetails> val = citiesPool.get(cityName);
            return  val!=null;
        }
        catch (NullPointerException e) {
            return false;
        }
    }

    public void addCity(String cityIs, WeatherRequest5Days request5Days){
        citiesPool.put(cityIs, detailsCreate(request5Days));
    }

    public String [] getCities(){
        Set<String> keySet = citiesPool.keySet();
        ArrayList<String> listOfKeys = new ArrayList<String>(keySet);
        return listOfKeys.toArray(new String[listOfKeys.size()]);
    }

    public ArrayList<WeatherWebDetails> getWeather5DaysDetails(String cityName)
    {
        return citiesPool.get(cityName);
    }

}
