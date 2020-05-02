package ru.geekbrains.lesson1.recycler;

import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.lesson1.util.Parcel;
import ru.geekbrains.lesson1.R;

public class CitySource implements CityDataSource {

        private final List<Wed> dataSource;   // создаем этот источник данных
        private final Resources resources;    // ресурсы приложения
        private final Parcel parcel;          // данные оз настроек

    public CitySource(Resources resources, Parcel parcel) {
            dataSource = new ArrayList<>(7);
            this.resources = resources;
            this.parcel = parcel;
        }

        public CitySource init() {

            // строки дней недели
            String[] descriptions = resources.getStringArray(R.array.week_Name);

            // изображения погоды (солнце, дождь , ветер, итд...)
            int[] pictures = getImageArray();

            // наполнение источника данных на 1 неделю
            for (int i = 0; i < descriptions.length; i++)
                dataSource.add(new Wed(descriptions[i], pictures[ getWeatherPictureIndex(i)-1], getTemperature(i)));
            return this;
        }

        // получаем температуру
        private int getTemperature(int num){
            // low part of temperature
            return parcel.getTemperatureOfWeek()[num] %100;
        }

       // получаем индекс погоды (солнце, дождь , ветер, итд...)
        private int getWeatherPictureIndex(int num){
            // hight part of temperature
            return Math.abs(parcel.getTemperatureOfWeek()[num] /100);
        }

        //
        public Wed getSoc(int position) {
            return dataSource.get(position);
        }

        public int size() {
            return dataSource.size();
        }

        // создаем массив ресурсов картинок, чтобы сразу нарисовать
        private int[] getImageArray() {

            TypedArray pictures = resources.obtainTypedArray(R.array.weather_state_imgs);

            int length = pictures.length();

            int[] result = new int[length];
            for (int i = 0; i < length; i++)
                result[i] = pictures.getResourceId(i, 0);

            pictures.recycle();
            return result;
        }
}
