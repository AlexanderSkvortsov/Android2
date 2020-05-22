package ru.geekbrains.lesson1.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import ru.geekbrains.lesson1.MainActivity;
import ru.geekbrains.lesson1.net.WeatherWebDetails;
import ru.geekbrains.lesson1.util.CitiesConst;
import ru.geekbrains.lesson1.util.OnSwipeTouchListener;
import ru.geekbrains.lesson1.util.Parcel;
import ru.geekbrains.lesson1.util.ParcelCitylDetails;
import ru.geekbrains.lesson1.R;

import static ru.geekbrains.lesson1.fragment.CoatOfWeekTemperatureFragment.PARCEL;

// Фрагмент выбора города из списка

public class CityFragment extends Fragment implements CitiesConst {

    private boolean isExistCoatOfArms;  // Можно ли расположить рядом фрагмент с температурой


    //+ Меняем текущую позицию на объект Parcel
    private Parcel currentParcel;       // Текущая посылка (номер города и название)

    private TextView newCityName;
    private TextView newCityTemperature;
    private TextView newPressure;
    private TextView newPressureName;
    private TextView newWind;
    private TextView newWindName;
    private FloatingActionButton cityDetails;

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
        cityWeather = layout.findViewById(R.id.btnCityWeather);
        backGround  = layout.findViewById(R.id.weatherView);
    }

    private void PullView(View layout)
    {
        fieldsInit(layout);
        btnsInit(layout);
    }

    public ParcelCitylDetails getParcelCity() {
       // return (ParcelCitylDetails) getActivity().getIntent().getExtras().getSerializable(CITY_PARCEL_DETAILS);
        return ((MainActivity) getActivity()).mainParcel;
    }

    private void fieldsInit(View layout) {
        // Получить посылку из параметра
        boolean val = parcelCity.isShowPressure();
        newPressure.setVisibility((val) ? View.VISIBLE : View.INVISIBLE);
        newPressureName.setVisibility((val) ? View.VISIBLE : View.INVISIBLE);

        val = parcelCity.isShowWind();
        newWind.setVisibility((val) ? View.VISIBLE : View.INVISIBLE);
        newWindName.setVisibility((val) ? View.VISIBLE : View.INVISIBLE);

        int cityWind = parcelCity.getWindOf1Day();
        newWind.setText(String.valueOf(cityWind ));

        newCityName.setText(parcelCity.getCityName());

        int cityTemperature = parcelCity.getTemperatureOf1Day();
        newCityTemperature.setText(String.valueOf(cityTemperature));
    }

    private void snackbarShow(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void btnsInit(final View layout) {
        cityDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = WIKI_ROOT + newCityName.getText().toString();
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browser);
                snackbarShow(layout,"CITY WIKI");
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
                    snackbarShow(layout,"CITY WEATHER");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        final String cityFinal = parcelCity.getCityName();
        final ArrayList<WeatherWebDetails> tempArray = parcelCity.getTemperatureOf5Days();

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

        // Определение, можно ли будет расположить рядом температуру в другом фрагменте
        isExistCoatOfArms = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

            if (savedInstanceState != null) {
            // Восстановление текущей позиции.
            currentParcel = (Parcel) savedInstanceState.getSerializable("CurrentCity");
        }
        else {
            //+ Если воcстановить не удалось, то сделаем объект с первым индексом
            currentParcel = new Parcel(parcelCity.getCityName(), parcelCity.getTemperatureOf5Days());
        }

        // Если можно нарисовать рядом температуру, то сделаем это
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

    // Показать температуру. Ecли возможно, то показать рядом со списком,
    // если нет, то открыть вторую activity
    private void showCoatOfWeekTemperature(Parcel parcel) {
        if (parcelCity.isNoWebData())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            String message = getResources().getString(R.string.warning_message)+" "+parcelCity.getCityName();
            builder.setTitle(message)
                    .setMessage(R.string.press_button)
                    //.setIcon(R.mipmap.ic_launcher_round)
                    .setIcon(R.drawable.pic8)
                    .setCancelable(false)
                    .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //                            Toast.makeText(getApplicationContext(), "Кнопка ок", Toast.LENGTH_SHORT).show();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return;
        }

        if (isExistCoatOfArms) {
            // Проверим, что фрагмент с температурой существует в activity
            CoatOfWeekTemperatureFragment detail = (CoatOfWeekTemperatureFragment)
            getFragmentManager().findFragmentById(R.id.coat_of_week_temperature);
            // Если есть необходимость, то выведем температуру
            //+ Здесь также применяем Parcel

            if (detail == null || detail.getParcel().getCityName() != parcel.getCityName()) {
                // Создаем новый фрагмент с текущей позицией для вывода температуры
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


