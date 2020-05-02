package ru.geekbrains.lesson1.recycler;

// данные для карточки
public class Wed {

    private final String description; // описание
    private final int picture;        // изображение
    private final int temperature;        // изображение

    public Wed(String description, int picture, int temperature) {
        this.description=description;
        this.picture=picture;
        this.temperature=temperature;
    }

    // геттеры
    public String getDescription() {
        return description;
    }

    public int getPicture() {
        return picture;
    }

    public int getTemperature() {
        return temperature;
    }
}