package com.example.proyecto_palabrasdesordenadas;

import androidx.annotation.NonNull;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class JuegoMultijugadorActivity extends AppCompatActivity {
    SoundManager soundManager;
    DBHandler dbHandler;
    public String palabraOriginal;
    private DatabaseReference gameRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_multijugador);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        gameRef = database.getReference("juego");
        dbHandler = new DBHandler(JuegoMultijugadorActivity.this);
        Intent stopIntent = new Intent(this, BackgroundSoundService.class);
        stopService(stopIntent);
        // Iniciar el servicio de nuevo con la nueva canción
        Intent startIntent = new Intent(this, BackgroundSoundService.class);
        startIntent.putExtra("song_selection",  2); //  2 para la canción "reloj"
        startService(startIntent);
        ProgressBar hpBarUsuario = findViewById(R.id.hpbar_usuario);
        ProgressBar hpBarRival = findViewById(R.id.hpbar_rival);
        hpBarUsuario.setMax(4000);
        hpBarUsuario.setProgress(4000);
        hpBarRival.setMax(4000);
        hpBarRival.setMax(4000);
        TextView textViewPalabra = findViewById(R.id.tv_palabra);
        EditText editTextPalabra = findViewById(R.id.et_palabrausuario);
        editTextPalabra.setEnabled(false);
        editTextPalabra.setFocusable(false);
        editTextPalabra.setFocusableInTouchMode(false);
        Button buttonComprobar = findViewById(R.id.btn_comprobar);
        Button buttonBorrar = findViewById(R.id.btn_borrar);
        GridLayout gridTeclado = findViewById(R.id.teclado);
        soundManager = new SoundManager();
        soundManager.setVolume(MainActivity.volson);
        buttonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPalabra.setText("");
            }
        });
        buttonComprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarPalabra(editTextPalabra,textViewPalabra);
            }
        });
        inicializarJuego();
        crearTeclado(gridTeclado,editTextPalabra);
        generarDesordenarPalabra();
    }
    private void comprobarPalabra(EditText editTextPalabraUsuario, TextView textViewPalabra){
        String palabraUsuario = editTextPalabraUsuario.getText().toString();
        String palabraGenerada = palabraOriginal;
        if(palabraUsuario.equalsIgnoreCase(palabraGenerada)){
            Toast.makeText(JuegoMultijugadorActivity.this, "¡Correcto!", Toast.LENGTH_SHORT).show();
            editTextPalabraUsuario.setText("");
            ProgressBar hpBarRival = findViewById(R.id.hpbar_rival);
            int vidaActualRival = hpBarRival.getProgress();
            int vidaReducida = new Random().nextInt(201) +   200; // Número aleatorio entre   200 y   400
            hpBarRival.setProgress(vidaActualRival - vidaReducida);
            reducirVidaRival(vidaReducida);
            generarDesordenarPalabra();
        } else {
            Toast.makeText(JuegoMultijugadorActivity.this, "Incorrecto, intenta de nuevo.", Toast.LENGTH_SHORT).show();
        }
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
        // Añadimos los botones a la vista
        for (int i = 0; i < letras.length; i++) {
            for (int j = 0; j < letras[i].length; j++) {
                Button button = new Button(this);
                button.setText(String.valueOf(letras[i][j]));
                button.setTypeface(customTypeface);
                button.setGravity(Gravity.CENTER);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        soundManager.playSound(JuegoMultijugadorActivity.this, MainActivity.sonido);
                        Button buttonClickado = (Button) view;
                        String letraSeleccionada = buttonClickado.getText().toString();
                        editTextPalabra.setText(editTextPalabra.getText() + letraSeleccionada);
                    }
                });

                // Aplicamos el estilo
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.width = anchoBoton;
                params.setMargins(8, 8, 8, 8);  // Ajustamos el margen
                button.setLayoutParams(params);
                button.setTextSize(22);

                // Establecemos el fondo del botón con el drawable personalizado
                button.setBackgroundResource(R.drawable.boton_personalizado);
                button.setTextColor(Color.parseColor("#CDA434"));
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

    private void generarDesordenarPalabra(){
        palabraOriginal = dbHandler.generarPalabraAleatoriaMultijugador();
        // Asegurarse de que la palabra tenga entre  3 y  6 caracteres
        while (palabraOriginal.length() >=  3 || palabraOriginal.length() <=  5) {
            palabraOriginal = dbHandler.generarPalabraAleatoriaMultijugador();
        }
        String palabraDesordenada = desordenarPalabra(palabraOriginal);
        TextView textViewPalabra = findViewById(R.id.tv_palabra);
        textViewPalabra.setText(palabraDesordenada);
    }

    private void reducirVidaRival(int vidaReducida) {
        ProgressBar hpBarRival = findViewById(R.id.hpbar_rival);
        int vidaActualRival = hpBarRival.getProgress();
        hpBarRival.setProgress(vidaActualRival - vidaReducida);
        // Actualizar la vida del rival en Firebase
        gameRef.child("vidaRival").setValue(vidaActualRival - vidaReducida);
    }

    private void escucharVidaUsuario() {
        gameRef.child("vidaUsuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int vidaUsuario = dataSnapshot.getValue(Integer.class);
                ProgressBar hpBarUsuario = findViewById(R.id.hpbar_usuario);
                hpBarUsuario.setProgress(vidaUsuario);
                // Verificar si el usuario ha perdido
                if (vidaUsuario <=  0) {
                    // El rival es el ganador
                    Toast.makeText(JuegoMultijugadorActivity.this, "¡El rival es el ganador!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores
                Toast.makeText(JuegoMultijugadorActivity.this, "Error al obtener datos de Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void escucharVidaRival() {
        gameRef.child("vidaRival").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int vidaRival = dataSnapshot.getValue(Integer.class);
                ProgressBar hpBarRival = findViewById(R.id.hpbar_rival);
                hpBarRival.setProgress(vidaRival);
                // Verificar si el rival ha perdido
                if (vidaRival <=  0) {
                    // El usuario es el ganador
                    Toast.makeText(JuegoMultijugadorActivity.this, "¡El usuario es el ganador!", Toast.LENGTH_SHORT).show();
                    finish();
                    // Aquí puedes realizar acciones adicionales, como mostrar un mensaje de victoria o reiniciar el juego
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores
                Toast.makeText(JuegoMultijugadorActivity.this, "Error al obtener datos de Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inicializarJuego() {
        gameRef.child("vidaRival").setValue(4000);
        gameRef.child("vidaUsuario").setValue(4000);
        escucharVidaRival();
        escucharVidaUsuario();
    }

}