package com.example.proyecto_palabrasdesordenadas;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager {
    private MediaPlayer mediaPlayer;
    private float volumen;

    // Metodo para reproducir los diferentes sonidos
    public void playSound(Context context, int soundEffectIndex){
        if(mediaPlayer != null){
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, getSoundResourceId(soundEffectIndex));
        mediaPlayer.setVolume(volumen, volumen);
        mediaPlayer.start();
    }
    // Metodo para obtener el recurso del sonido seleccionado
    public int getSoundResourceId(int soundEffectIndex){
        switch (soundEffectIndex){
            case 1:
                return SoundEffect.CLICK.getResId();
            case 2:
                return SoundEffect.BONK.getResId();
            case 3:
                return SoundEffect.POP.getResId();
            case 4:
                return SoundEffect.EXPLOSION.getResId();
            case 5:
                return SoundEffect.CORRECT.getResId();
            case 6:
                return SoundEffect.NEGATIVE.getResId();
            default:
                throw new IllegalArgumentException("Indice del recurso seleccionado invalido.");
        }
    }
    // Metodo para ajustar el volumen de la aplicacion
    public void setVolume(float volume){
        volumen = volume;
        if(mediaPlayer != null){
            mediaPlayer.setVolume(volume, volume);
        }
    }
    // Metodo para parar los sonidos
    public void stopSound(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
