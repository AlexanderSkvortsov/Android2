package ru.geekbrains.lesson1;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CitiesPool implements Serializable {
    private Map<String, ArrayList<Integer>> citiesPool = new HashMap<>();

    private int getTemperature() {
        int min = -40;
        int max = 50;
        int diff = max - min;
        Random random = new Random();
        int i = random.nextInt(diff + 1);
        i += min;
        return i;
    }

    private ArrayList<Integer> getTemperatureOfWeek()
    {
        ArrayList<Integer> temperatureOfWeek = new ArrayList<>();
        temperatureOfWeek.clear();
        for (int i = 0 ;i< 7 ;i++) temperatureOfWeek.add(getTemperature());
        return temperatureOfWeek;
    }

    public CitiesPool(String[] citiesList){
        for (String s:citiesList) {
                    citiesPool.put(s,getTemperatureOfWeek());
        }
    }

    public int getTemperature(String cityName){
        Calendar calendar = Calendar.getInstance();
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        if (!checkCity(cityName))
        {
            addCity(cityName);
        }

        return   citiesPool.get(cityName).get(weekday);
    }

    public String getCity(int cityIndex){
        return  (String) citiesPool.keySet().toArray()[cityIndex];
    }

    public boolean checkCity(String cityName){
        try {
            ArrayList<Integer> val = citiesPool.get(cityName);
            return  true;
        }
        catch (NullPointerException e) {
            return false;
        }
    }

    public void addCity(String cityName){
        citiesPool.put(cityName,getTemperatureOfWeek());
    }

    public String [] getCities(){
        Set<String> keySet = citiesPool.keySet();
        ArrayList<String> listOfKeys = new ArrayList<String>(keySet);
        return listOfKeys.toArray(new String[listOfKeys.size()]);
    }

    public ArrayList<Integer> getTemperatureOfWeek(String cityName)
    {

        return citiesPool.get(cityName);
    }

    public int[] getTemperatureOfWeekAsArray(String cityName)
    {
        ArrayList<Integer> tempArray = citiesPool.get(cityName);
        int[] array = new int[tempArray.size()];

        int i = 0;
        for(Integer value: tempArray) {
            array[i++] = value;
        }

        return array;
    }

}
