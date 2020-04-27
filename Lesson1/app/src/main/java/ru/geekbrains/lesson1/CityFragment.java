package ru.geekbrains.lesson1;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

import static ru.geekbrains.lesson1.CoatOfWeekTemperatureFragment.PARCEL;

// Фрагмент выбора города из списка
public class CityFragment extends Fragment implements CitiesConst {

    private boolean isExistCoatOfArms;  // Можно ли расположить рядом фрагмент с температурой
    public static final String CITY_PARCEL_DETAILS = "city_parcel_details";

    //+ Меняем текущую позицию на объект Parcel
    private Parcel currentParcel;       // Текущая посылка (номер города и название)

    private TextView newCityName;
    private TextView newCityTemperature;
    private TextView newPressure;
    private TextView newPressureName;
    private TextView newWind;
    private TextView newWindName;
    private FloatingActionButton cityDetails;
    private FloatingActionButton backToMain;
    private FloatingActionButton cityWeather;
    private ParcelCitylDetails parcelCity;
    private ImageView backGround;

     // При создании фрагмента укажем его макет
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_city, container, false);
        parcelCity = getParcelCity();
        InitView(layout);
        PullView(layout);

        return layout;
    }

    private void InitView(View layout)
    {
        newCityName = layout.findViewById(R.id.cityName);
        newPressure = layout.findViewById(R.id.cityPressure);;
        newPressureName = layout.findViewById(R.id.txtPressureName);;
        newWind = layout.findViewById(R.id.cityWind);;
        newWindName = layout.findViewById(R.id.txtWindName);;
        newCityTemperature = layout.findViewById(R.id.cityTemperature);
        cityDetails= layout.findViewById(R.id.btnCityDetails);
        backToMain= layout.findViewById(R.id.btnBackToMain);
        cityWeather = layout.findViewById(R.id.btnCityWeather);
        backGround  = layout.findViewById(R.id.weatherView);
    }

    private void PullView(View layout)
    {
        fieldsInit(layout);
        btnsInit(layout);
    }

    public ParcelCitylDetails getParcelCity() {
    //    return (ParcelCitylDetails) getArguments().getSerializable(CITY_PARCEL_DETAILS);
        return (ParcelCitylDetails) getActivity().getIntent().getExtras().getSerializable(CITY_PARCEL_DETAILS);
    }

    private void fieldsInit(View layout) {
        // Получить посылку из параметра


        boolean val = parcelCity.isShowPressure();
        newPressure.setVisibility((val) ? View.VISIBLE : View.INVISIBLE);
        newPressureName.setVisibility((val) ? View.VISIBLE : View.INVISIBLE);

        val = parcelCity.isShowWind();
        newWind.setVisibility((val) ? View.VISIBLE : View.INVISIBLE);
        newWindName.setVisibility((val) ? View.VISIBLE : View.INVISIBLE);

        newCityName.setText(parcelCity.getCityName());

        Calendar calendar = Calendar.getInstance();
        int weekday = calendar.get(Calendar.DAY_OF_WEEK); // 1 is Sunday
        weekday = (weekday == 1)?7:weekday-1; // normalize for Russian

        int[] cityTemperature = parcelCity.getTemperatureOfWeek();
        int  cTeperature = cityTemperature [weekday-1] %100;
        newCityTemperature.setText(String.valueOf(cTeperature));
    }

    private void btnsInit(View layout) {
        cityDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = WIKI_ROOT + newCityName.getText().toString();
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browser);
            }
        });

        cityWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = null;
                try {
                    query = URLEncoder.encode(newCityName.getText().toString() + " " + HOT_WORD, "utf-8");
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
                getActivity().finish();
            }
        });


        final String cityFinal = parcelCity.getCityName();
        final int[] tempArray = parcelCity.getTemperatureOfWeek();

        newCityName .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //+ Теперь опираемся на Parcel, а не на текущую позицию
                currentParcel = new Parcel(cityFinal,tempArray);
                showCoatOfWeekTemperature(currentParcel);
            }
        });

        backGround.setOnTouchListener(new OnSwipeTouchListener(getActivity().getApplicationContext()) {

            public void onSwipeLeft() {
                currentParcel = new Parcel(cityFinal,tempArray);
                showCoatOfWeekTemperature(currentParcel);
            }

            public void onSwipeRight() {
                getActivity().finish();
            }

        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // activity создана, можно к ней обращаться. Выполним начальные действия
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Определение, можно ли будет расположить рядом герб в другом фрагменте
        isExistCoatOfArms = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

            if (savedInstanceState != null) {
            // Восстановление текущей позиции.
            currentParcel = (Parcel) savedInstanceState.getSerializable("CurrentCity");
        }
        else {
            //+ Если воcстановить не удалось, то сделаем объект с первым индексом
            currentParcel = new Parcel(parcelCity.getCityName(), parcelCity.getTemperatureOfWeek());
        }

        // Если можно нарисовать рядом герб, то сделаем это
        if (isExistCoatOfArms)
            showCoatOfWeekTemperature(currentParcel);
    }

    // Сохраним текущую позицию (вызывается перед выходом из фрагмента)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //+ Также меняем текущую позицию на Parcel
        outState.putSerializable("CurrentCity", currentParcel);
        super.onSaveInstanceState(outState);
    }

    // Показать герб. Ecли возможно, то показать рядом со списком,
    // если нет, то открыть вторую activity
    private void showCoatOfWeekTemperature(Parcel parcel) {
        if (isExistCoatOfArms) {
            // Проверим, что фрагмент с гербом существует в activity
            CoatOfWeekTemperatureFragment detail = (CoatOfWeekTemperatureFragment)
            getFragmentManager().findFragmentById(R.id.coat_of_week_temperature);
            // Если есть необходимость, то выведем герб
            //+ Здесь также применяем Parcel

            if (detail == null || detail.getParcel().getCityName() != parcel.getCityName()) {
                // Создаем новый фрагмент с текущей позицией для вывода герба
                detail = CoatOfWeekTemperatureFragment.create(parcel);

                // Выполняем транзакцию по замене фрагмента
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.coat_of_week_temperature, detail);  // замена фрагмента
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } else {
            // Если нельзя вывести  рядом, откроем вторую activity
            Intent intent = new Intent();
            intent.setClass(getActivity(), CoatOfWeekTemperatureActivity.class);
            //+ и передадим туда Parcel
            intent.putExtra(PARCEL, parcel);
            startActivity(intent);
        }
    }
}


