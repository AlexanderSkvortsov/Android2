package ru.geekbrains.lesson1.retrofit2;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.geekbrains.lesson1.net.WeatherRequest5Days;

public interface IOpenWeather {
    @GET("forecast")
   // Call<WeatherRequest5Days> loadWeather(@Query("q") String cityCountry, @Query("units") String metric, @Query("appid") String keyApi);
    Call<WeatherRequest5Days> loadWeather(@Query("q") String cityCountry,  @Query("appid") String keyApi);
}