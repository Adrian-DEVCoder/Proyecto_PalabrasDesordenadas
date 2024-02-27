package com.example.proyecto_palabrasdesordenadas;

import static androidx.constraintlayout.widget.StateSet.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MultijugadorActivity extends AppCompatActivity {
    private EditText editTextNombreSala;
    private EditText editTextContrasena;
    private Button buttonCrearSala;
    private Button buttonUnirseSala;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multijugador);
        ImageView imageViewLogo = findViewById(R.id.img_logo2);
        TextView textViewNombreSala = findViewById(R.id.tv_nombresala);
        editTextNombreSala = findViewById(R.id.edt_nombresala);
        TextView textViewContrasena = findViewById(R.id.tv_contrasenasala);
        editTextContrasena = findViewById(R.id.edt_contrasena);
        buttonCrearSala = findViewById(R.id.btn_crearsala);
        buttonUnirseSala = findViewById(R.id.btn_unirse);

        imageViewLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MultijugadorActivity.this, MenuActivity.class));
            }
        });
        buttonCrearSala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearSala();
            }
        });
        buttonUnirseSala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unirseSala();
            }
        });
    }

    private void crearSala(){
        String nombreSala = editTextNombreSala.getText().toString();
        String contrasenaSala = editTextContrasena.getText().toString();

        if(!nombreSala.isEmpty() && !contrasenaSala.isEmpty()){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            String key = databaseReference.child("salas").push().getKey();
            databaseReference.child("salas").child(key).setValue(new Sala(nombreSala,contrasenaSala));
            Toast.makeText(this,"Sala creada con exito",Toast.LENGTH_SHORT).show();
            unirseSala();
        } else {
            Toast.makeText(this, "Por favor, introduce el nombre y la contraseña de la sala", Toast.LENGTH_SHORT).show();
        }
    }

    private void unirseSala() {
        String nombreSala = editTextNombreSala.getText().toString();
        String contrasena = editTextContrasena.getText().toString();

        // Obtener el ID del usuario actual
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String jugadorActual = user != null ? user.getUid() : null;

        if (jugadorActual != null && !nombreSala.isEmpty() && !contrasena.isEmpty()) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("salas").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot salaSnapshot : dataSnapshot.getChildren()) {
                        Sala sala = salaSnapshot.getValue(Sala.class);
                        if (sala.getNombre().equals(nombreSala) && sala.getContrasena().equals(contrasena)) {
                            // Unirse a la sala
                            String salaId = salaSnapshot.getKey(); // Obtener el ID de la sala
                            mDatabase.child("salas").child(salaId).child("jugadores").child(jugadorActual).setValue(true);
                            Toast.makeText(MultijugadorActivity.this, "Unido a la sala con éxito", Toast.LENGTH_SHORT).show();
                            // Redirigir al usuario a la pantalla de espera
                            Intent intent = new Intent(MultijugadorActivity.this, EsperaMultijugadorActivity.class);
                            intent.putExtra("salaId", salaId);
                            intent.putExtra("jugadorActual", jugadorActual);
                            startActivity(intent);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            });
        } else {
            Toast.makeText(this, "Por favor, introduce el nombre y la contraseña de la sala", Toast.LENGTH_SHORT).show();
        }
    }


}