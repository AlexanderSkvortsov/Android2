package ru.geekbrains.lesson1.util;

public class ParcelCitylDetails extends Parcel {
    private final boolean showWind;
    private final boolean showPressure;

    public ParcelCitylDetails(String cityName, int[] temperatureOfWeek, boolean showWind, boolean showPressure) {
        super(cityName, temperatureOfWeek);
        this.showWind = showWind;
        this.showPressure = showPressure;
    }

    public boolean isShowWind() {
        return showWind;
    }

    public boolean isShowPressure() {
        return showPressure;
    }
}
