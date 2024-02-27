package com.example.proyecto_palabrasdesordenadas;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

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

        if (intent == null) {
            // Manejar el caso en que el Intent es null
            Log.e("BackgroundSoundService", "Intent es null");
            return START_STICKY;
        }

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            return START_STICKY;
        }

        int songSelection = intent.getIntExtra("song_selection",  1);

        int resId;
        if (songSelection ==  1) {
            resId = R.raw.music;
        } else if (songSelection ==  2) {
            resId = R.raw.reloj;
        } else {
            throw new IllegalArgumentException("Invalid song selection");
        }

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
