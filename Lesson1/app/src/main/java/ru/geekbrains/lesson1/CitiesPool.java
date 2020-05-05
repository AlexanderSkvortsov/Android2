package ru.geekbrains.lesson1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CitiesPool {
    private Map<String,Integer> citiesPool = new HashMap<>();

    private int getTemperature(){
        int min = -40;
        int max = 50;
        int diff = max - min;
        Random random = new Random();
        int i = random.nextInt(diff + 1);
        i += min;
        return i;
    }

    public CitiesPool(String[] citiesList){

        for (String s:citiesList
             ) {
            citiesPool.put(s,getTemperature());
        }
    }

    public int getTemperature(String cityName){
        int val;
        try {
            val = citiesPool.get(cityName);
            return  val;
        }
        catch (NullPointerException e)
        {
            val = getTemperature();
            citiesPool.put(cityName,val);
        }
        return  val;
    }

    public String getCity(int cityIndex){
        return  (String) citiesPool.keySet().toArray()[cityIndex];
    }

    public boolean checkCity(String cityName){
        try {
            int val = citiesPool.get(cityName);
            return  true;
        }
        catch (NullPointerException e) {
            return false;
        }
    }

    public void addCity(String cityName){
        citiesPool.put(cityName,getTemperature());
    }

    public String [] getCities(){
        Set<String> keySet = citiesPool.keySet();
        ArrayList<String> listOfKeys = new ArrayList<String>(keySet);
        return listOfKeys.toArray(new String[listOfKeys.size()]);
    }
}
