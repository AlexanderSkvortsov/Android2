package ru.geekbrains.lesson1.fragment;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ru.geekbrains.lesson1.MainActivity;
import ru.geekbrains.lesson1.recycler.CityDataSource;
import ru.geekbrains.lesson1.recycler.CitySource;
import ru.geekbrains.lesson1.recycler.CitySourceBuilder;
import ru.geekbrains.lesson1.recycler.CityWeatherAdapter;
import ru.geekbrains.lesson1.util.Parcel;
import ru.geekbrains.lesson1.R;

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

        View layout = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        initDataSource(layout);

        return layout;
    }

    // Выделяем инициализацию источника данных
    private void initDataSource(View layout) {
        // создаем источник данных
        CityDataSource sourceData = new CitySourceBuilder()
                .setResources(getResources(),getParcel())
                .build();

        final CityWeatherAdapter adapter = initRecyclerView(layout,sourceData);
    }

    private CityWeatherAdapter initRecyclerView(View layout,CityDataSource sourceData) {
        RecyclerView recyclerView = layout.findViewById(R.id.recycler_view);

        // Эта установка служит для повышения производительности системы
        recyclerView.setHasFixedSize(true);

        // Будем работать со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(layout.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Установим анимацию - а чтобы было хорошо заметно, сделаем анимацию долгой
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(500);
        animator.setRemoveDuration(500);
        recyclerView.setItemAnimator(animator);

        // Установим адаптер
        CityWeatherAdapter adapter = new CityWeatherAdapter(sourceData);
        recyclerView.setAdapter(adapter);

        return adapter;
    }
}
