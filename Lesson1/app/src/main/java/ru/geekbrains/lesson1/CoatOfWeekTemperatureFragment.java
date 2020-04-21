package ru.geekbrains.lesson1;

import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.DayOfWeek;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CoatOfWeekTemperatureFragment extends Fragment {


    public static final String PARCEL = "parcel";

    // Фабричный метод создания фрагмента
    // Фрагменты рекомендуется создавать через фабричные методы.
    public static CoatOfWeekTemperatureFragment create(Parcel parcel) {
        CoatOfWeekTemperatureFragment fragment = new CoatOfWeekTemperatureFragment();    // создание

        // Передача параметра
        Bundle args = new Bundle();
        args.putSerializable(PARCEL, parcel);
        fragment.setArguments(args);
        return fragment;
    }

    // Получить посылку из параметра
    public Parcel getParcel() {
        return (Parcel) getArguments().getSerializable(PARCEL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_coat_of_week_temperature, container, false);

        // list of resources
        int [] weekTemperatureIDs = new int[7];
        weekTemperatureIDs[0] = R.id.fragMondayTemperature;
        weekTemperatureIDs[1] = R.id.fragTuesdayTemperature;
        weekTemperatureIDs[2] = R.id.fragWednesdayTemperature;
        weekTemperatureIDs[3] = R.id.fragThursdayTemperature;
        weekTemperatureIDs[4] = R.id.fragFridayTemperature;
        weekTemperatureIDs[5] = R.id.fragSaturdayTemperature;
        weekTemperatureIDs[6] = R.id.fragSundayTemperature;

        TextView cityNameView = layout.findViewById(R.id.fragCityName);

        Parcel parcel = getParcel();

        int[] weekOfTemperature = parcel.getTemperatureOfWeek();

        // fill from parcel
        TextView tempView;
        for(int i=0;i<7;i++)
        {
            tempView = layout.findViewById(weekTemperatureIDs[i]);
            tempView.setText(String.valueOf(weekOfTemperature[i]));
        }

        cityNameView.setText(parcel.getCityName());

        return layout;
    }
}
