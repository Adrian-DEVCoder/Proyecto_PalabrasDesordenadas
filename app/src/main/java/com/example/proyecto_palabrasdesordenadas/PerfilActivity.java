package com.example.proyecto_palabrasdesordenadas;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {
    private SoundManager soundManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        ImageView imageViewLogo = findViewById(R.id.img_logo2);
        soundManager = new SoundManager();
        soundManager.setVolume(MainActivity.volson);
        imageViewLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(PerfilActivity.this, MainActivity.sonido);
                startActivity(new Intent(PerfilActivity.this, MenuActivity.class));
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trofeos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Trofeo> trofeosMap = new HashMap<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Trofeo trofeo = document.toObject(Trofeo.class);
                        trofeosMap.put(document.getId(), trofeo);
                    }
                    obtenerTrofeosDesbloqueados(db,trofeosMap);
                } else {
                    Log.w(TAG, "Error al obtener los trofeos.", task.getException());
                }
            }
        });
    }
    private void obtenerTrofeosDesbloqueados(FirebaseFirestore db, Map<String, Trofeo> trofeosMap){
        String usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("usuarios").document(usuarioId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documento = task.getResult();
                    if(documento.exists()){
                        List<String> trofeosDesbloqueados = (List<String>) documento.get("trofeos");
                        configurarTrofeos(trofeosMap, trofeosDesbloqueados); // Asegúrate de pasar trofeosMap aquí también
                    } else {
                        Log.d(TAG, "No se encontro el documento del usuario");
                    }
                } else {
                    Log.d(TAG, "Error al obtener el documento del usuario"+task.getException());
                }
            }
        });
    }

    private void configurarTrofeos(Map<String, Trofeo> trofeosMap, List<String> trofeosDesbloqueados) {

        // Obtener todos los ImageView de trofeos
        ImageView imgTrofeo1 = findViewById(R.id.img_trofeo);
        ImageView imgTrofeo2 = findViewById(R.id.img_trofeo_2);
        ImageView imgTrofeo3 = findViewById(R.id.img_trofeo_3);
        ImageView imgTrofeo4 = findViewById(R.id.img_trofeo_4);
        ImageView imgTrofeo5 = findViewById(R.id.img_trofeo_5);
        ImageView imgTrofeo6 = findViewById(R.id.img_trofeo_6);
        ImageView imgTrofeo7 = findViewById(R.id.img_trofeo_7);
        ImageView imgTrofeo8 = findViewById(R.id.img_trofeo_8);
        ImageView imgTrofeo9 = findViewById(R.id.img_trofeo_9);

        boolean trofeoDesbloqueadoUno = trofeosDesbloqueados.contains("estrategaVerbal");
        if(trofeoDesbloqueadoUno){
            imgTrofeo1.setImageResource(R.drawable.trophy);
        } else{
            imgTrofeo1.setImageResource(R.drawable.trophy_locked);
        }
        boolean trofeoDesbloqueadoDos = trofeosDesbloqueados.contains("exploradorDeIdiomas");
        if(trofeoDesbloqueadoDos){
            imgTrofeo2.setImageResource(R.drawable.trophy);
        } else{
            imgTrofeo2.setImageResource(R.drawable.trophy_locked);
        }
        boolean trofeoDesbloqueadoTres = trofeosDesbloqueados.contains("linguisticoIntrepido");
        if(trofeoDesbloqueadoTres){
            imgTrofeo3.setImageResource(R.drawable.trophy);
        } else{
            imgTrofeo3.setImageResource(R.drawable.trophy_locked);
        }
        boolean trofeoDesbloqueadoCuatro = trofeosDesbloqueados.contains("luchadorDeLetras");
        if(trofeoDesbloqueadoCuatro){
            imgTrofeo4.setImageResource(R.drawable.trophy);
        } else{
            imgTrofeo4.setImageResource(R.drawable.trophy_locked);
        }
        boolean trofeoDesbloqueadoCinco = trofeosDesbloqueados.contains("maestroDeLaPalabra");
        if(trofeoDesbloqueadoCinco){
            imgTrofeo5.setImageResource(R.drawable.trophy);
        } else{
            imgTrofeo5.setImageResource(R.drawable.trophy_locked);
        }
        boolean trofeoDesbloqueadoSeis = trofeosDesbloqueados.contains("relojMaestro");
        if(trofeoDesbloqueadoSeis){
            imgTrofeo6.setImageResource(R.drawable.trophy);
        } else{
            imgTrofeo6.setImageResource(R.drawable.trophy_locked);
        }
        boolean trofeoDesbloqueadoSiete = trofeosDesbloqueados.contains("sprinterVerbal");
        if(trofeoDesbloqueadoSiete){
            imgTrofeo7.setImageResource(R.drawable.trophy);
        } else{
            imgTrofeo7.setImageResource(R.drawable.trophy_locked);
        }
        boolean trofeoDesbloqueadoOcho = trofeosDesbloqueados.contains("supervivienteLinguistico");
        if(trofeoDesbloqueadoOcho){
            imgTrofeo8.setImageResource(R.drawable.trophy);
        } else{
            imgTrofeo8.setImageResource(R.drawable.trophy_locked);
        }
        boolean trofeoDesbloqueadoNueve = trofeosDesbloqueados.contains("tornadoDeLetras");
        if(trofeoDesbloqueadoNueve){
            imgTrofeo9.setImageResource(R.drawable.trophy);
        } else{
            imgTrofeo9.setImageResource(R.drawable.trophy_locked);
        }
        imgTrofeo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "estrategaVerbal";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo, trofeoId, trofeosDesbloqueados);
            }
        });
        imgTrofeo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "exploradorDeIdiomas";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo, trofeoId, trofeosDesbloqueados);
            }
        });
        imgTrofeo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "linguisticoIntrepido";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo, trofeoId, trofeosDesbloqueados);
            }
        });
        imgTrofeo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "luchadorDeLetras";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo, trofeoId, trofeosDesbloqueados);
            }
        });
        imgTrofeo5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "maestroDeLaPalabra";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo, trofeoId, trofeosDesbloqueados);
            }
        });
        imgTrofeo6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "relojMaestro";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo, trofeoId, trofeosDesbloqueados);
            }
        });
        imgTrofeo7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "sprinterVerbal";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo, trofeoId, trofeosDesbloqueados);
            }
        });
        imgTrofeo8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "supervivienteLinguistico";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo, trofeoId, trofeosDesbloqueados);
            }
        });
        imgTrofeo9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "tornadoDeLetras";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo, trofeoId, trofeosDesbloqueados);
            }
        });
    }

    private void mostrarDialogoDetallesTrofeo(Trofeo trofeo,String trofeoId, List<String> trofeosDesbloqueados) {
        // Verificar si el trofeo está desbloqueado
        boolean trofeoDesbloqueado = trofeosDesbloqueados.contains(trofeoId);
        // Inflar el layout del diálogo
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout;
        if (trofeoDesbloqueado) {
            dialogLayout = inflater.inflate(R.layout.dialog_perfil, null);
        } else {
            dialogLayout = inflater.inflate(R.layout.dialog_logro_bloqueado, null);
        }
        // Configurar los elementos del diálogo
        TextView tvTitulo = dialogLayout.findViewById(R.id.tv_titulo);
        TextView tvDescripcion = dialogLayout.findViewById(R.id.tv_descripcion);
        ImageView imageViewTrofeo = dialogLayout.findViewById(R.id.imageView);
        if (trofeoDesbloqueado) {
            tvTitulo.setText(trofeo.getTitulo());
            tvDescripcion.setText(trofeo.getDescripcion());
            // Obtener el ID del recurso
            String resourceName = trofeo.getRecurso();
            int imageResourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
            imageViewTrofeo.setImageResource(imageResourceId);
        } else {
            // Configurar el título y la descripción para el diálogo de trofeo bloqueado
            tvTitulo.setText("¿?");
            tvDescripcion.setText(trofeo.getDescripcion());
        }
        // Crear el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogLayout);
        // Mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
