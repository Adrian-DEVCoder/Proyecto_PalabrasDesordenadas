package com.example.proyecto_palabrasdesordenadas;

import static androidx.constraintlayout.widget.StateSet.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EsperaMultijugadorActivity extends AppCompatActivity {
    private String salaId;
    private String jugadorActual;
    private String idUsuario;
    private String idRival;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espera_multijugador);
        Intent intent = getIntent();
        salaId = intent.getStringExtra("salaId");
        jugadorActual = intent.getStringExtra("jugadorActual");

        TextView textViewEspera = findViewById(R.id.tv_esperando);
        Button buttonCancelar = findViewById(R.id.btn_cancelar);
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("salas").child(salaId).child("jugadores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 1) {
                    // Determinar si el jugador actual es el primer jugador o el segundo
                    if (dataSnapshot.child(jugadorActual).exists()) {
                        // El jugador actual ya está en la sala, asignarle el idUsuario
                        idUsuario = jugadorActual;
                        // Asignar el idRival al otro jugador en la sala
                        for (DataSnapshot jugadorSnapshot : dataSnapshot.getChildren()) {
                            if (!jugadorSnapshot.getKey().equals(jugadorActual)) {
                                idRival = jugadorSnapshot.getKey();
                                break;
                            }
                        }
                    }
                    startGame();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void startGame() {
        // Aquí puedes iniciar el juego, por ejemplo, redirigir al usuario a la actividad del juego
        Intent intent = new Intent(EsperaMultijugadorActivity.this, JuegoMultijugadorActivity.class);
        intent.putExtra("salaId", salaId);
        intent.putExtra("jugadorActual", jugadorActual);
        intent.putExtra("idUsuario", idUsuario);
        intent.putExtra("idRival", idRival);
        startActivity(intent);
        finish();
    }
}
