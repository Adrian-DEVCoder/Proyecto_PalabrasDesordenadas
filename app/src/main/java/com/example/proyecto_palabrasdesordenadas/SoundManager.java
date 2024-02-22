package com.example.proyecto_palabrasdesordenadas;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager {
    private MediaPlayer mediaPlayer;

    public void playSound(Context context, int soundEffectIndex) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, getSoundResourceId(soundEffectIndex));
        mediaPlayer.start();
    }

    private int getSoundResourceId(int soundEffectIndex) {
        switch (soundEffectIndex) {
            case  1:
                return SoundEffect.MECH.getResId();
            case  2:
                return SoundEffect.BONK.getResId();
            case  3:
                return SoundEffect.POP.getResId();
            case  4:
                return SoundEffect.EXPLOSION.getResId();
            default:
                throw new IllegalArgumentException("Invalid sound effect index");
        }
    }
    public void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }}
    public void stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
