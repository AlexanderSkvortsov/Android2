package ru.geekbrains.lesson1;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ru.geekbrains.lesson1.fragment.CityFragment;
import ru.geekbrains.lesson1.fragment.CoatOfWeekTemperatureFragment;
import ru.geekbrains.lesson1.util.App;
import ru.geekbrains.lesson1.util.CitiesConst;
import ru.geekbrains.lesson1.util.CitiesPool;
import ru.geekbrains.lesson1.util.ParcelCitylDetails;
import ru.geekbrains.lesson1.util.SingleCitiesPresenter;


public class MainActivity extends AppCompatActivity implements CitiesConst {

        public ParcelCitylDetails mainParcel;
        private CitiesPool citiesPool = null;
        private  MenuItem isDarkTheme;
        private boolean isDark;
        private boolean isWind;
        private boolean isPressure;
        private String mainCity;
        SingleCitiesPresenter singleCitiesPresenter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            SingleCitiesPresenter singleCitiesPresenter  = SingleCitiesPresenter.getInstance();

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


        }

    public ParcelCitylDetails createParcelCity() {

        SharedPreferences sharedPref = getSharedPreferences(GEEK_WEATHER, MODE_PRIVATE);

        isDark= sharedPref.getBoolean(IS_DARK_THEME, true);
        isWind= sharedPref.getBoolean(IS_WIND, true);
        isPressure= sharedPref.getBoolean(IS_PRESSUE, true);
        mainCity= sharedPref.getString(MAIN_CITY, citiesPool.getCity(0));

        return new ParcelCitylDetails(mainCity, citiesPool.getTemperatureOfWeekAsArray(mainCity), isWind, isPressure,isDark);
    }

    private void InitPool() {

        if (citiesPool == null) {
            citiesPool = new CitiesPool(getResources().getStringArray(R.array.city_names));
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        showToast("onStop");
        saveParcelCity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.darkThemeSettings).setChecked(isDark);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        super.onOptionsItemSelected(item);

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cityVewSettings) {
            //Toast.makeText(getApplicationContext(),"item2",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(this, CitiesActivity.class);

            intent.putExtra(IS_WIND, isWind);
            intent.putExtra(IS_PRESSUE, isPressure);
            intent.putExtra(MAIN_CITY, mainCity);

            startActivityForResult(intent, RESULT_FROM_SETTINGS);
            return true;
        }

            //noinspection SimplifiableIfStatement
        if (id == R.id.darkThemeSettings) {
                SingleCitiesPresenter singleCitiesPresenter = SingleCitiesPresenter.getInstance();

                item.setChecked(!item.isChecked());
                singleCitiesPresenter.setDarkTheme(item.isChecked());
                this.recreate();
                return true;
        }

        return true;
    }

    protected void updateFragment(boolean updateWind, boolean updatePressure, String updateCity){

        if ((updateWind != isWind) ||
            (updateCity != mainCity)||
            (updatePressure != isPressure)
            )
        {
            mainCity = updateCity;
            isWind = updateWind;
            isPressure = updatePressure;

            if (citiesPool.checkCity(mainCity )){
                citiesPool.addCity(mainCity);
            }

            this.recreate();

            //CityFragment cityFragment = (CityFragment) getSupportFragmentManager().findFragmentById(R.id.cities);
            //cityFragment.getActivity().recreate();
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
        //       Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        Log.d(GEEK_WEATHER,message);
    }


}
