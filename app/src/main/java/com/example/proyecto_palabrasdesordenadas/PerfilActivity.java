package com.example.proyecto_palabrasdesordenadas;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        TextView textViewLogros = findViewById(R.id.tv_logros);
        textViewLogros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    configurarTrofeos(trofeosMap);
                } else {
                    Log.w(TAG, "Error al obtener los trofeos.", task.getException());
                }
            }
        });
    }

    private void configurarTrofeos(Map<String, Trofeo> trofeosMap) {
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

        imgTrofeo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "estrategaVerbal";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo);
            }
        });
        imgTrofeo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "exploradorDeIdiomas";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo);
            }
        });
        imgTrofeo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "linguisticoIntrepido";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo);
            }
        });
        imgTrofeo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "luchadorDeLetras";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo);
            }
        });
        imgTrofeo5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "maestroDeLaPalabra";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo);
            }
        });
        imgTrofeo6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "relojMaestro";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo);
            }
        });
        imgTrofeo7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "sprinterVerbal";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo);
            }
        });
        imgTrofeo8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "supervivienteLinguistico";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo);
            }
        });
        imgTrofeo9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trofeoId = "tornadoDeLetras";
                Trofeo trofeo = trofeosMap.get(trofeoId);
                mostrarDialogoDetallesTrofeo(trofeo);
            }
        });
    }

    private void mostrarDialogoDetallesTrofeo(Trofeo trofeo){
        // Inflar el layout del diálogo
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_perfil, null);
        // Configurar los elementos del diálogo
        TextView tvTitulo = dialogLayout.findViewById(R.id.tv_titulo);
        TextView tvDescripcion = dialogLayout.findViewById(R.id.tv_descripcion);
        ImageView imageViewTrofeo = dialogLayout.findViewById(R.id.imageView);
        tvTitulo.setText(trofeo.getTitulo());
        tvDescripcion.setText(trofeo.getDescripcion());
        // Obtener el ID del recurso
        String resourceName = trofeo.getRecurso();
        int imageResourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
        imageViewTrofeo.setImageResource(imageResourceId);
        // Crear el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogLayout);
        // Mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
