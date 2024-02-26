package com.example.proyecto_palabrasdesordenadas;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ImageView icon1 = findViewById(R.id.icon1);
        ImageView icon2 = findViewById(R.id.icon2);
        ImageView icon3 = findViewById(R.id.icon3);
        ImageView icon4 = findViewById(R.id.icon4);
        ImageView icon5 = findViewById(R.id.icon5);
        ImageView icon6 = findViewById(R.id.icon6);
        ImageView icon7 = findViewById(R.id.icon7);

    }
}
