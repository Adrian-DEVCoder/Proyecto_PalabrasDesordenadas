package com.example.proyecto_palabrasdesordenadas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MenuActivity extends AppCompatActivity {

    private boolean imagenCambiada = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ImageView imageViewLogo = findViewById(R.id.img_logo);
        Button buttonNuevaPartida = findViewById(R.id.btn_nueva_partida);
        Button buttonContinuarPartida = findViewById(R.id.btn_continuar_partida);
        Button buttonConfiguracion = findViewById(R.id.btn_configuracion);
        ImageButton imageButtonIdioma = findViewById(R.id.btn_idioma);
        Button buttonPerfil = findViewById(R.id.btn_perfil);
        getWindow().setStatusBarColor(getResources().getColor(R.color.azuloscuro, this.getTheme()));
        // Listener del logo para volver atras
        imageViewLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
            }
        });
        // Listener del boton para nueva partida
        buttonNuevaPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, NuevaPartidaActivity.class));
            }
        });
        // Listener del boton para continuar partida
        buttonContinuarPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, PartidaActivity.class));
            }
        });
        // Listener del boton de configuracion
        buttonConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, ConfiguracionActivity.class));
            }
        });
        // Listener del boton para cambiar la bandera
        imageButtonIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagenCambiada){
                    imageButtonIdioma.setImageResource(R.drawable.espana);
                    buttonNuevaPartida.setText(R.string.button_nueva);
                    buttonContinuarPartida.setText(R.string.button_continuar);
                    buttonConfiguracion.setText(R.string.button_ajustes);
                    buttonPerfil.setText(R.string.button_perfil);
                } else {
                    imageButtonIdioma.setImageResource(R.drawable.estados_unidos);
                    buttonNuevaPartida.setText(R.string.button_new);
                    buttonContinuarPartida.setText(R.string.button_continue);
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
                startActivity(new Intent(MenuActivity.this, PerfilActivity.class));
            }
        });
    }
}