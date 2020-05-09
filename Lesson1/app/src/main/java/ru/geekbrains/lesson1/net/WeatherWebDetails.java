package ru.geekbrains.lesson1.net;

import java.io.Serializable;
import java.util.Date;

public class WeatherWebDetails implements Serializable {
    private int temperature;
    private int pressure;
    private int wind;
    private int ico;
    private long dt;

    public WeatherWebDetails(int temperature, int pressure, int wind, int ico, long dt) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.wind = wind;
        this.ico = ico;
        this.dt = dt;
    }

    public long getDt() {
        return dt;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public int getWind() {
        return wind;
    }

    public int getIco() {
        return ico;
    }

    public int getPackParcel() {
        return Math.abs(temperature)+ico*100+pressure*10000+ wind*10000000 * ((temperature <0)?-1:1);
    }

}
