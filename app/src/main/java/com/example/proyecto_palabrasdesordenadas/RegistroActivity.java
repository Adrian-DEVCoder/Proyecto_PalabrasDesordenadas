package com.example.proyecto_palabrasdesordenadas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {
    private SoundManager soundManager;
    private FirebaseAuth autorizacion;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        autorizacion = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        EditText editTextNombre = findViewById(R.id.et_nombre);
        EditText editTextCorreo = findViewById(R.id.et_correo);
        EditText editTextContrasena = findViewById(R.id.et_contrasena);
        Button buttonRegistro = findViewById(R.id.btn_registro);
        Button buttonIniciarSesion = findViewById(R.id.btn_iniciar_sesion);
        soundManager = new SoundManager();
        soundManager.setVolume(MainActivity.volson);
        // Modificamos el color de la barra de estado
        getWindow().setStatusBarColor(getResources().getColor(R.color.azuloscuro,this.getTheme()));
        // Listener del boton para realizar el registro
        buttonRegistro.setOnClickListener(   new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(RegistroActivity.this, MainActivity.sonido);
                String nombre = editTextNombre.getText().toString().trim();
                String correo = editTextCorreo.getText().toString().trim();
                String contrasena = editTextContrasena.getText().toString().trim();
                if(nombre.isEmpty()){
                    editTextNombre.setError("Nombre no puede estar vacio.");
                } else if (correo.isEmpty()){
                    editTextCorreo.setError("Correo no puede estar vacio.");
                } else if (contrasena.isEmpty()){
                    editTextContrasena.setError("Contraseña no puede estar vacio.");
                } else {
                    autorizacion.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = autorizacion.getCurrentUser();
                                if (user != null) {
                                    crearDocumentoUsuario(user, nombre);
                                }
                                Toast.makeText(RegistroActivity.this, "Creacion de Cuenta Satisfactoria.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistroActivity.this, MenuActivity.class));
                                finish();
                            } else {
                                Toast.makeText(RegistroActivity.this, "Creación de Cuenta Errónea.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        buttonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(RegistroActivity.this, MainActivity.sonido);
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
            }
        });
    }

    private void crearDocumentoUsuario(FirebaseUser user, String nombre) {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("nombre", nombre);
        usuario.put("email", user.getEmail());
        usuario.put("trofeos", new ArrayList<String>()); // Inicialmente, el usuario no tiene trofeos

        db.collection("usuarios").document(user.getUid())
                .set(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Documento creado exitosamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error al crear el documento: " + e);
                    }
                });
    }
}
