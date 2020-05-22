package ru.geekbrains.lesson1.util;

import java.util.ArrayList;

import ru.geekbrains.lesson1.net.WeatherWebDetails;

public class ParcelCitylDetails extends Parcel {
    private final boolean showWind;
    private final boolean showPressure;
    private final boolean darkTheme;
    private final boolean noWebData;


    public ParcelCitylDetails(String cityName, ArrayList<WeatherWebDetails> weather5days, boolean showWind, boolean showPressure, boolean darkTheme, boolean noWebData) {
        super(cityName, weather5days);
        this.showWind = showWind;
        this.showPressure = showPressure;
        this.darkTheme = darkTheme;
        this.noWebData=noWebData;
    }

    public boolean isDarkTheme() {
        return darkTheme;
    }

    public boolean isShowWind() {
        return showWind;
    }

    public boolean isShowPressure() {
        return showPressure;
    }

    public boolean isNoWebData() {
        return noWebData;
    }
}
