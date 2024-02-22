package com.example.proyecto_palabrasdesordenadas;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class BackgroundSoundService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private final BroadcastReceiver volumeChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.example.proyecto_palabrasdesordenadas.ACTION_CHANGE_VOLUME")) {
                float volume = intent.getFloatExtra("volume",  1.0f);
                setVolume(volume);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).registerReceiver(volumeChangeReceiver,
                new IntentFilter("com.example.proyecto_palabrasdesordenadas.ACTION_CHANGE_VOLUME"));
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Si mediaPlayer ya está en ejecución, simplemente ignora la solicitud
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            return START_STICKY;
        }

        // Recuperar el valor entero del Intent
        int songSelection = intent.getIntExtra("song_selection",  1); // Valor por defecto  1 si no se encuentra

        // Seleccionar el recurso de la canción basado en el valor de songSelection
        int resId;
        if (songSelection ==  1) {
            resId = R.raw.music; // Reemplazar con el nombre real de tu archivo de música
        } else if (songSelection ==  2) {
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

    public void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }
    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            LocalBroadcastManager.getInstance(this).unregisterReceiver(volumeChangeReceiver);
        }
        super.onDestroy();
    }
}
