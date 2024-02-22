package com.example.proyecto_palabrasdesordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class ConfiguracionActivity extends AppCompatActivity {

    private SeekBar seekBarMusicVolume;
    private SeekBar seekBarSoundVolume;
    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        seekBarMusicVolume = findViewById(R.id.seekBarMusicVolume);
        seekBarSoundVolume = findViewById(R.id.seekBarSoundVolume);
        soundManager = new SoundManager();
        Button buttonChangeSound = findViewById(R.id.btn_sound);
        buttonChangeSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.sonido++;
                if (MainActivity.sonido > SoundEffect.values().length) {
                    MainActivity.sonido =  1; // Vuelve al primer sonido
                }
                String newSoundName = SoundEffect.values()[MainActivity.sonido -  1].name();
                buttonChangeSound.setText(newSoundName);
                soundManager.playSound(ConfiguracionActivity.this, MainActivity.sonido);
            }
        });
        // Configura el listener para el volumen de la música
        seekBarMusicVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Convierte el progreso a un valor de volumen entre  0.0 y  1.0
                float volume = progress /  100.0f;
                // Cambia el volumen de la música de fondo
                Intent intent = new Intent("com.example.proyecto_palabrasdesordenadas.ACTION_CHANGE_VOLUME");
                intent.putExtra("volume",   volume); // Cambia el volumen a la mitad
                LocalBroadcastManager.getInstance(ConfiguracionActivity.this).sendBroadcast(intent);
                // Aquí debes implementar la lógica para enviar el volumen a tu servicio de música
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No necesitas hacer nada aquí
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // No necesitas hacer nada aquí
            }
        });

        // Configura el listener para el volumen del sonido
        seekBarSoundVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Convierte el progreso a un valor de volumen entre  0.0 y  1.0
                float volume = progress /  100.0f;
                // Cambia el volumen del sonido
                soundManager.setVolume(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No necesitas hacer nada aquí
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // No necesitas hacer nada aquí
            }
        });
    }
}