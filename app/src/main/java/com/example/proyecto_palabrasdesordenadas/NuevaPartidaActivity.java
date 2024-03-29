package com.example.proyecto_palabrasdesordenadas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class NuevaPartidaActivity extends AppCompatActivity {
    private SoundManager soundManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_partida);
        Intent intent = getIntent();
        boolean imagenCambiada = intent.getBooleanExtra("imagenCambiada", false);
        ImageView imageViewLogo = findViewById(R.id.img_logo_nueva);
        ImageButton buttonUnJugador = findViewById(R.id.btn_singleplayer);
        ImageButton buttonMultijugador = findViewById(R.id.btn_multiplayer);
        TextView textViewUnJugador = findViewById(R.id.tv_singleplayer);
        TextView textViewMultijugador = findViewById(R.id.tv_multiplayer);
        soundManager = new SoundManager();
        soundManager.setVolume(MainActivity.volson);
        if(imagenCambiada){
            textViewUnJugador.setText(R.string.singleplayer);
            textViewMultijugador.setText(R.string.s_multiplayer);
        } else {
            textViewUnJugador.setText(R.string.un_jugador);
            textViewMultijugador.setText(R.string.multijugador);
        }
        getWindow().setStatusBarColor(getResources().getColor(R.color.azuloscuro, this.getTheme()));
        // Listener para el logo para realizar la accion de volver
        imageViewLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(NuevaPartidaActivity.this,  MainActivity.sonido);
                Intent intent = new Intent(NuevaPartidaActivity.this, MenuActivity.class);
                intent.putExtra("imagenCambiada",imagenCambiada);
                startActivity(intent);
            }
        });
        // Listener para el boton de un jugador
        buttonUnJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(NuevaPartidaActivity.this,  MainActivity.sonido);
                Intent intent = new Intent(NuevaPartidaActivity.this, PartidaActivity.class);
                intent.putExtra("imagenCambiada",imagenCambiada);
                startActivity(intent);
            }
        });
        // Listener para el boton de multijugador
        buttonMultijugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(NuevaPartidaActivity.this,  MainActivity.sonido);
                Intent intent = new Intent(NuevaPartidaActivity.this, MultijugadorActivity.class);
                intent.putExtra("imagenCambiada",imagenCambiada);
                startActivity(intent);
            }
        });
    }
}