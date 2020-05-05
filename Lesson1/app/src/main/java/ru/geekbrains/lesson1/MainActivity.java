package ru.geekbrains.lesson1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ru.geekbrains.lesson1.CitiesConst;

public class MainActivity extends AppCompatActivity {

    TextView newCityName;
    TextView newCityTemperature;
    TextView newPressure;
    TextView newPressureName;
    TextView newWind;
    TextView newWindName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitView();
        PullView();
    }

    private void InitView()
    {
        newCityName = findViewById(R.id.cityName);
        newPressure = findViewById(R.id.cityPressure);;
        newPressureName = findViewById(R.id.txtPressureName);;
        newWind = findViewById(R.id.cityWind);;
        newWindName = findViewById(R.id.txtWindName);;
        newCityTemperature = findViewById(R.id.cityTemperature);
    }

    private void PullView()
    {
        Intent intent = getIntent();

        setTextField(intent, CitiesConst.CITY_FROM_LIST, newCityName);
        setTextField(intent, CitiesConst.CITY_TEMPERATURE, newCityTemperature);

        boolean value1;
        value1 = intent.getBooleanExtra(CitiesConst.PRESSURE_SHOW, true); //if it's a string you stored.
        showField(value1, newPressure);
        showField(value1, newPressureName);

        value1 = intent.getBooleanExtra(CitiesConst.WIND_SHOW,true); //if it's a string you stored.
        showField(value1, newWind);
        showField(value1, newWindName);

    }

    private void setTextField(Intent intent, String cityFromList, TextView newCityValue) {
        String value;
        value = intent.getStringExtra(cityFromList); //if it's a string you stored.
        if (value != null) {
            newCityValue.setText(value);
        }
    }

    private void showField(boolean value1, TextView newPressure) {
        newPressure.setVisibility((value1) ? View.VISIBLE : View.INVISIBLE);
    }

}
