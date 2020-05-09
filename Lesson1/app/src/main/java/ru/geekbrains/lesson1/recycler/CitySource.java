package ru.geekbrains.lesson1.recycler;

import android.content.res.Resources;
import android.content.res.TypedArray;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

            int detailSize = getArraySize();

                // изображения погоды (солнце, дождь , ветер, итд...)
            int[] pictures = getImageArray();

            // наполнение источника данных на 1 неделю
            for (int i = 0; i < detailSize; i++) {
                Date curDate = getDate(i);
                Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String s = formatter.format(curDate);
                dataSource.add(new Wed(s, pictures[getWeatherPictureIndex(i) - 1], getTemperature(i)));
            }
            return this;
        }

        // получаем температуру
        private int getTemperature(int num){
            // low part of temperature
            return parcel.getTemperatureOf5Days().get(num).getTemperature();
        }

    // получаем дату и время
    private Date getDate(int num){

        long dt =  parcel.getTemperatureOf5Days().get(num).getDt();
        return new java.util.Date((long)dt*1000);
    }

    // получаем размер
    private int getArraySize(){
        return parcel.getTemperatureOf5Days().size();
    }
       // получаем индекс погоды (солнце, дождь , ветер, итд...)
        private int getWeatherPictureIndex(int num){
            // hight part of temperature
            return parcel.getTemperatureOf5Days().get(num).getIco();
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
