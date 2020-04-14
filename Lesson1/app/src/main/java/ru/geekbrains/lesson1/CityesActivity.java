package ru.geekbrains.lesson1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class CityesActivity extends AppCompatActivity {

    TextView cityName;
    ListView cityNames;
    EditText selectedCity;
    Button buttonAction;
    Switch swPressure;
    Switch swWind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cityes);

        InitView();
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

    private void PullView() {
        String[] cityValues = getResources().getStringArray(R.array.city_names);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, cityValues);

        cityNames.setAdapter(adapter);

        cityNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                ShowSelectedCity(text);
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

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("CITY_FROM_LIST", CityName);
        intent.putExtra("PRESSURE_SHOW", swPressure.isChecked());
        intent.putExtra("WIND_SHOW", swWind.isChecked());

        startActivity(intent);
    }

}
