package ru.geekbrains.lesson1;

import android.content.res.TypedArray;
import android.graphics.Color;
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

        int [] weekImageIDs = new int[7];
        weekImageIDs[0] = R.id.fragMondayImage;
        weekImageIDs[1] = R.id.fragTuesdayImage;
        weekImageIDs[2] = R.id.fragWednesdayImage;
        weekImageIDs[3] = R.id.fragThursdayImage;
        weekImageIDs[4] = R.id.fragFridayImage;
        weekImageIDs[5] = R.id.fragSaturdayImage;
        weekImageIDs[6] = R.id.fragSundayImage;
        
        Parcel parcel = getParcel();

        TextView cityNameView = layout.findViewById(R.id.fragCityName);
        cityNameView.setText(parcel.getCityName());

        int[] weekOfTemperature = parcel.getTemperatureOfWeek();
        String cels = getActivity().getResources().getString(R.string.txtCelsius);

        TypedArray weatherStateImages = getResources().obtainTypedArray(R.array.weather_state_imgs);

        // fill from parcel
        TextView tempTextView;
        ImageView tempImageView;
        for(int i=0;i<7;i++)
        {
            tempTextView = layout.findViewById(weekTemperatureIDs[i]);
            int j= weekOfTemperature[i]%100;
            tempTextView.setText(String.valueOf(j)+cels);
            tempTextView.setTextColor((j<5)? Color.BLUE:(j>25)?Color.RED:Color.GREEN);

            tempImageView = layout.findViewById(weekImageIDs[i]);;
            int weatherStateIndex = +Math.abs(weekOfTemperature[i]/100);
            tempImageView.setImageResource(weatherStateImages.getResourceId(weatherStateIndex-1, -1));

        }
        weatherStateImages.recycle();


        return layout;
    }
}
