package com.example.proyecto_palabrasdesordenadas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MenuActivity extends AppCompatActivity {
    private SoundManager soundManager;
    private boolean imagenCambiada = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent stopIntent = new Intent(this, BackgroundSoundService.class);
        stopService(stopIntent);
        // Iniciar el servicio de nuevo con la nueva canción
        Intent startIntent = new Intent(this, BackgroundSoundService.class);
        startIntent.putExtra("song_selection", 1);
        startService(startIntent);
        ImageView imageViewLogo = findViewById(R.id.img_logo2);
        Button buttonNuevaPartida = findViewById(R.id.btn_nueva_partida);
        Button buttonConfiguracion = findViewById(R.id.btn_configuracion);
        ImageButton imageButtonIdioma = findViewById(R.id.btn_idioma);
        Button buttonPerfil = findViewById(R.id.btn_perfil);
        getWindow().setStatusBarColor(getResources().getColor(R.color.azuloscuro, this.getTheme()));
        soundManager = new SoundManager();
        soundManager.setVolume(MainActivity.volson);
        // Listener del logo para volver atras
        imageViewLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(MenuActivity.this,  MainActivity.sonido);
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
            }
        });
        // Listener del boton para nueva partida
        buttonNuevaPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(MenuActivity.this,  MainActivity.sonido);
                Intent intent = new Intent(MenuActivity.this, NuevaPartidaActivity.class);
                intent.putExtra("imagenCambiada",imagenCambiada);
                startActivity(intent);
            }
        });
        // Listener del boton de configuracion
        buttonConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(MenuActivity.this,  MainActivity.sonido);
                startActivity(new Intent(MenuActivity.this, ConfiguracionActivity.class));
            }
        });
        // Listener del boton para cambiar la bandera
        imageButtonIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(MenuActivity.this,  MainActivity.sonido);
                if(imagenCambiada){
                    imageButtonIdioma.setImageResource(R.drawable.espana);
                    buttonNuevaPartida.setText(R.string.button_nueva);
                    buttonConfiguracion.setText(R.string.button_ajustes);
                    buttonPerfil.setText(R.string.button_perfil);
                } else {
                    imageButtonIdioma.setImageResource(R.drawable.estados_unidos);
                    buttonNuevaPartida.setText(R.string.button_new);
                    buttonConfiguracion.setText(R.string.button_setting);
                    buttonPerfil.setText(R.string.button_profile);
                }
                imagenCambiada = !imagenCambiada;
            }
        });
        // Listener del boton del perfil
        buttonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(MenuActivity.this,  MainActivity.sonido);
                startActivity(new Intent(MenuActivity.this, PerfilActivity.class));
            }
        });
    }
}