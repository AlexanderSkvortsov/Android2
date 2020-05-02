package ru.geekbrains.lesson1.util;

public class ParcelCitylDetails extends Parcel {
    private final boolean showWind;
    private final boolean showPressure;
    private final boolean darkTheme;

    public ParcelCitylDetails(String cityName, int[] temperatureOfWeek, boolean showWind, boolean showPressure, boolean darkTheme) {
        super(cityName, temperatureOfWeek);
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
