package ru.geekbrains.lesson1.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.geekbrains.lesson1.R;

public class CityWeatherAdapter extends RecyclerView.Adapter<CityWeatherAdapter.ViewHolder> {

    private final CityDataSource dataSource;

    // Передаем в конструктор источник данных
    // В нашем случае это массив, но может быть и запросом к БД
    public CityWeatherAdapter(CityDataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Создать новый элемент пользовательского интерфейса
    @NonNull
    @Override
    public CityWeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        // Создаем новый элемент пользовательского интерфейса
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);

        // Здесь можно установить требуемые параметры
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    // Заменить данные в пользовательском интерфейсе
    @Override
    public void onBindViewHolder(@NonNull CityWeatherAdapter.ViewHolder viewHolder, int position) {
        // Получить элемент из источника данных (БД, интернет...)
        // Вывести на экран используя ViewHolder
        Wed soc = dataSource.getSoc(position);
        viewHolder.setData(soc.getDescription(), soc.getPicture(), soc.getTemperature());
    }

    // Вернуть размер данных
    @Override
    public int getItemCount() {
        return dataSource.size();
    }


    // Этот класс хранит связь между данными и элементами View
    // Сложные данные могут потребовать несколько View на один пункт списка
    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView description;
        private final ImageView image;
        private final TextView temperature;
        private  final String celsius;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            celsius=itemView.getResources().getString(R.string.txtCelsius);
            description = itemView.findViewById(R.id.itemCityName);
            image = itemView.findViewById(R.id.itemCityImage);
            temperature = itemView.findViewById(R.id.itemCityTemperature);
        }

        public void setData(String description, int picture, int temperature) {
            getTemperature().setText(String.valueOf(temperature+celsius));
            getImage().setImageResource(picture);
            getDescription().setText(description);
        }

        public TextView getTemperature() {
            return temperature;
        }

        public TextView getDescription() {
            return description;
        }

        public ImageView getImage() {
            return image;
        }
    }
}
