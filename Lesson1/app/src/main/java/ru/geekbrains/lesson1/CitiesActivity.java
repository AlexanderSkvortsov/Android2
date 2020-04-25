package ru.geekbrains.lesson1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import ru.geekbrains.lesson1.util.CitiesConst;
import ru.geekbrains.lesson1.util.CitiesPool;
import ru.geekbrains.lesson1.util.ParcelCitylDetails;

import static ru.geekbrains.lesson1.fragment.CityFragment.CITY_PARCEL_DETAILS;


public class CitiesActivity extends AppCompatActivity {

    public static final String GEEK_WEATHER = "GEEK_WEATHER";

    TextView cityName;
    ListView cityNames;
    EditText selectedCity;
    Button buttonAction;
    Switch swPressure;
    Switch swWind;

    private String selected_City = "Moskow";
    private CitiesPool citiesPool = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        showToast("Application " + ((savedInstanceState == null) ? "First Start" : "Next Start"));

        InitView();
        InitListView();
        PullView();
    }

    private void InitView()
    {
        cityName = findViewById(R.id.city_name);
        cityNames = findViewById(R.id.city_list);
        selectedCity = findViewById(R.id.editText);
        buttonAction = findViewById(R.id.selectCityButton);

        swPressure = findViewById(R.id.chkPressure);
        swWind = findViewById(R.id.chkWind);
    }

    private void InitListView() {

        if (citiesPool == null) {
            citiesPool = new CitiesPool(getResources().getStringArray(R.array.city_names));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, citiesPool.getCities());

        cityNames.setAdapter(adapter);
    }

    private void PullView() {
        //String[] cityValues = getResources().getStringArray(R.array.city_names);


        cityNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                ShowSelectedCity(text);
                selectedCity.setText(text);
            }
        });

        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowSelectedCity(selectedCity.getText().toString());
            }
        });
    }

    private void ShowSelectedCity(String CityName)
    {
       //Toast.makeText(this, CityName, Toast.LENGTH_SHORT).show();
        if (!citiesPool.checkCity(CityName))
        {
            citiesPool.addCity(CityName);
            InitListView();
        }

        Intent intent = new Intent(this, MainActivity.class);
        ParcelCitylDetails parcelCitylDetails = new ParcelCitylDetails(CityName,
                                                                       citiesPool.getTemperatureOfWeekAsArray(CityName),
                                                                       swWind.isChecked(),
                                                                       swPressure.isChecked());

        intent.putExtra(CITY_PARCEL_DETAILS,parcelCitylDetails);
        selected_City = CityName;

        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showToast("onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        showToast("onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        showToast("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showToast("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        showToast("onPause");

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        showToast("onSaveInstanceState");
        outState.putBoolean(CitiesConst.WIND_SHOW,swWind.isChecked());
        outState.putBoolean(CitiesConst.PRESSURE_SHOW,swPressure.isChecked());
        outState.putString(CitiesConst.CITY_FROM_LIST, selected_City );
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        showToast("onRestoreInstanceState");
        if (savedInstanceState !=null) {
            swWind.setChecked(savedInstanceState.getBoolean(CitiesConst.WIND_SHOW));
            swPressure.setChecked(savedInstanceState.getBoolean(CitiesConst.PRESSURE_SHOW));
            selected_City = savedInstanceState.getString(CitiesConst.CITY_FROM_LIST);
            selectedCity.setText(selected_City);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showToast("onDestroy");
    }

    private void showToast(String message)
    {
 //       Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        Log.d(GEEK_WEATHER,message);
    }
}
