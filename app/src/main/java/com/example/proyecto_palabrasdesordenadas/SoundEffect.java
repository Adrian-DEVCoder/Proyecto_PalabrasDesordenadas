package com.example.proyecto_palabrasdesordenadas;

public enum SoundEffect {
    CLICK(R.raw.mech),
    BONK(R.raw.bonk),
    POP(R.raw.pop),
    EXPLOSION(R.raw.explosion),
    CORRECT(R.raw.correct),
    NEGATIVE(R.raw.negative);

    private int resId;

    SoundEffect(int resId){ this.resId = resId; }

    public int getResId() { return resId; }
}
