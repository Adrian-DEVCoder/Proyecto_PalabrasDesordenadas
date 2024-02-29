package com.example.proyecto_palabrasdesordenadas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class JuegoMultijugadorActivity extends AppCompatActivity {
    SoundManager soundManager;
    DBHandler dbHandler;
    public String palabraOriginal;
    private ExecutorService executorService;
    public static String idUsuario;
    public String idRival;
    public static String idGanador;
    public static String idPerdedor;
    private DatabaseReference gameRef;
    private ValueEventListener vidaUsuarioListener;
    private ValueEventListener vidaRivalListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_multijugador);
        executorService = Executors.newSingleThreadExecutor();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        gameRef = database.getReference("juego");
        // Recuperar los identificadores desde el Intent
        Intent intent = getIntent();
        idUsuario = intent.getStringExtra("idUsuario");
        idRival = intent.getStringExtra("idRival");
        // Asegurarse de que los identificadores no sean nulos
        if (idUsuario == null || idRival == null) {
            finish();
            return;
        }
        establecerVidaUsuarioPorDefecto(idUsuario);
        establecerVidaRivalPorDefecto(idRival);
        establecerDamageUsuarioPorDefecto(idUsuario);
        establecerDamageRivalPorDefecto(idRival);
        dbHandler = new DBHandler(JuegoMultijugadorActivity.this);
        boolean esIngles = false;
        Intent stopIntent = new Intent(this, BackgroundSoundService.class);
        stopService(stopIntent);
        // Iniciar el servicio de nuevo con la nueva canción
        Intent startIntent = new Intent(this, BackgroundSoundService.class);
        startIntent.putExtra("song_selection", 2); // 2 para la canción "reloj"
        startService(startIntent);
        ProgressBar hpBarUsuario = findViewById(R.id.hpbar_usuario);
        ProgressBar hpBarRival = findViewById(R.id.hpbar_rival);
        hpBarUsuario.setMax(4000);
        hpBarUsuario.setProgress(4000);
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        escucharVidaUsuario(hpBarUsuario);
        escucharVidaRival(hpBarRival);
        escucharDañoRival();
        crearTeclado(gridTeclado,editTextPalabra);
        generarDesordenarPalabra(esIngles);
        buttonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPalabra.setText("");
            }
        });
        buttonComprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarPalabra(editTextPalabra,textViewPalabra,esIngles);
            }
        });
    }

    // Metodos para establecer los atributos de los jugadores
    private void establecerVidaUsuarioPorDefecto(String idUsuario){
        DatabaseReference vidaUsuarioRef = gameRef.child(idUsuario).child("vida");
        vidaUsuarioRef.setValue(4000);
    }
    private void establecerVidaRivalPorDefecto(String idRival){
        DatabaseReference vidaRivalRef = gameRef.child(idRival).child("vida");
        vidaRivalRef.setValue(4000);
    }
    private void establecerDamageUsuarioPorDefecto(String idUsuario){
        DatabaseReference damageUsuarioRef = gameRef.child(idUsuario).child("damage");
        damageUsuarioRef.setValue(0);
    }
    private void establecerDamageRivalPorDefecto(String idRival){
        DatabaseReference damageRivalRef = gameRef.child(idRival).child("damage");
        damageRivalRef.setValue(0);
    }
    // Metodo para aplicar el daño al usuario
    private void aplicarDañoRival(int damage) {
        DatabaseReference dañoRivalRef = gameRef.child(idRival).child("damage");
        dañoRivalRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Integer damageActual = currentData.getValue(Integer.class);
                if (damageActual == null) {
                    damageActual = 0;
                }
                int nuevoDamage = damageActual + damage;
                currentData.setValue(nuevoDamage);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                // Manejar el resultado de la transacción
            }
        });
    }
    // Metodo para escuchar el damage del rival
    private void escucharDañoRival() {
        DatabaseReference dañoRivalRef = gameRef.child(idRival).child("damage");
        dañoRivalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int damage = snapshot.getValue(Integer.class);
                if (damage > 0) {
                    reducirVidaRival(damage);
                    resetearDañoRival();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar error
            }
        });
    }
    private void resetearDañoRival() {
        DatabaseReference dañoRivalRef = gameRef.child(idRival).child("damage");
        dañoRivalRef.setValue(0);
    }
    // Metodos para obtener la vida tanto del usuario como del rival
    private void escucharVidaUsuario(ProgressBar hpBarUsuario){
        DatabaseReference vidaUsuarioRef = gameRef.child(idUsuario).child("vida");
        vidaUsuarioListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int vidaActual = snapshot.getValue(Integer.class);
                hpBarUsuario.setProgress(vidaActual);
                if(vidaActual <= 0){
                    redirigirAJugadorGanador(idRival); // El rival es el ganador
                    redirigirAJugadorPerdedor(idUsuario); // El usuario actual es el perdedor
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        vidaUsuarioRef.addValueEventListener(vidaUsuarioListener);
    }

    private void escucharVidaRival(ProgressBar hpBarRival){
        DatabaseReference vidaRivalRef = gameRef.child(idRival).child("vida");
        vidaRivalListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int vidaActual = snapshot.getValue(Integer.class);
                hpBarRival.setProgress(vidaActual);
                if(vidaActual <= 0){
                    redirigirAJugadorGanador(idUsuario); // El usuario actual es el ganador
                    redirigirAJugadorPerdedor(idRival); // El rival es el perdedor
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        vidaRivalRef.addValueEventListener(vidaRivalListener);
    }

    // Metodo para redirigir a victoria o a derrota
    private void redirigirAJugadorGanador(String idGanador) {
        JuegoMultijugadorActivity.idGanador = idGanador;
        Intent intent = new Intent(JuegoMultijugadorActivity.this, VictoriaDerrotaActivity.class);
        startActivity(intent);
        finish(); // Finaliza la actividad actual para evitar regresar a ella
    }

    private void redirigirAJugadorPerdedor(String idPerdedor) {
        JuegoMultijugadorActivity.idPerdedor = idPerdedor;
        Intent intent = new Intent(JuegoMultijugadorActivity.this, VictoriaDerrotaActivity.class);
        startActivity(intent);
        finish(); // Finaliza la actividad actual para evitar regresar a ella
    }

    // Metodos para reducir la vida del usuario y del rival
    private void reducirVidaUsuario(int damage) {
        DatabaseReference vidaUsuarioRef = gameRef.child(idUsuario).child("vida");
        vidaUsuarioRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Integer vidaActual = currentData.getValue(Integer.class);
                if (vidaActual == null) {
                    return Transaction.success(currentData);
                }
                int nuevaVida = vidaActual - damage;
                currentData.setValue(nuevaVida);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (committed) {
                    ProgressBar hpBarUsuario = findViewById(R.id.hpbar_usuario);
                    if(currentData != null){
                        hpBarUsuario.setProgress(currentData.getValue(Integer.class));
                    }
                }
            }
        });
    }

    private void reducirVidaRival(int damage) {
        DatabaseReference vidaRivalRef = gameRef.child(idRival).child("vida");
        vidaRivalRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Integer vidaActual = currentData.getValue(Integer.class);
                if (vidaActual == null) {
                    return Transaction.success(currentData);
                }
                int nuevaVida = vidaActual - damage;
                currentData.setValue(nuevaVida);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (committed) {
                    ProgressBar hpBarRival = findViewById(R.id.hpbar_rival);
                    hpBarRival.setProgress(currentData.getValue(Integer.class));
                }
            }
        });
    }

    private int calcularDañoRecibido(String palabra) {
        // Obtener el valor actual del daño del rival desde Firebase o donde sea que se mantenga
        DatabaseReference dañoRivalRef = gameRef.child(idRival).child("damage");
        AtomicReference<Integer> damageRival = new AtomicReference<>(0); // Valor inicial por si no hay datos
        dañoRivalRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer damage = snapshot.getValue(Integer.class);
                if (damage != null) {
                    damageRival.set(damage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar error
            }
        });
        // Devolver el valor del daño del rival como daño recibido por el usuario
        return damageRival.get();
    }

    private void comprobarPalabra(EditText editTextPalabraUsuario, TextView textViewPalabra, boolean esIngles) {
        String palabraUsuario = editTextPalabraUsuario.getText().toString();
        String palabraGenerada = palabraOriginal;
        if (palabraUsuario.equalsIgnoreCase(palabraGenerada)) {
            soundManager.playSound(JuegoMultijugadorActivity.this, 5);
            Animation scaleAnimation = AnimationUtils.loadAnimation(JuegoMultijugadorActivity.this, R.anim.scale_animation_success);
            editTextPalabraUsuario.startAnimation(scaleAnimation);
            editTextPalabraUsuario.setText("");
            int damage = new Random().nextInt(150) + 151; // Calcular el daño basado en la palabra
            aplicarDañoRival(damage); // Aplicar daño al rival
            // Calcular el daño recibido por el jugador actual
            int damageRecibido = calcularDañoRecibido(palabraGenerada);
            reducirVidaUsuario(damageRecibido); // Reducir la vida del jugador actual
            // Actualizar la interfaz de usuario
            actualizarBarraVidaUsuario(); // Asegurarse de que la barra de vida del usuario se actualice
            // Reducir la vida del rival
            verificarDañoRival(); // Ya no se necesita retornar el daño del rival, se maneja internamente
            generarDesordenarPalabra(esIngles);
        } else {
            soundManager.playSound(JuegoMultijugadorActivity.this, 6);
            Animation scaleAnimation = AnimationUtils.loadAnimation(JuegoMultijugadorActivity.this, R.anim.shake_animation);
            editTextPalabraUsuario.startAnimation(scaleAnimation);
        }
    }

    private void verificarDañoRival() {
        DatabaseReference dañoRivalRef = gameRef.child(idRival).child("damage");
        dañoRivalRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer damage = snapshot.getValue(Integer.class);
                if (damage != null && damage > 0) {
                    reducirVidaRival(damage); // Reducir la vida del rival
                    resetearDañoRival(); // Reiniciar el daño del rival después de usarlo
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar error
            }
        });
    }

    private void actualizarBarraVidaUsuario() {
        // Obtén una referencia a la barra de vida del usuario
        ProgressBar hpBarUsuario = findViewById(R.id.hpbar_usuario);

        // Obtiene la vida actual del usuario desde Firebase o desde donde sea que estés manteniendo los datos
        DatabaseReference vidaUsuarioRef = gameRef.child(idUsuario).child("vida");
        vidaUsuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer vidaActual = snapshot.getValue(Integer.class);
                if (vidaActual != null) {
                    // Actualiza la barra de vida con el valor actual
                    hpBarUsuario.setProgress(vidaActual);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Maneja el error
            }
        });
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
        Typeface customTypeface = ResourcesCompat.getFont(this, R.font.montserrat_semibold);
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
                params.setMargins(8, 8, 8, 8); // Ajustamos el margen
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
                gridTeclado.addView(button);
            }
        }
    }

    private void generarDesordenarPalabra(boolean esIngles) {
        palabraOriginal = dbHandler.generarPalabraAleatoria(esIngles);
        while (palabraOriginal.length() < 3 || palabraOriginal.length() > 6) {
            palabraOriginal = dbHandler.generarPalabraAleatoria(esIngles);
        }
        String palabraGenerada = desordenarPalabra(palabraOriginal);
        TextView textViewPalabra = findViewById(R.id.tv_palabra);
        textViewPalabra.setText(palabraGenerada);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Eliminar los listeners para evitar fugas de memoria
        if (vidaUsuarioListener != null) {
            gameRef.child(idUsuario).child("vida").removeEventListener(vidaUsuarioListener);
        }
        if (vidaRivalListener != null) {
            gameRef.child(idRival).child("vida").removeEventListener(vidaRivalListener);
        }
    }
}


