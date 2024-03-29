package com.example.proyecto_palabrasdesordenadas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class JuegoActivity extends AppCompatActivity {
    // Llamamos a la clase Sound Manager
    private SoundManager soundManager;
    private int vidasRestantes = 3;
    private int puntuacion = 0;
    private CountDownTimer countDownTimer;
    private String palabraOriginal = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String usuarioId = user.getUid();
    DBHandler dbHandler;
    private boolean imagenCambiada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        // Obtenemos la instancia de la base de datos
        dbHandler = new DBHandler(JuegoActivity.this);
        // Obtenemos los valores del idioma seleccionado, dificultad y modo de juego
        Intent intent = getIntent();
        imagenCambiada = intent.getBooleanExtra("imagenCambiada", false);
        String dificultadSeleccionada = intent.getStringExtra("dificultadSeleccionada");
        boolean modoContrarreloj = intent.getBooleanExtra("modoContrarreloj", false);
        boolean modoPuntuacion = intent.getBooleanExtra("modoPuntuacion", false);
        Intent stopIntent = new Intent(this, BackgroundSoundService.class);
        stopService(stopIntent);

        // Iniciar el servicio de nuevo con la nueva canción
        Intent startIntent = new Intent(this, BackgroundSoundService.class);
        startIntent.putExtra("song_selection", 2); //  2 para la canción "reloj"
        startService(startIntent);
        // Declaracion y asignacion de variables
        Button buttonComprobar = findViewById(R.id.btn_comprobar);
        Button buttonBorrar = findViewById(R.id.btn_borrar);
        if (!imagenCambiada) {
            buttonComprobar.setText(R.string.comprobar);
            buttonBorrar.setText(R.string.boton_borrar);
        } else {
            buttonComprobar.setText(R.string.button_check);
            buttonBorrar.setText(R.string.button_delete);
        }
        TextView textViewTimerScore = findViewById(R.id.tv_timerscore);
        ImageView imageViewVida1 = findViewById(R.id.img_vida);
        ImageView imageViewVida2 = findViewById(R.id.img_vida2);
        ImageView imageViewVida3 = findViewById(R.id.img_vida3);
        TextView textViewPalabra = findViewById(R.id.tv_palabra);
        TextView textViewCounter = findViewById(R.id.tv_counter);
        TextView textViewScore = findViewById(R.id.tv_score);
        TextView textViewPuntuacion = findViewById(R.id.tv_puntuacion);
        EditText editTextPalabraUsuario = findViewById(R.id.et_palabrausuario);
        soundManager = new SoundManager();
        soundManager.setVolume(MainActivity.volson);
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
            iniciarTemporizador(imagenCambiada, modoContrarreloj, modoPuntuacion);
        } else if (modoPuntuacion) {
            // Modo Puntuación: Mostramos la puntuación y las vidas, ocultamos el temporizador
            textViewPuntuacion.setVisibility(View.VISIBLE);
            imageViewVida1.setVisibility(View.VISIBLE);
            imageViewVida2.setVisibility(View.VISIBLE);
            imageViewVida3.setVisibility(View.VISIBLE);
            textViewTimerScore.setVisibility(View.GONE);
            textViewCounter.setVisibility(View.GONE);
        }
        switch (dificultadSeleccionada) {
            case "EASY":
            case "FÁCIL":
                do {
                    palabraOriginal = dbHandler.generarPalabraAleatoria(imagenCambiada);
                } while (palabraOriginal == null || palabraOriginal.length() < 3 || palabraOriginal.length() > 4);
                break;
            case "NORMAL":
            case "MEDIA":
                do {
                    palabraOriginal = dbHandler.generarPalabraAleatoria(imagenCambiada);
                } while (palabraOriginal == null || palabraOriginal.length() < 5 || palabraOriginal.length() > 8);
                break;
            case "HARD":
            case "DIFÍCIL":
                do {
                    palabraOriginal = dbHandler.generarPalabraAleatoria(imagenCambiada);
                } while (palabraOriginal == null || palabraOriginal.length() < 9);
                break;
        }
        String palabraDesordenada = desordenarPalabra(palabraOriginal);
        textViewPalabra.setText(palabraDesordenada);
        crearTeclado(gridTeclado, editTextPalabraUsuario);
        // Accion del boton de comprobar
        buttonComprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String palabraUsuario = editTextPalabraUsuario.getText().toString().trim().toLowerCase();
                if (!palabraUsuario.isEmpty()) {
                    if (palabraUsuario.equals(palabraOriginal.toLowerCase())) {
                        soundManager.playSound(JuegoActivity.this, 5);
                        if (!imagenCambiada) {
                            Animation scaleAnimation = AnimationUtils.loadAnimation(JuegoActivity.this, R.anim.scale_animation_success);
                            editTextPalabraUsuario.startAnimation(scaleAnimation);
                        } else {
                            Animation scaleAnimation = AnimationUtils.loadAnimation(JuegoActivity.this, R.anim.scale_animation_success);
                            editTextPalabraUsuario.startAnimation(scaleAnimation);
                        }
                        do {
                            palabraUsuario = (String) dbHandler.generarPalabraAleatoria(imagenCambiada);
                        } while (!verificarLongitudPalabra(palabraUsuario, dificultadSeleccionada));
                        registrarPalabraFormada(usuarioId, palabraUsuario);
                        if (modoPuntuacion) {
                            Random random = new Random();
                            int puntuacionAleatoria = random.nextInt(11) + 15;
                            puntuacion += puntuacionAleatoria;
                            textViewScore.setText(String.valueOf(puntuacion));
                        } else {
                            puntuacion++;
                            textViewCounter.setText(String.valueOf(puntuacion));
                        }
                        palabraOriginal = palabraUsuario;
                        String nuevaPalabraDesordenada = desordenarPalabra(palabraUsuario);
                        textViewPalabra.setText(nuevaPalabraDesordenada);
                        editTextPalabraUsuario.setText("");
                    } else {
                        soundManager.playSound(JuegoActivity.this, 6);
                        if (!imagenCambiada) {
                            Animation shakeAnimation = AnimationUtils.loadAnimation(JuegoActivity.this, R.anim.shake_animation);
                            editTextPalabraUsuario.startAnimation(shakeAnimation);
                        } else {
                            Animation shakeAnimation = AnimationUtils.loadAnimation(JuegoActivity.this, R.anim.shake_animation);
                            editTextPalabraUsuario.startAnimation(shakeAnimation);
                        }
                        vidasRestantes--;
                        switch (vidasRestantes) {
                            case 2:
                                imageViewVida1.setImageResource(R.drawable.nlife);
                                break;
                            case 1:
                                imageViewVida2.setImageResource(R.drawable.nlife);
                                break;
                            case 0:
                                imageViewVida3.setImageResource(R.drawable.nlife);
                                terminarJuego(imagenCambiada, puntuacion, modoContrarreloj, modoPuntuacion);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        });
        // Accion del boton de borrar
        buttonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPalabraUsuario.setText("");
            }
        });
    }
    // Metodo para mostrar un dialogo cuando el juego termina
    private void mostrarDialogoJuegoTerminado() {
        // Crear el Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(JuegoActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_game_over, null);
        builder.setView(dialogView);
        // Configurar el mensaje del Dialog
        TextView dialogTitle = dialogView.findViewById(R.id.tv_dialog_titulo);
        if (!imagenCambiada) {
            dialogTitle.setText("JUEGO TERMINADO");
        } else {
            dialogTitle.setText("GAME OVER");
        }
        TextView dialogMessage = dialogView.findViewById(R.id.tv_dialog_mensaje);
        if(!imagenCambiada) {
            dialogMessage.setText("Se agotaron las vidas del juego");
        } else {
            dialogMessage.setText("There are no more lifes left");
        }
        // Configurar el botón del Dialog
        Button dialogButton = dialogView.findViewById(R.id.btn_volvermenu);
        if(!imagenCambiada){
            dialogButton.setText("VOLVER AL MENU PRINCIPAL");
        } else {
            dialogButton.setText("BACK TO MAIN MENU");
        }
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JuegoActivity.this, PartidaActivity.class));
            }
        });

        // Mostrar el Dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Metodo para iniciar el temporizador, en el modo contrarreloj
    private void iniciarTemporizador(boolean imagenCambiada, boolean modoContrarreloj, boolean modoPuntuacion) {
        TextView textViewTimer = findViewById(R.id.tv_timerscore);
        countDownTimer = new CountDownTimer(90000, 1000) {
            @Override
            public void onTick(long l) {
                textViewTimer.setText(""+l / 1000);
            }

            @Override
            public void onFinish() {
                terminarJuego(imagenCambiada,puntuacion,modoContrarreloj,modoPuntuacion);
            }
        };
        textViewTimer.setTextSize(32);
        textViewTimer.setTextColor(Color.parseColor("#EFB810"));
        Typeface customTypeface = ResourcesCompat.getFont(this, R.font.montserrat_semibold);
        textViewTimer.setTypeface(customTypeface);
        countDownTimer.start();
    }
    // Metodo para verificar la longitud de la palabra
    private boolean verificarLongitudPalabra(String palabra, String dificultad) {
        switch (dificultad) {
            case "EASY":
            case "FÁCIL":
                return palabra.length() >= 3 && palabra.length() <= 4;
            case "NORMAL":
            case "MEDIA":
                return palabra.length() >= 5 && palabra.length() <= 8;
            case "HARD":
            case "DÍFICIL":
                return palabra.length() >= 9;
            default:
                return false;
        }
    }
    // Metodo para terminar el juego
    private void terminarJuego(boolean imagenCambiada, int puntuacion, boolean modoContrarreloj, boolean modoPuntuacion) {
        // Verificar si el temporizador es null antes de cancelarlo
        if(!imagenCambiada){
            mostrarDialogoJuegoTerminado();
        } else {
            mostrarDialogoJuegoTerminado();
        }
        Intent stopIntent = new Intent(this, BackgroundSoundService.class);
        stopService(stopIntent);
        Intent startIntent = new Intent(this, BackgroundSoundService.class);
        startIntent.putExtra("song_selection",  1); //  1 para la canción "music"
        startService(startIntent);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        String modo = "";
        if(modoContrarreloj){
            modo = "modoContrarreloj";
        } else {
            modo = "modoPuntuacion";
        }
        registrarPartida(usuarioId,modo,puntuacion,vidasRestantes);
        verificarEstrategaVerbal(usuarioId);
        verificarExploradorDeIdiomas(usuarioId, imagenCambiada);
        verificarLinguisticoIntrepido(usuarioId);
        verificarTrofeoLuchadorDeLetras(usuarioId);
        verificarTrofeoMaestroDeLaPalabra(usuarioId);
        verificarTrofeoRelojMaestro(usuarioId);
        verificarTrofeoSprinterVerbal(usuarioId);
        verificarTrofeoSupervivienteLinguistico(usuarioId);
        verificarTrofeoTornadoDeLetras(usuarioId);
    }

    // Metodo para desordenar las palabras
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
                        soundManager.playSound(JuegoActivity.this, MainActivity.sonido);
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
    // Metodo para registrar las partidas
    private void registrarPartida(String usuarioId, String modo, int puntuacion, int vidasRestantes){
        DocumentReference usuarioRef = db.collection("usuarios").document(usuarioId);
        Map<String, Object> partida = new HashMap<>();
        partida.put("fechaPartida", new Date());
        partida.put("modoDeJuego", modo);
        partida.put("puntuacion", puntuacion);
        partida.put("vidasRestantes", vidasRestantes);
        partida.put("idPartida", UUID.randomUUID().toString());

        usuarioRef.update("partidas", FieldValue.arrayUnion(partida))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("PartidaRegistrada", "Partida registrada con éxito");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("PartidaRegistrada", "Error al registrar la partida", e);
                    }
                });
    }
    // Metodo para verificar el trofeo de Estratega Verbal
    private void verificarEstrategaVerbal(String usuarioId){
        if(puntuacion >=  5000){
            ganarTrofeo(usuarioId, "estrategaVerbal").addOnCompleteListener(new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(@NonNull Task<Boolean> task) {
                    if (task.isSuccessful() && task.getResult()) {
                        mostrarSnackbarPersonalizado("¡Has ganado el trofeo Estratega Verbal!");
                    }
                }
            });
        }
    }
    // Metodo para verificar el trofeo de Explorador de Idiomas
    private void verificarExploradorDeIdiomas(String usuarioId, boolean idioma){
        if(idioma){
            ganarTrofeo(usuarioId, "exploradorDeIdiomas").addOnCompleteListener(new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(@NonNull Task<Boolean> task) {
                    if (task.isSuccessful() && task.getResult()) {
                        mostrarSnackbarPersonalizado("¡Has ganado el trofeo Explorador de Idiomas!");
                    }
                }
            });
        }
    }
    // Metodo para verificar el trofeo de Linguistico Intrepido
    private void verificarLinguisticoIntrepido(String usuarioId){
        DocumentReference usuarioRef = db.collection("usuarios").document(usuarioId);
        usuarioRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        List<Map<String, Object>> partidas = (List<Map<String, Object>>) document.get("partidas");
                        if(partidas != null){
                            // Ordenar las partidas por fecha en orden ascendente
                            partidas.sort(Comparator.comparing(partida -> ((Timestamp) partida.get("fechaPartida")).toDate()));

                            // Verificar si el usuario ha jugado partidas durante  7 días seguidos
                            boolean jugado7DiasSeguidos = verificarJugado7DiasSeguidos(partidas);

                            if (jugado7DiasSeguidos) {
                                // Aquí puedes otorgar el trofeo al usuario
                                ganarTrofeo(usuarioId, "linguisticoIntrepido").addOnCompleteListener(new OnCompleteListener<Boolean>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Boolean> task) {
                                        if (task.isSuccessful() && task.getResult()) {
                                            mostrarSnackbarPersonalizado("¡Has ganado el trofeo Linguistico Intrepido!");
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        Log.d("TrofeoSieteDias", "No se encontró el documento del usuario");
                    }
                } else {
                    Log.d("TrofeoSieteDias", "Error al obtener el documento del usuario", task.getException());
                }
            }
        });
    }

    private boolean verificarJugado7DiasSeguidos(List<Map<String, Object>> partidas) {
        if (partidas.size() <  7) {
            return false; // No hay suficientes partidas para verificar  7 días seguidos
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(((Timestamp) partidas.get(0).get("fechaPartida")).toDate());
        int diasJugados =  0;

        for (int i =  1; i < partidas.size(); i++) {
            Date fechaPartida = ((Timestamp) partidas.get(i).get("fechaPartida")).toDate();
            cal.add(Calendar.DAY_OF_MONTH,  1); // Avanzar un día
            if (cal.getTime().equals(fechaPartida)) {
                diasJugados++;
                if (diasJugados ==  7) {
                    return true; // Se han jugado  7 días seguidos
                }
            } else {
                diasJugados =  0; // Reiniciar el conteo de días jugados
            }
        }

        return false; // No se han jugado  7 días seguidos
    }

    // Metodo para verificar el trofeo Luchador de Letras
    private void registrarPalabraFormada(String usuarioId, String palabra) {
        DocumentReference usuarioRef = db.collection("usuarios").document(usuarioId);
        Map<String, Object> palabraFormada = new HashMap<>();
        palabraFormada.put("fechaPartida", new Date());
        palabraFormada.put("palabra", palabra);

        usuarioRef.update("palabrasFormadas", FieldValue.arrayUnion(palabraFormada))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("PalabraFormada", "Palabra formada registrada con éxito");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("PalabraFormada", "Error al registrar la palabra formada", e);
                    }
                });
    }

    private void verificarTrofeoLuchadorDeLetras(String usuarioId) {
        DocumentReference usuarioRef = db.collection("usuarios").document(usuarioId);
        usuarioRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> palabrasFormadas = (List<Map<String, Object>>) document.get("palabrasFormadas");
                        if (palabrasFormadas != null) {
                            // Verificar si el usuario ha formado una palabra de al menos  9 letras
                            boolean calificaParaTrofeo = palabrasFormadas.stream()
                                    .anyMatch(palabraFormada -> ((String) palabraFormada.get("palabra")).length() >=  9);

                            if (calificaParaTrofeo) {
                                ganarTrofeo(usuarioId, "luchadorDeLetras").addOnCompleteListener(new OnCompleteListener<Boolean>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Boolean> task) {
                                        if (task.isSuccessful() && task.getResult()) {
                                            mostrarSnackbarPersonalizado("¡Has ganado el trofeo Luchador de Letras!");
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        Log.d("TrofeoLuchadorDeLetras", "No se encontró el documento del usuario");
                    }
                } else {
                    Log.d("TrofeoLuchadorDeLetras", "Error al obtener el documento del usuario", task.getException());
                }
            }
        });
    }
    // Metodo para verificar trofeo Maestro de la Palabra
    private void verificarTrofeoMaestroDeLaPalabra(String usuarioId){
        DocumentReference usuarioRef = db.collection("usuarios").document(usuarioId);
        usuarioRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        List<String> trofeosGanados = (List<String>) document.get("trofeos");
                        if(trofeosGanados != null){
                            db.collection("trofeos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        List<String> todosLosTrofeos = new ArrayList<>();
                                        for(QueryDocumentSnapshot trofeo : task.getResult()){
                                            todosLosTrofeos.add(trofeo.getId());
                                        }
                                        boolean todosLosTrofeosConseguidos = trofeosGanados.containsAll(todosLosTrofeos);
                                        if(todosLosTrofeosConseguidos){
                                            ganarTrofeo(usuarioId, "maestroDeLaPalabra").addOnCompleteListener(new OnCompleteListener<Boolean>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Boolean> task) {
                                                    if (task.isSuccessful() && task.getResult()) {
                                                        mostrarSnackbarPersonalizado("!Has ganado el trofeo Maestro de la Palabra¡");
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        Log.d("TrofeoMaestroDeLaPalabra", "Error al obtener los trofeos disponibles", task.getException());
                                    }
                                }
                            });
                        }
                    } else {
                        Log.d("TrofeoMaestroDeLaPalabra", "No se encontró el documento del usuario");
                    }
                } else {
                    Log.d("TrofeoMaestroDeLaPalabra", "Error al obtener el documento del usuario", task.getException());
                }
            }
        });
    }
    // Metodo para verificar trofeo Reloj Maestro
    private void verificarTrofeoRelojMaestro(String usuarioId) {
        DocumentReference usuarioRef = db.collection("usuarios").document(usuarioId);
        usuarioRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> partidas = (List<Map<String, Object>>) document.get("partidas");
                        if (partidas != null) {
                            // Filtrar partidas en modo contrarreloj sin fallos
                            List<Map<String, Object>> partidasContrarrelojSinFallos = partidas.stream()
                                    .filter(partida -> {
                                        Object modoDeJuego = partida.get("modoDeJuego");
                                        Object fallos = partida.get("fallos");
                                        // Asegúrate de que ambos objetos no sean null antes de realizar la comparación
                                        return modoDeJuego != null && "modoContrarreloj".equals(modoDeJuego) && fallos != null && (int) fallos ==  0;
                                    })
                                    .collect(Collectors.toList());

                            // Verificar si el usuario ha completado  10 niveles contrarreloj sin fallar ninguna palabra
                            boolean calificaParaTrofeo = partidasContrarrelojSinFallos.size() >=  10;

                            if (calificaParaTrofeo) {
                                ganarTrofeo(usuarioId, "relojMaestro").addOnCompleteListener(new OnCompleteListener<Boolean>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Boolean> task) {
                                        if (task.isSuccessful() && task.getResult()) {
                                            mostrarSnackbarPersonalizado("¡Has ganado el trofeo Reloj Maestro!");
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        Log.d("TrofeoRelojMaestro", "No se encontró el documento del usuario");
                    }
                } else {
                    Log.d("TrofeoRelojMaestro", "Error al obtener el documento del usuario", task.getException());
                }
            }
        });
    }

    // Metodo para verificar el trofeo Sprinter Verbal
    private void verificarTrofeoSprinterVerbal(String usuarioId) {
        DocumentReference usuarioRef = db.collection("usuarios").document(usuarioId);
        usuarioRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> partidas = (List<Map<String, Object>>) document.get("partidas");
                        if (partidas != null) {
                            // Filtrar partidas en modo contrarreloj con puntuación de al menos   80
                            List<Map<String, Object>> partidasContrarrelojConPuntuacionAlta = partidas.stream()
                                    .filter(partida -> {
                                        Object modoDeJuego = partida.get("modoDeJuego");
                                        Object puntuacion = partida.get("puntuacion");
                                        // Asegúrate de que ambos objetos no sean null antes de realizar la comparación
                                        // Convierte puntuacion a Long y luego a int para la comparación
                                        return modoDeJuego != null && "modoContrarreloj".equals(modoDeJuego) && puntuacion != null && ((Long) puntuacion).intValue() >=   80;
                                    })
                                    .collect(Collectors.toList());

                            // Verificar si el usuario ha alcanzado la puntuación de al menos   80 en un nivel contrarreloj
                            boolean calificaParaTrofeo = !partidasContrarrelojConPuntuacionAlta.isEmpty();

                            if (calificaParaTrofeo) {
                                ganarTrofeo(usuarioId, "sprinterVerbal").addOnCompleteListener(new OnCompleteListener<Boolean>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Boolean> task) {
                                        if (task.isSuccessful() && task.getResult()) {
                                            mostrarSnackbarPersonalizado("¡Has ganado el trofeo Sprinter Verbal!");
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        Log.d("TrofeoSprinterVerbal", "No se encontró el documento del usuario");
                    }
                } else {
                    Log.d("TrofeoSprinterVerbal", "Error al obtener el documento del usuario", task.getException());
                }
            }
        });
    }
    // Metodo para verificar el trofeo de Superviviente Linguistico
    private void verificarTrofeoSupervivienteLinguistico(String usuarioId) {
        DocumentReference usuarioRef = db.collection("usuarios").document(usuarioId);
        usuarioRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> partidas = (List<Map<String, Object>>) document.get("partidas");
                        if (partidas != null) {
                            // Filtrar partidas en modo puntuación
                            List<Map<String, Object>> partidasModoPuntuacion = partidas.stream()
                                    .filter(partida -> "modoPuntuacion".equals(partida.get("modoDeJuego")))
                                    .collect(Collectors.toList());

                            // Verificar si el usuario ha jugado al menos  10 partidas en modo puntuación
                            boolean calificaParaTrofeo = partidasModoPuntuacion.size() >=   10;

                            if (calificaParaTrofeo) {
                                ganarTrofeo(usuarioId, "supervivienteLinguistico").addOnCompleteListener(new OnCompleteListener<Boolean>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Boolean> task) {
                                        if (task.isSuccessful() && task.getResult()) {
                                            mostrarSnackbarPersonalizado("¡Has ganado el trofeo Superviviente Linguístico!");
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        Log.d("TrofeoSupervivienteLinguistico", "No se encontró el documento del usuario");
                    }
                } else {
                    Log.d("TrofeoSupervivienteLinguistico", "Error al obtener el documento del usuario", task.getException());
                }
            }
        });
    }
    // Metodo para verificar el trofeo Tornado de Letras
    private void verificarTrofeoTornadoDeLetras(String usuarioId){
        DocumentReference usuarioRef = db.collection("usuarios").document(usuarioId);
        usuarioRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> palabrasFormadas = (List<Map<String, Object>>) document.get("palabrasFormadas");
                        if (palabrasFormadas != null) {
                            // Verificar si el usuario ha formado una palabra de al menos  9 letras
                            boolean calificaParaTrofeo = palabrasFormadas.stream()
                                    .anyMatch(palabraFormada -> ((String) palabraFormada.get("palabra")).length() >=  7);

                            if (calificaParaTrofeo) {
                                ganarTrofeo(usuarioId, "tornadoDeLetras").addOnCompleteListener(new OnCompleteListener<Boolean>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Boolean> task) {
                                        if (task.isSuccessful() && task.getResult()) {
                                            mostrarSnackbarPersonalizado("¡Has ganado el trofeo Tornado de Letras!");
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        Log.d("TrofeoTornadoDeLetras", "No se encontró el documento del usuario");
                    }
                } else {
                    Log.d("TrofeoTornadoDeLetras", "Error al obtener el documento del usuario", task.getException());
                }
            }
        });
    }
    // Metodo para actualizar los trofeos ganados en Firebase
    private Task<Boolean> ganarTrofeo(String usuarioId, String trofeoId) {
        DocumentReference usuarioRef = db.collection("usuarios").document(usuarioId);
        return usuarioRef.get().continueWithTask(new Continuation<DocumentSnapshot, Task<Boolean>>() {
            @Override
            public Task<Boolean> then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> trofeosGanados = (List<String>) document.get("trofeos");
                        if (trofeosGanados != null && !trofeosGanados.contains(trofeoId)) {
                            // Si el trofeo no está en la lista, lo agregamos
                            return usuarioRef.update("trofeos", FieldValue.arrayUnion(trofeoId))
                                    .continueWith(new Continuation<Void, Boolean>() {
                                        @Override
                                        public Boolean then(@NonNull Task<Void> task) throws Exception {
                                            if (task.isSuccessful()) {
                                                return true; // Trofeo ganado
                                            } else {
                                                Log.w("TrofeoGanado", "Error al actualizar el trofeo ganado", task.getException());
                                                return false; // Error al ganar el trofeo
                                            }
                                        }
                                    });
                        } else {
                            // Si el trofeo ya está en la lista, no hacemos nada
                            Log.d("TrofeoGanado", "El usuario ya ha ganado este trofeo");
                            return Tasks.forResult(false); // Trofeo ya ganado
                        }
                    } else {
                        Log.d("TrofeoGanado", "No se encontró el documento del usuario");
                        return Tasks.forResult(false); // Documento de usuario no encontrado
                    }
                } else {
                    Log.d("TrofeoGanado", "Error al obtener el documento del usuario", task.getException());
                    return Tasks.forResult(false); // Error al obtener el documento
                }
            }
        });
    }

    private void mostrarSnackbarPersonalizado(String mensaje) {
        // Obtén el CoordinatorLayout o cualquier otro ViewGroup que estés usando como contenedor principal
        ConstraintLayout constraintLayout = findViewById(R.id.container_juego);

        // Infla el layout personalizado
        View snackbarView = LayoutInflater.from(this).inflate(R.layout.custom_snackbar, null);
        TextView snackbarText = snackbarView.findViewById(R.id.snackbar_text);
        LottieAnimationView lottieAnimationView = snackbarView.findViewById(R.id.lottie_animation);

        // Configura el mensaje y la animación
        snackbarText.setText(mensaje);
        lottieAnimationView.setAnimation(R.raw.confeti);
        lottieAnimationView.playAnimation();

        // Crea el Snackbar personalizado
        Snackbar snackbar = Snackbar.make(constraintLayout, "", Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT); // Hace que el fondo del Snackbar sea transparente
        snackbar.setAction("OK", null); // Establece el texto del botón de acción y no asigna un OnClickListener
        snackbar.setActionTextColor(Color.WHITE); // Cambia el color del texto del botón "OK"

        // Reemplaza el contenido del Snackbar con el layout personalizado
        snackbar.setAnchorView(snackbarView);

        // Muestra el Snackbar
        snackbar.show();
    }



}