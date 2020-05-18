package ru.geekbrains.lesson1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.nio.channels.ScatteringByteChannel;
import java.util.Locale;
import java.util.regex.Pattern;

import ru.geekbrains.lesson1.util.CitiesConst;
import ru.geekbrains.lesson1.util.CitiesPool;
import ru.geekbrains.lesson1.util.ParcelCitylDetails;
import ru.geekbrains.lesson1.util.SingleCitiesPresenter;

import static ru.geekbrains.lesson1.fragment.CityFragment.CITY_PARCEL_DETAILS;


public class CitiesActivity extends AppCompatActivity implements CitiesConst{

    Pattern checkLoginRU ;
    ListView cityNames;

    TextInputEditText selectedCity;
    Button buttonAction;
    Switch swPressure;
    Switch swWind;

    private String selected_City = "Moscow";
    private CitiesPool citiesPool = null;
    private SingleCitiesPresenter singleCitiesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SingleCitiesPresenter singleCitiesPresenter  = SingleCitiesPresenter.getInstance();

        if (singleCitiesPresenter.isDarkTheme()) {
            setTheme(R.style.AppDarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_cities);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.action_settings);

        showToast("Application " + ((savedInstanceState == null) ? "First Start" : "Next Start"));

        InitView();
        InitListView();
        PullView();
        InitTextView();
        initViewFromIntent();
    }

    public boolean  isLocaleRU() {
        String l = Locale.getDefault().getLanguage();
        return l.equals("ru");
    }

    // Показать ошибку
    private void showError(TextView view, String message) {
        view.setError(message);
    }

    // спрятать ошибку
    private void hideError(TextView view) {
        view.setError(null);
    }

    // Валидация
    private boolean validate(TextView tv, Pattern check, String message){
        String value = tv.getText().toString();
        if (check.matcher(value).matches()){    // Проверим на основе регулярных выражений
            hideError(tv);
            return true;
        }
        else{
            showError(tv, message);
            return false;
        }
    }

    private void InitTextView()
    {
        checkLoginRU=  isLocaleRU()?Pattern.compile("^[а-яА-яЁё]+(?:[\\s-][а-яА-яЁё]+)*$"): Pattern.compile("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$");;

        selectedCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // Как только фокус потерян, сразу проверяем на валидность данные
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                TextView tv = (TextView) v;
                // Это сама валидация, она вынесена в отдельный метод, чтобы не дублировать код см вызов ниже
                validate(tv, checkLoginRU , "Это не город!");
            }
        });
    }

    private void InitView()
    {
        cityNames = findViewById(R.id.city_list);
        selectedCity = findViewById(R.id.inputCityName);
        buttonAction = findViewById(R.id.selectCityButton);

        swPressure = findViewById(R.id.chkPressure);
        swWind = findViewById(R.id.chkWind);
    }

    private void InitListView() {

        singleCitiesPresenter = SingleCitiesPresenter.getInstance();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, singleCitiesPresenter.getCitiesList());

        cityNames.setAdapter(adapter);
        selectedCity.setText(adapter.getItem(0));

    }

    private void PullView() {
        //String[] cityValues = getResources().getStringArray(R.array.city_names);


        cityNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                selectedCity.setText(text);
            }
        });

        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cityNameValidatior()){
                    Intent intent = createIntentForResult();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
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

    private boolean cityNameValidatior(){
        // Это сама валидация, она вынесена в отдельный метод, чтобы не дублировать код см вызов ниже
        return validate(selectedCity, checkLoginRU, "Это не город!");
    }

    private Intent createIntentForResult(){
        // save after Back
        Intent intent = new Intent();
        intent.putExtra(IS_WIND,swWind.isChecked());
        intent.putExtra(IS_PRESSUE,swPressure.isChecked());
        intent.putExtra(MAIN_CITY,selectedCity.getText().toString());
        return  intent;
    }

    private void initViewFromIntent(){
        // save after Back
        Intent intent = getIntent();
        swWind.setChecked(intent.getBooleanExtra(IS_WIND,false));
        swPressure.setChecked(intent.getBooleanExtra(IS_PRESSUE, false));
        selectedCity.setText(intent.getStringExtra(MAIN_CITY));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if ( cityNameValidatior())
                {
                    setResult(RESULT_OK, createIntentForResult());
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
