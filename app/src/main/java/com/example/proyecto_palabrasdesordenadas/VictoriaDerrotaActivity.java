package com.example.proyecto_palabrasdesordenadas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VictoriaDerrotaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victoria_derrota);

        TextView textViewVictoria = findViewById(R.id.tv_victoria);
        Button buttonVolver = findViewById(R.id.btn_volver);
        String idJugadorActual = JuegoMultijugadorActivity.idUsuario; // Asume que idUsuario es el ID del jugador actual
        // Determinar si el jugador actual es el ganador o el perdedor
        boolean esGanador = idJugadorActual.equals(JuegoMultijugadorActivity.idGanador);
        // Configurar el TextView y el color de fondo según si es ganador o perdedor
        if (esGanador) {
            textViewVictoria.setText("VICTORIA");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getWindow().getDecorView().setBackgroundResource(R.drawable.background_color_victoria);
                }
            });
        } else {
            textViewVictoria.setText("DERROTA");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getWindow().getDecorView().setBackgroundResource(R.drawable.background_color_derrota);
                }
            });
        }
        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VictoriaDerrotaActivity.this,MenuActivity.class));
            }
        });
    }
}