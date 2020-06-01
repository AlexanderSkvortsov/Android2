package ru.geekbrains.lesson1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.geekbrains.lesson1.fragment.CityFragment;
import ru.geekbrains.lesson1.fragment.CoatOfWeekTemperatureFragment;
import ru.geekbrains.lesson1.net.WeatherRequest5Days;
import ru.geekbrains.lesson1.net.WeatherThread;
import ru.geekbrains.lesson1.pool.WeatherCityPool;
import ru.geekbrains.lesson1.pool.WeatherListActivity;
import ru.geekbrains.lesson1.retrofit2.IOpenWeather;
import ru.geekbrains.lesson1.util.App;
import ru.geekbrains.lesson1.util.CitiesConst;
import ru.geekbrains.lesson1.util.CitiesPool;
import ru.geekbrains.lesson1.util.ParcelCitylDetails;
import ru.geekbrains.lesson1.util.SingleCitiesPresenter;

public class MainActivity extends AppCompatActivity implements CitiesConst , NavigationView.OnNavigationItemSelectedListener{

        public ParcelCitylDetails mainParcel = null;
        private CitiesPool citiesPool = null;
        private boolean isDark;
        private boolean isWind;
        private boolean isPressure;
        private String mainCity;
        //WeatherThread weatherThread;
        private AppBarConfiguration mAppBarConfiguration;
        private Switch swDarkSetting;
        private MainActivity mainActivity;
        private WeatherCityPool weatherCityPool;
        private boolean noDataFromWeb;

        // retrofit variable
        private SharedPreferences sharedPref;
        private IOpenWeather openWeather;
        private String privateApiKey;
        WeatherRequest5Days weatherRequest5Days = null;
        private ProgressDialog mProgressDialog;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mainActivity = this;

            SingleCitiesPresenter singleCitiesPresenter = SingleCitiesPresenter.getInstance();

            initRetrofit();
            initPreferences();

            InitParcelCity();

            if (weatherCityPool == null )  weatherCityPool = new WeatherCityPool();

            requestRetrofit(mainCity,privateApiKey);

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

/*
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
*/

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
        return new ParcelCitylDetails(mainCity, citiesPool.getWeather5DaysDetails(mainCity), isWind, isPressure,isDark, noDataFromWeb);
    }

    private void InitPool() {

        if (citiesPool == null) {

            citiesPool = new CitiesPool(mainCity, weatherRequest5Days );

            noDataFromWeb = (weatherRequest5Days == null);
/*
            if (noDataFromWeb) {
                Toast.makeText(getApplicationContext(), "Нет данных!", Toast.LENGTH_LONG).show();
                Log.e(GEEK_WEATHER, "No data from web!");
            }

 */
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
                    citiesPool.addCity(mainCity,weatherRequest5Days);
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
        savePreferences();
    }

    private void showToast(String message)
    {
        Log.e(GEEK_WEATHER,message);
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

    // *************** retrofit **********************
    private void initRetrofit() {
        Retrofit retrofit = App.getRetrofitInstance();
        openWeather = retrofit.create(IOpenWeather.class);
    }

    private void initPreferences() {
        sharedPref = getPreferences(MODE_PRIVATE);
        loadPreferences();                   // Загружаем настройки
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("apiKey", privateApiKey);
        editor.apply();
    }

    // Загружаем настройки
    private void loadPreferences() {
        privateApiKey = sharedPref.getString("apiKey", "36a6ee9abf5b6edad46ef3edc95b5dc0");
    }

    private void requestRetrofit(String city, String keyApi) {

        mProgressDialog = new ProgressDialog(this,R.style.MyTheme);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        mProgressDialog.show();

        openWeather.loadWeather(city+",RU",  keyApi)
                .enqueue(new Callback<WeatherRequest5Days>() {
                    @Override
                    public void onResponse(Call<WeatherRequest5Days> call, Response<WeatherRequest5Days> response) {

                        mProgressDialog.dismiss();
                        // if successfully
                        if (response.body() != null && response.isSuccessful()) {
                            noDataFromWeb=false;
                            weatherRequest5Days = response.body();
                            citiesPool = new CitiesPool(mainCity, weatherRequest5Days );
                            mainParcel = createParcelCity();
                            weatherCityPool.setCityWeather(mainParcel.getCityName(), mainParcel.getTemperatureOf1Day());

                            Fragment frg = null;
                            frg = getSupportFragmentManager().findFragmentByTag("MainCityFragment");

                            if (frg != null)
                                ((CityFragment)frg ).updateDataFromParcel();

                            /* NOT WORKING !!!, lost more than 4 hovers for understanding why.
                            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.detach(frg);
                            ft.attach(frg);
                            ft.commit();
                            */

                        }

                        // if bad result
                        if (!response.isSuccessful() && response.errorBody() != null){
                            try {
                                JSONObject jsonError = new JSONObject(response.errorBody().string());
                                String error = jsonError.getString("message");
                                showToast(error);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    // totally fail
                    public void onFailure(Call<WeatherRequest5Days> call, Throwable t) {
                        showToast("Internet Error!");
                        mProgressDialog.dismiss();

                    }
                });
    }

}
