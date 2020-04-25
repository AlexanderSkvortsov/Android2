package ru.geekbrains.lesson1.recycler;

import android.content.res.Resources;

import ru.geekbrains.lesson1.util.Parcel;

public class CitySourceBuilder {

        private Resources resources;
        private Parcel parcel;

        public CitySourceBuilder setResources(Resources resources, Parcel parcel) {
            this.resources = resources;
            this.parcel = parcel;
            return this;
        }

        public CityDataSource build() {
            CitySource socSource = new CitySource(resources, parcel);
            socSource.init();
            return socSource;
        }
}
