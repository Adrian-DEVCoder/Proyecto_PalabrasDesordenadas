package com.example.proyecto_palabrasdesordenadas;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class TrophyService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Trofeo estrategaVerbal = new Trofeo("Estratega Verbal","estratega_verbal","Otorgado por acumular 5000 puntos en una sola partida en modo puntuación");
        Trofeo exploradorDeIdiomas = new Trofeo("Explorador de Idiomas","explorador_de_idiomas","Otorgado por completar una partida con el juego en inglés");
        Trofeo linguisticoIntrepido = new Trofeo("Lingüistico Intrépido","linguistico_intrepido","Otorgado por jugar durante 7 días consecutivos");
        Trofeo luchadorDeLetras = new Trofeo("Luchador de Letras","luchador_de_letras","Otorgado por formar una palabra de al menos 9 letras en modo puntuación");
        Trofeo maestroDeLaPalabra = new Trofeo("Maestro de la Palabra","maestro_de_la_palabra","Otorgado por desbloquear todos los trofeos del juego");
        Trofeo relojMaestro = new Trofeo("Reloj Maestro","reloj_maestro","Otorgado por completar 10 niveles contrarreloj sin fallar ninguna palabra");
        Trofeo sprinterVerbal = new Trofeo("Sprinter Verbal","sprinter_verbal","Otorgado por alcanzar una puntuacion de 80 en un solo nivel contrarreloj");
        Trofeo supervivienteLinguistico = new Trofeo("Superviviente Lingüistico","superviviente_linguistico","Otorgado por jugar al menos 10 partidas en modo por puntuación");
        Trofeo tornadoDeLetras = new Trofeo("Tornado de Letras","tornado_de_letras","Otorgado por formar palabras con más de 7 letras en un solo nivel");

        db.collection("trofeos").document("estrategaVerbal").set(estrategaVerbal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"Documento creado satisfactoriamente.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Error al escribir el documento.");
                    }
                });
        db.collection("trofeos").document("exploradorDeIdiomas").set(exploradorDeIdiomas)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"Documento creado satisfactoriamente.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Error al escribir el documento.");
                    }
                });
        db.collection("trofeos").document("linguisticoIntrepido").set(linguisticoIntrepido)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"Documento creado satisfactoriamente.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Error al escribir el documento.");
                    }
                });
        db.collection("trofeos").document("luchadorDeLetras").set(luchadorDeLetras)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"Documento creado satisfactoriamente.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Error al escribir el documento.");
                    }
                });
        db.collection("trofeos").document("maestroDeLaPalabra").set(maestroDeLaPalabra)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"Documento creado satisfactoriamente.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Error al escribir el documento.");
                    }
                });
        db.collection("trofeos").document("relojMaestro").set(relojMaestro)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"Documento creado satisfactoriamente.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Error al escribir el documento.");
                    }
                });
        db.collection("trofeos").document("sprinterVerbal").set(sprinterVerbal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"Documento creado satisfactoriamente.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Error al escribir el documento.");
                    }
                });
        db.collection("trofeos").document("supervivienteLinguistico").set(supervivienteLinguistico)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"Documento creado satisfactoriamente.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Error al escribir el documento.");
                    }
                });
        db.collection("trofeos").document("tornadoDeLetras").set(tornadoDeLetras)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"Documento creado satisfactoriamente.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Error al escribir el documento.");
                    }
                });
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
