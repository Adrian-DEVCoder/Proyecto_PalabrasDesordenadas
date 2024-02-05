package com.example.proyecto_palabrasdesordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class JuegoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        // Obtenemos los valores del idioma seleccionado, dificultad y modo de juego
        Intent intent = getIntent();
        boolean imagenCambiada = intent.getBooleanExtra("imagenCambiada",false);
        String dificultadSeleccionada = intent.getStringExtra("dificultadSeleccionada");
        boolean modoContrarreloj = intent.getBooleanExtra("modoContrarreloj",false);
        boolean modoPuntuacion = intent.getBooleanExtra("modoPuntuacion",false);
        // Declaracion y asignacion de variables
        TextView textViewTimerScore = findViewById(R.id.tv_timerscore);
        ImageView imageViewVida1 = findViewById(R.id.img_vida);
        ImageView imageViewVida2 = findViewById(R.id.img_vida2);
        ImageView imageViewVida3 = findViewById(R.id.img_vida3);
        TextView textViewPalabra = findViewById(R.id.tv_palabra);
        EditText editTextPalabraUsuario = findViewById(R.id.et_palabrausuario);
        GridLayout gridTeclado = findViewById(R.id.teclado);
        crearTeclado(gridTeclado, editTextPalabraUsuario);
    }

    private void crearTeclado(GridLayout gridTeclado, EditText editTextPalabra) {
        // Creamos una matriz de las letras del abecedario
        char[][] letras = {
                {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M'},
                {'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'Y', 'Z'}
        };
        Typeface customTypeface = ResourcesCompat.getFont(this, R.font.montserrat_bold);
        int numColumnas = 8;
        gridTeclado.setColumnCount(numColumnas);
        int anchoPantalla = getResources().getDisplayMetrics().widthPixels;
        int anchoBoton =98;

        // Configuramos la animación de escala
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(100); // Duración de la animación en milisegundos

        for (int i = 0; i < letras.length; i++) {
            for (int j = 0; j < letras[i].length; j++) {
                Button button = new Button(this);
                button.setText(String.valueOf(letras[i][j]));
                button.setTypeface(customTypeface);
                button.setGravity(Gravity.CENTER);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button buttonClickado = (Button) view;
                        String letraSeleccionada = buttonClickado.getText().toString();
                        editTextPalabra.setText(editTextPalabra.getText() + letraSeleccionada);
                    }
                });

                // Aplicamos el estilo
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.width = anchoBoton;
                params.setMargins(8, 8, 8, 8);  // Ajusta el margen según tus preferencias
                button.setLayoutParams(params);
                button.setTextSize(22); // Puedes ajustar el tamaño del texto aquí

                // Establecemos el fondo del botón con el drawable personalizado
                button.setBackgroundResource(R.drawable.boton_personalizado);

                // Color del texto (amarillo/dorado)
                button.setTextColor(Color.parseColor("#CDA434"));

                // Agregamos la animación de escala al botón
                button.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            view.startAnimation(scaleAnimation);
                        }
                        return false;
                    }
                });

                // Agregamos el botón al layout
                gridTeclado.addView(button);
            }
        }
    }

}