package com.example.proyecto_palabrasdesordenadas;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

public class BackgroundSoundService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Recuperar el valor entero del Intent
        int songSelection = intent.getIntExtra("song_selection", 1); // Valor por defecto 1 si no se encuentra

        // Seleccionar el recurso de la canción basado en el valor de songSelection
        int resId;
        if (songSelection == 1) {
            resId = R.raw.music; // Reemplazar con el nombre real de tu archivo de música
        } else if (songSelection == 2) {
            resId = R.raw.reloj; // Reemplazar con el nombre real de tu archivo de música
        } else {
            throw new IllegalArgumentException("Invalid song selection");
        }

        // Crear el MediaPlayer con la canción seleccionada
        mediaPlayer = MediaPlayer.create(getApplicationContext(), resId);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
