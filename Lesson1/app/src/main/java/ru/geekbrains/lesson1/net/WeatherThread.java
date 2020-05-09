package ru.geekbrains.lesson1.net;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import com.google.gson.Gson;

import static ru.geekbrains.lesson1.util.CitiesConst.GEEK_WEATHER;

public class WeatherThread implements Runnable {
    private static final String TAG = "WEATHER";
   // private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?lat=55.75&lon=37.62&appid=";
    //private static final String WEATHER_URL_PREFIX = "https://api.openweathermap.org/data/2.5/weather?q=";
    //private static final String WEATHER_URL_SUFFICS = ",RU&appid=";

    private static final String WEATHER_URL_PREFIX ="https://api.openweathermap.org/data/2.5/forecast?q=";
    private static final String WEATHER_URL_SUFFICS = ",RU&appid=";

    private static final String WEATHER_API_KEY = "36a6ee9abf5b6edad46ef3edc95b5d55";
    private String cityName;
    private WeatherRequest5Days requestResult;

    public WeatherRequest5Days getRequestResult() {
        return requestResult;
    }

    public WeatherThread(String cityName) {
        this.cityName = cityName;
    }

    private String getLines(BufferedReader in) throws IOException {
//        return in.lines().collect(Collectors.joining("\n"));
        // for all API
        String response = new String();
        for (String line; (line = in.readLine()) != null; response += line);
        return  response;
    }

    @Override
    public void run() {
        try {
                final URL uri  = new URL(WEATHER_URL_PREFIX+cityName+WEATHER_URL_SUFFICS + WEATHER_API_KEY);
                requestResult = null;

                Log.e(GEEK_WEATHER, "Start connection");

                HttpsURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpsURLConnection) uri.openConnection();
                    urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
                    urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
                    String result = getLines(in);
                    // преобразование данных запроса в модель
                    Gson gson = new Gson();
                    requestResult =  gson.fromJson(result, WeatherRequest5Days.class);
                    in.close();

                    Log.e(GEEK_WEATHER, "Connection done");

                    // congratulations, here we get the city weather details

                } catch (Exception e) {
                    Log.e(GEEK_WEATHER, "Fail connection", e);
                    e.printStackTrace();
                } finally {
                    if (null != urlConnection) {
                        urlConnection.disconnect();
                    }
                }
        } catch (MalformedURLException e) {
        Log.e(TAG, "Fail URI", e);
        e.printStackTrace();
        }
    }
}


