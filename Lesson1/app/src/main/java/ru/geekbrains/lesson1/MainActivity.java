package ru.geekbrains.lesson1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import ru.geekbrains.lesson1.net.WeatherRequest5Days;
import ru.geekbrains.lesson1.net.WeatherThread;
import ru.geekbrains.lesson1.pool.WeatherCityPool;
import ru.geekbrains.lesson1.pool.WeatherListActivity;
import ru.geekbrains.lesson1.util.CitiesConst;
import ru.geekbrains.lesson1.util.CitiesPool;
import ru.geekbrains.lesson1.util.ParcelCitylDetails;
import ru.geekbrains.lesson1.util.SingleCitiesPresenter;

public class MainActivity extends AppCompatActivity implements CitiesConst , NavigationView.OnNavigationItemSelectedListener{

        public ParcelCitylDetails mainParcel;
        private CitiesPool citiesPool = null;
        private boolean isDark;
        private boolean isWind;
        private boolean isPressure;
        private String mainCity;
        WeatherThread weatherThread;
        private AppBarConfiguration mAppBarConfiguration;
        private Switch swDarkSetting;
        private MainActivity mainActivity;
        private WeatherCityPool weatherCityPool;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mainActivity = this;

            SingleCitiesPresenter singleCitiesPresenter = SingleCitiesPresenter.getInstance();


            InitParcelCity();

            if (weatherCityPool == null )  weatherCityPool = new WeatherCityPool();

            getActualWeb(mainCity);
            if ((weatherThread.getRequestResult() == null) )
            {
                mainCity = "Москва";
                getActualWeb(mainCity);
            }

            // restore or create
            if (savedInstanceState == null) {
                InitPool();
            }
            else {
                citiesPool=(CitiesPool )savedInstanceState.getSerializable("CityPool");
            }

            mainParcel = createParcelCity();

            if (savedInstanceState == null) {
                singleCitiesPresenter.setDarkTheme(isDark);
            }
            else
                isDark = singleCitiesPresenter.isDarkTheme();

            if (isDark) {
                setTheme(R.style.AppDarkTheme);
            } else {
                setTheme(R.style.AppTheme);
            }

            setContentView(R.layout.activity_main);
            initDrawer();

        }

    private void initDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener( this);

        swDarkSetting = (Switch)navigationView.getMenu().findItem(R.id.nav_switch).getActionView().findViewById(R.id.switchTheme);
        swDarkSetting.setChecked(isDark);

        swDarkSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                SingleCitiesPresenter singleCitiesPresenter = SingleCitiesPresenter.getInstance();
                singleCitiesPresenter.setDarkTheme(isChecked);
                mainActivity.recreate();
            }
        });
    }

    private boolean getActualWeb(String cityIs ){
            weatherThread = new WeatherThread(cityIs);
            Thread childWeatherThread = new Thread(weatherThread);
            childWeatherThread.start();
            Log.e(GEEK_WEATHER, "Thread start");
            try {
                Thread.sleep(1000);
                childWeatherThread.join();
                Log.e(GEEK_WEATHER, "Thread join");

                return  weatherThread.getRequestResult() != null;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }

    private void InitParcelCity() {
            SharedPreferences sharedPref = getSharedPreferences(GEEK_WEATHER, MODE_PRIVATE);

            isDark= sharedPref.getBoolean(IS_DARK_THEME, true);
            isWind= sharedPref.getBoolean(IS_WIND, true);
            isPressure= sharedPref.getBoolean(IS_PRESSUE, true);
            mainCity= sharedPref.getString(MAIN_CITY, "Москва");

            Gson gson = new Gson();
            String json =  sharedPref.getString(CITY_WEATHER_LIST, "");
            weatherCityPool = gson.fromJson(json, WeatherCityPool.class);
        }

    private  ParcelCitylDetails createParcelCity() {
        return new ParcelCitylDetails(mainCity, citiesPool.getWeather5DaysDetails(mainCity), isWind, isPressure,isDark);
    }

    private void InitPool() {

        if (citiesPool == null) {

            if (weatherThread.getRequestResult() == null) {
                Toast.makeText(getApplicationContext(), "Нет данных!", Toast.LENGTH_SHORT).show();
                Log.e(GEEK_WEATHER,"No data from web!");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
            else
            {
                // get the weather from web
                WeatherRequest5Days weatherRequest5Days =  weatherThread.getRequestResult();
                citiesPool = new CitiesPool(mainCity, weatherRequest5Days );
            }
        }
    }

    public void saveParcelCity(){
        SharedPreferences sharedPref = getSharedPreferences(GEEK_WEATHER, MODE_PRIVATE);
        // Настройки сохраняются посредством специального класса editor.
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(IS_DARK_THEME, isDark);
        editor.putBoolean(IS_WIND, isWind);
        editor.putBoolean(IS_PRESSUE, isPressure);
        editor.putString(MAIN_CITY, mainCity);
        editor.apply();

        Gson gson = new Gson();
        String json = gson.toJson(weatherCityPool);
        editor.putString(CITY_WEATHER_LIST, json);
        editor.commit();

    }

    @Override
    protected void onStop() {
        super.onStop();
        showToast("onStop");
        saveParcelCity();
    }

    protected void updateFragment(boolean updateWind, boolean updatePressure, String updateCity){

        if ((updateWind != isWind) ||
            (updateCity != mainCity)||
            (updatePressure != isPressure))
        {
            mainCity = updateCity;
            isWind = updateWind;
            isPressure = updatePressure;

            if (!citiesPool.checkCity(mainCity )){
//                if (getActualWeb(mainCity )){
                    citiesPool.addCity(mainCity,weatherThread.getRequestResult());
                    saveParcelCity();
                    weatherCityPool.setCityWeather(mainParcel.getCityName(), mainParcel.getTemperatureOf1Day());
                    this.recreate();
            }
         }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       switch (requestCode) {
           case RESULT_FROM_SETTINGS:
               if (resultCode == RESULT_OK) {
                   boolean isWind = data.getBooleanExtra(IS_WIND, false);
                   boolean isPressure = data.getBooleanExtra(IS_PRESSUE, false);
                   String mainCity = data.getStringExtra(MAIN_CITY);
                   updateFragment(isWind, isPressure, mainCity);
               }
               break;
           default:
               super.onActivityResult(requestCode, resultCode, data);
       }
    }
    
    // Сохраним текущую позицию (вызывается перед выходом из фрагмента)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //+ Также меняем текущую позицию на Parcel
        outState.putSerializable("CityPool", citiesPool);
        super.onSaveInstanceState(outState);
    }

    private void showToast(String message)
    {
        Log.d(GEEK_WEATHER,message);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_selectCity) {

                Intent intent = new Intent();
                intent.setClass(this, CitiesActivity.class);

                intent.putExtra(IS_WIND, isWind);
                intent.putExtra(IS_PRESSUE, isPressure);
                intent.putExtra(MAIN_CITY, mainCity);

                startActivityForResult(intent, RESULT_FROM_SETTINGS);
            }

        if (id == R.id.nav_showCitiesWeather) {
            Intent intent = new Intent();
            intent.setClass(this, WeatherListActivity.class);

            SingleCitiesPresenter singleCitiesPresenter = SingleCitiesPresenter.getInstance();
            singleCitiesPresenter.setWeatherCityPool(weatherCityPool);

            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        return true;
    }


}
