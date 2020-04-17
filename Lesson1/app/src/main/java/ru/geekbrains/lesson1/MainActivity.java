package ru.geekbrains.lesson1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ru.geekbrains.lesson1.CitiesConst;

public class MainActivity extends AppCompatActivity implements CitiesConst{

    private TextView newCityName;
    private TextView newCityTemperature;
    private TextView newPressure;
    private TextView newPressureName;
    private TextView newWind;
    private TextView newWindName;
    private FloatingActionButton cityDetails;
    private FloatingActionButton backToMain;
    private FloatingActionButton cityWeather;

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
        cityDetails= findViewById(R.id.btnCityDetails);
        backToMain= findViewById(R.id.btnBackToMain);
        cityWeather = findViewById(R.id.btnCityWeather);
    }

    private void PullView()
    {
        final Intent intent = getIntent();

        setTextField(intent, CitiesConst.CITY_FROM_LIST, newCityName);
        setTextField(intent, CitiesConst.CITY_TEMPERATURE, newCityTemperature);

        boolean value1;
        value1 = intent.getBooleanExtra(CitiesConst.PRESSURE_SHOW, true); //if it's a string you stored.
        showField(value1, newPressure);
        showField(value1, newPressureName);

        value1 = intent.getBooleanExtra(CitiesConst.WIND_SHOW,true); //if it's a string you stored.
        showField(value1, newWind);
        showField(value1, newWindName);

        cityDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = WIKI_ROOT+newCityName.getText().toString();
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(browser);
            }
        });

        cityWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = null;
                try {
                    query = URLEncoder.encode(newCityName.getText().toString()+" "+ HOT_WORD, "utf-8");
                    String url = WEATHER_ROOT + query;
                    Intent browser = new Intent(Intent.ACTION_VIEW);
                    browser.setData(Uri.parse(url));
                    startActivity(browser);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
