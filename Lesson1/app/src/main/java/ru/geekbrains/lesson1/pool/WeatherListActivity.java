package ru.geekbrains.lesson1.pool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ru.geekbrains.lesson1.R;
import ru.geekbrains.lesson1.util.SingleCitiesPresenter;

public class WeatherListActivity extends AppCompatActivity {

    private SingleCitiesPresenter singleCitiesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_list);
        InitListView();
    }


    private void InitListView() {

        singleCitiesPresenter = SingleCitiesPresenter.getInstance();
        WeatherCityPool weatherCityPool = singleCitiesPresenter.getWeatherCityPool();

        setListToView(R.id.city_weather_list, weatherCityPool.getCities());
        setListToView(R.id.temper_weather_list, weatherCityPool.getTemperatures());

    }

    private void setListToView(Integer resId, String[] viewItems){
        ListView listView = findViewById(resId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, viewItems);
        listView.setAdapter(adapter);
    }
}
