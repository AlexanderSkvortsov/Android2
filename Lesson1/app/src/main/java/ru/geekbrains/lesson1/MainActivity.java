package ru.geekbrains.lesson1;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import ru.geekbrains.lesson1.util.CitiesConst;


public class MainActivity extends AppCompatActivity implements CitiesConst {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);
        }

}
