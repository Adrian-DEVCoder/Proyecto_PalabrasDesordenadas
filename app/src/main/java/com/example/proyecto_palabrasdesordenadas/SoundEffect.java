package com.example.proyecto_palabrasdesordenadas;

public enum SoundEffect {
    MECH(R.raw.mech),
    BONK(R.raw.bonk),
    POP(R.raw.pop),
    EXPLOSION(R.raw.explosion);

    private int resId;

    SoundEffect(int resId) {
        this.resId = resId;
    }

    public int getResId() {
        return resId;
    }
}
