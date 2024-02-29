package com.example.proyecto_palabrasdesordenadas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PartidaActivity extends AppCompatActivity {
    private SoundManager soundManager;
    private boolean buttonContrarrelojClickado = false;
    private boolean buttonPuntuacionClickado = false;
    private String dificultadSeleccionada = "FÁCIL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);
        Intent intent  = getIntent();
        boolean imagenCambiada = intent.getBooleanExtra("imagenCambiada",false);
        ImageView imageViewLogo = findViewById(R.id.img_logo_partida);
        TextView textViewDificultad = findViewById(R.id.tv_dificultad);
        Button buttonDificultad = findViewById(R.id.btn_dificultad);
        Button buttonContrarreloj = findViewById(R.id.btn_contrarreloj);
        Button buttonPuntuacion = findViewById(R.id.btn_puntuacion);
        Button buttonComenzar = findViewById(R.id.btn_comenzar);
        soundManager = new SoundManager();
        soundManager.setVolume(MainActivity.volson);
        if(imagenCambiada){
            textViewDificultad.setText("DIFFICULTY:");
            buttonDificultad.setText("EASY");
            dificultadSeleccionada = "EASY";
            buttonContrarreloj.setText("TIME TRIAL MODE");
            buttonPuntuacion.setText("SCORING MODE");
            buttonComenzar.setText("START GAME");
        } else {
            textViewDificultad.setText("DIFICULTAD:");
            buttonDificultad.setText("FÁCIL");
            dificultadSeleccionada = "FÁCIL";
            buttonContrarreloj.setText("MODO CONTRARRELOJ");
            buttonPuntuacion.setText("MODO PUNTUACIÓN");
            buttonComenzar.setText("COMENZAR PARTIDA");
        }
        getWindow().setStatusBarColor(getResources().getColor(R.color.azuloscuro, this.getTheme()));

        // Listener del logo para realizar la accion de volver
        imageViewLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(PartidaActivity.this, MainActivity.sonido);
                Intent intent = new Intent(PartidaActivity.this, NuevaPartidaActivity.class);
                intent.putExtra("imagenCambiada", imagenCambiada);
                startActivity(intent);
            }
        });

        // Listener del boton de dificultad
        buttonDificultad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dificultadActual = buttonDificultad.getText().toString();
                soundManager.playSound(PartidaActivity.this, MainActivity.sonido);
                if(dificultadActual.equalsIgnoreCase("FÁCIL")){
                    buttonDificultad.setText("MEDIA");
                    dificultadSeleccionada = "MEDIA";
                } else if (dificultadActual.equalsIgnoreCase("EASY")){
                    buttonDificultad.setText("NORMAL");
                    dificultadSeleccionada = "NORMAL";
                } else if (dificultadActual.equalsIgnoreCase("MEDIA")){
                    buttonDificultad.setText("DIFÍCIL");
                    dificultadSeleccionada = "DIFÍCIL";
                } else if (dificultadActual.equalsIgnoreCase("NORMAL")){
                    buttonDificultad.setText("HARD");
                    dificultadSeleccionada = "HARD";
                } else if (dificultadActual.equalsIgnoreCase("DIFÍCIL")){
                    buttonDificultad.setText("FÁCIL");
                    dificultadSeleccionada = "FÁCIL";
                } else if (dificultadActual.equalsIgnoreCase("HARD")){
                    buttonDificultad.setText("EASY");
                    dificultadSeleccionada = "EASY";
                }
            }
        });
        // Listener del boton de contrarreloj
        buttonContrarreloj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(PartidaActivity.this, MainActivity.sonido);
                buttonContrarrelojClickado = !buttonContrarrelojClickado;
                if(buttonContrarrelojClickado && !buttonPuntuacionClickado){
                    buttonContrarreloj.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    buttonContrarreloj.setTextColor(Color.parseColor("#673AB7"));
                } else {
                    buttonContrarreloj.setBackgroundColor(Color.parseColor("#FF673AB7"));
                    buttonContrarreloj.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
        });
        // Listener del boton de puntuacion
        buttonPuntuacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(PartidaActivity.this, MainActivity.sonido);
                buttonPuntuacionClickado = !buttonPuntuacionClickado;
                if(buttonPuntuacionClickado && !buttonContrarrelojClickado){
                    buttonPuntuacion.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    buttonPuntuacion.setTextColor(Color.parseColor("#673AB7"));
                } else {
                    buttonPuntuacion.setBackgroundColor(Color.parseColor("#FF673AB7"));
                    buttonPuntuacion.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
        });
        // Listener del boton de comenzar partida, pasamos el idioma, dificultad y el estado de los modos
        buttonComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(PartidaActivity.this, MainActivity.sonido);
                if(!buttonContrarrelojClickado && !buttonPuntuacionClickado){
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PartidaActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_seleccionar_modo, null);
                    dialogBuilder.setView(dialogView);
                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                } else {
                    Intent intent = new Intent(PartidaActivity.this, JuegoActivity.class);
                    intent.putExtra("imagenCambiada", imagenCambiada);
                    intent.putExtra("dificultadSeleccionada", dificultadSeleccionada);
                    intent.putExtra("modoContrarreloj",buttonContrarrelojClickado);
                    intent.putExtra("modoPuntuacion", buttonPuntuacionClickado);
                    startActivity(intent);
                }
            }
        });
    }
}