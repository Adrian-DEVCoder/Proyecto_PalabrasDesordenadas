package com.example.proyecto_palabrasdesordenadas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.Toast;

import java.util.Random;

public class JuegoActivity extends AppCompatActivity {

    private int vidasRestantes = 3;
    private int contador = 0;
    private int puntuacion = 0;
    private CountDownTimer countDownTimer;
    private SQLiteDatabase database;
    private String palabraOriginal = "";
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        // Obtenemos la instancia de la base de datos
        dbHandler = new DBHandler(JuegoActivity.this);
        // Obtenemos los valores del idioma seleccionado, dificultad y modo de juego
        Intent intent = getIntent();
        boolean imagenCambiada = intent.getBooleanExtra("imagenCambiada",false);
        String dificultadSeleccionada = intent.getStringExtra("dificultadSeleccionada");
        boolean modoContrarreloj = intent.getBooleanExtra("modoContrarreloj",false);
        boolean modoPuntuacion = intent.getBooleanExtra("modoPuntuacion",false);
        // Declaracion y asignacion de variables
        Button buttonComprobar = findViewById(R.id.btn_comprobar);
        Button buttonBorrar = findViewById(R.id.btn_borrar);
        TextView textViewTimerScore = findViewById(R.id.tv_timerscore);
        ImageView imageViewVida1 = findViewById(R.id.img_vida);
        ImageView imageViewVida2 = findViewById(R.id.img_vida2);
        ImageView imageViewVida3 = findViewById(R.id.img_vida3);
        TextView textViewPalabra = findViewById(R.id.tv_palabra);
        TextView textViewCounter = findViewById(R.id.tv_counter);
        TextView textViewScore = findViewById(R.id.tv_score);
        TextView textViewPuntuacion = findViewById(R.id.tv_puntuacion);
        EditText editTextPalabraUsuario = findViewById(R.id.et_palabrausuario);
        editTextPalabraUsuario.setEnabled(false);
        editTextPalabraUsuario.setFocusable(false);
        editTextPalabraUsuario.setFocusableInTouchMode(false);
        GridLayout gridTeclado = findViewById(R.id.teclado);
        if (modoContrarreloj) {
            // Modo Contrarreloj: Mostramos el temporizador, la puntuación y las vidas
            textViewTimerScore.setVisibility(View.VISIBLE);
            textViewScore.setVisibility(View.VISIBLE);
            imageViewVida1.setVisibility(View.VISIBLE);
            imageViewVida2.setVisibility(View.VISIBLE);
            imageViewVida3.setVisibility(View.VISIBLE);
            textViewPuntuacion.setVisibility(View.GONE);
            textViewScore.setVisibility(View.GONE);
            iniciarTemporizador();
        } else if (modoPuntuacion) {
            // Modo Puntuación: Mostramos la puntuación y las vidas, ocultamos el temporizador
            textViewPuntuacion.setVisibility(View.VISIBLE);
            imageViewVida1.setVisibility(View.VISIBLE);
            imageViewVida2.setVisibility(View.VISIBLE);
            imageViewVida3.setVisibility(View.VISIBLE);
            textViewTimerScore.setVisibility(View.GONE); // Ocultamos el temporizador
            textViewCounter.setVisibility(View.GONE);
        }
        switch (dificultadSeleccionada) {
            case "FÁCIL":
                do {
                    palabraOriginal = dbHandler.generarPalabraAleatoria();
                } while (palabraOriginal == null || palabraOriginal.length() < 3 || palabraOriginal.length() > 4);
                break;
            case "MEDIA":
                do {
                    palabraOriginal = dbHandler.generarPalabraAleatoria();
                } while (palabraOriginal == null || palabraOriginal.length() < 5 || palabraOriginal.length() > 8);
                break;
            case "DIFÍCIL":
                do {
                    palabraOriginal = dbHandler.generarPalabraAleatoria();
                } while (palabraOriginal == null || palabraOriginal.length() < 9);
                break;
        }
        String palabraDesordenada = desordenarPalabra(palabraOriginal);
        textViewPalabra.setText(palabraDesordenada);
        crearTeclado(gridTeclado, editTextPalabraUsuario);
        buttonComprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String palabraUsuario = editTextPalabraUsuario.getText().toString().trim().toLowerCase();
                String palabraMostrada = textViewPalabra.getText().toString().trim().toLowerCase();
                if (!palabraUsuario.isEmpty() && palabraUsuario.length() == palabraMostrada.length()) {
                    if (palabraUsuario.equals(palabraOriginal.toLowerCase())) {
                        Toast.makeText(JuegoActivity.this,"Has acertado!",Toast.LENGTH_SHORT).show();
                        contador++;
                        textViewCounter.setText(String.valueOf(contador));
                        String nuevaPalabra;
                        do {
                            nuevaPalabra = dbHandler.generarPalabraAleatoria();
                        } while (!verificarLongitudPalabra(nuevaPalabra, dificultadSeleccionada));
                        palabraOriginal = nuevaPalabra;
                        String nuevaPalabraDesordenada = desordenarPalabra(nuevaPalabra);
                        textViewPalabra.setText(nuevaPalabraDesordenada);
                        editTextPalabraUsuario.setText("");
                        Random random = new Random();
                        int puntuacionAleatoria = random.nextInt(11) + 15;
                        puntuacion += puntuacionAleatoria;
                        textViewScore.setText(String.valueOf(puntuacion));
                    } else {
                        Toast.makeText(JuegoActivity.this,"Has fallado!",Toast.LENGTH_SHORT).show();
                        vidasRestantes--;
                        switch (vidasRestantes){
                            case 2:
                                imageViewVida1.setImageResource(R.drawable.nlife);
                                break;
                            case 1:
                                imageViewVida2.setImageResource(R.drawable.nlife);
                                break;
                            case 0:
                                imageViewVida3.setImageResource(R.drawable.nlife);
                                countDownTimer.cancel();
                                Toast.makeText(JuegoActivity.this, "Se agotaron las vidas del juego. Juego Terminado.",Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        });
        buttonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPalabraUsuario.setText("");
            }
        });
    }

    private void iniciarTemporizador() {
        TextView textViewTimer = findViewById(R.id.tv_timerscore);
        countDownTimer = new CountDownTimer(90000, 1000) {
            @Override
            public void onTick(long l) {
                textViewTimer.setText(""+l / 1000);
            }

            @Override
            public void onFinish() {
                textViewTimer.setText("Tiempo agotado!!");
                terminarJuego();
            }
        };
        textViewTimer.setTextSize(32); // Tamaño del texto
        textViewTimer.setTextColor(Color.RED); // Color del texto
        Typeface customTypeface = ResourcesCompat.getFont(this, R.font.montserrat_bold); // Fuente personalizada
        textViewTimer.setTypeface(customTypeface);
        countDownTimer.start();
    }

    private boolean verificarLongitudPalabra(String palabra, String dificultad) {
        switch (dificultad) {
            case "FÁCIL":
                return palabra.length() >= 3 && palabra.length() <= 4;
            case "NORMAL":
                return palabra.length() >= 5 && palabra.length() <= 8;
            case "DÍFICIL":
                return palabra.length() >= 9;
            default:
                return false; // O manejar el caso por defecto según lo que necesites
        }
    }

    private void terminarJuego() {
        countDownTimer.cancel();
        Toast.makeText(this, "Se agotaron las vidas. Juego terminado.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public String desordenarPalabra(String palabra){
        char[] letras = palabra.toCharArray();
        Random random = new Random();
        for(int i=0;i<letras.length;i++){
            int iAleatorio = random.nextInt(letras.length);
            char temp = letras[i];
            letras[i] = letras[iAleatorio];
            letras[iAleatorio] = temp;
        }
        return new String(letras);
    }

    private void crearTeclado(GridLayout gridTeclado, EditText editTextPalabra) {
        char[][] letras = {
                {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M'},
                {'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'Y', 'Z'}
        };
        // Fuente personalizada para los botones
        Typeface customTypeface = ResourcesCompat.getFont(this, R.font.montserrat_bold);
        // Valores para el grid del usuario y ancho de botones
        int numColumnas = 8;
        gridTeclado.setColumnCount(numColumnas);
        int anchoPantalla = getResources().getDisplayMetrics().widthPixels;
        int anchoBoton =98;
        // Animacion para la pulsacion de los botones
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(100); // Duración de la animación en milisegundos
        // Añadir los botones a la vista
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