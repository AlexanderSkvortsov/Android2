package ru.geekbrains.lesson1.fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import ru.geekbrains.lesson1.R;
import ru.geekbrains.lesson1.fragment.CoatOfWeekTemperatureFragment;
import ru.geekbrains.lesson1.util.SingleCitiesPresenter;

public class CoatOfWeekTemperatureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SingleCitiesPresenter singleCitiesPresenter  = SingleCitiesPresenter.getInstance();

        if (singleCitiesPresenter.isDarkTheme()) {
            setTheme(R.style.AppDarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_coat_of_week_temperature);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.weekWeatherTitle);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Если устройство перевернули в альбомную ориентацию, то надо эту activity закрыть
            finish();
            return;
        }

        if (savedInstanceState == null) {
            // Если эта activity запускается первый раз (с каждым новым гербом первый раз)
            // то перенаправим параметр фрагменту
            CoatOfWeekTemperatureFragment details = new CoatOfWeekTemperatureFragment();
            details.setArguments(getIntent().getExtras());

            // Добавим фрагмент в activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, details).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
