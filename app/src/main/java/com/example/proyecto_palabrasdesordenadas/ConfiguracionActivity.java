package com.example.proyecto_palabrasdesordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
        seekBarMusicVolume.setProgress(MainActivity.progMusic);
        seekBarSoundVolume.setProgress(MainActivity.progSound);
        soundManager = new SoundManager();
        ImageView imageViewLogo = findViewById(R.id.img_logo2);
        Button buttonChangeSound = findViewById(R.id.btn_sound);
        String newSoundName = SoundEffect.values()[MainActivity.sonido - 1].name();
        buttonChangeSound.setText(newSoundName);
        imageViewLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(ConfiguracionActivity.this,  MainActivity.sonido);
                startActivity(new Intent(ConfiguracionActivity.this, MainActivity.class));
            }
        });
        buttonChangeSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.sonido++;
                if(MainActivity.sonido > SoundEffect.values().length - 2){
                    MainActivity.sonido = 1;
                }
                String newSoundName = SoundEffect.values()[MainActivity.sonido - 1].name();
                buttonChangeSound.setText(newSoundName);
                soundManager.setVolume(MainActivity.volson);
                soundManager.playSound(ConfiguracionActivity.this, MainActivity.sonido);
            }
        });

        seekBarMusicVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MainActivity.progMusic = progress;
                float volume = progress / 100.0f;
                Intent intent = new Intent("com.example.proyecto_palabrasdesordenadas.ACTION_CHANGE_VOLUME");
                intent.putExtra("volume", volume);
                LocalBroadcastManager.getInstance(ConfiguracionActivity.this).sendBroadcast(intent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Metodo vacio, no hace falta la implementacion de logica en este
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Metodo vacio, no hace falta la implementacion de logica en este
            }
        });

        // Configuramos el listener para el volumen del sonido
        seekBarSoundVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                MainActivity.progSound = i;
                float volume = i / 100.0f;
                soundManager.setVolume(volume);
                MainActivity.volson = volume;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}