package ru.geekbrains.lesson1.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import ru.geekbrains.lesson1.R;
import ru.geekbrains.lesson1.pool.WeatherCityPool;

public final  class SingleCitiesPresenter {

        //Внутреннее поле, будет хранить единственный экземпляр
        private static SingleCitiesPresenter instance = null;

        // Поле для синхронизации
        private static final Object syncObj = new Object();

        private ArrayList<String> listOfCities;
        private boolean isDarkTheme;
        private WeatherCityPool weatherCityPool;

    public WeatherCityPool getWeatherCityPool() {
        return weatherCityPool;
    }

    public void setWeatherCityPool(WeatherCityPool weatherCityPool) {
        this.weatherCityPool = weatherCityPool;
    }

    // Конструктор (вызывать извне его нельзя, поэтому он приватный)
        private SingleCitiesPresenter(){
            String[] temp = App.getContext().getResources().getStringArray(R.array.city_names);
            listOfCities = new ArrayList<String>( Arrays.asList(temp));
        }

        public void setCity(String cityName)
        {
            listOfCities.add(cityName);
        }

        public String[] getCitiesList()
        {
            String[] array = new String[listOfCities.size()];

            int i = 0;
            for(String value: listOfCities) {
                array[i++] = value;
            }

            return array;
        }

        public boolean isDarkTheme() {
            return isDarkTheme;
        }

        public void setDarkTheme(boolean darkTheme) {
            isDarkTheme = darkTheme;
        }

    // Метод, который возвращает экземпляр объекта.
        // Если объекта нет, то создаем его.
        public static SingleCitiesPresenter getInstance(){
            // Здесь реализована «ленивая» инициализация объекта,
            // то есть, пока объект не нужен, не создаем его.
            synchronized (syncObj) {
                if (instance == null) {
                    instance = new SingleCitiesPresenter();
                }
                return instance;
            }
        }
}
