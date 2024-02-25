package com.example.proyecto_palabrasdesordenadas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistroActivity extends AppCompatActivity {
    private SoundManager soundManager;
    private FirebaseAuth autorizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        autorizacion = FirebaseAuth.getInstance();
        ImageView imageViewFondo = findViewById(R.id.img_fondo);
        ImageView imageViewLogo = findViewById(R.id.img_logo2);
        EditText editTextNombre = findViewById(R.id.et_nombre);
        EditText editTextCorreo = findViewById(R.id.et_correo);
        EditText editTextContrasena = findViewById(R.id.et_contrasena);
        Button buttonRegistro = findViewById(R.id.btn_registro);
        Button buttonIniciarSesion = findViewById(R.id.btn_iniciar_sesion);
        TextView textViewNombre = findViewById(R.id.tv_nombre);
        TextView textViewCorreo = findViewById(R.id.tv_correo);
        TextView textViewContrasena = findViewById(R.id.tv_contrasena);
        TextView textViewRegistro = findViewById(R.id.tv_registro);
        TextView textViewIniciar = findViewById(R.id.tv_iniciarsesion);
        soundManager = new SoundManager();
        soundManager.setVolume(MainActivity.volson);
        // Modificamos el color de la barra de estado
        getWindow().setStatusBarColor(getResources().getColor(R.color.azuloscuro,this.getTheme()));
        // Listener del boton para realizar el registro
        buttonRegistro.setOnClickListener(new View.OnClickListener() {
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
}