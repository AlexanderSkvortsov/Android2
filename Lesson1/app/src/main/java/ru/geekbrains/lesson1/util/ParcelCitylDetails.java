package ru.geekbrains.lesson1.util;

import java.util.ArrayList;

import ru.geekbrains.lesson1.net.WeatherWebDetails;

public class ParcelCitylDetails extends Parcel {
    private final boolean showWind;
    private final boolean showPressure;
    private final boolean darkTheme;

    public ParcelCitylDetails(String cityName, ArrayList<WeatherWebDetails> weather5days, boolean showWind, boolean showPressure, boolean darkTheme) {
        super(cityName, weather5days);
        this.showWind = showWind;
        this.showPressure = showPressure;
        this.darkTheme = darkTheme;
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
}
