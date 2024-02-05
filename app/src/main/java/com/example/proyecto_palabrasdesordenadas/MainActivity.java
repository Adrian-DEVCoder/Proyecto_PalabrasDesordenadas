package com.example.proyecto_palabrasdesordenadas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autorizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autorizacion = FirebaseAuth.getInstance();
        EditText editTextCorreo = findViewById(R.id.et_contrasena);
        EditText editTextContrasena = findViewById(R.id.et_correo);
        Button buttonIniciarSesion = findViewById(R.id.btn_iniciar_sesion);
        ImageButton ibuttonGoogle = findViewById(R.id.btn_google);
        Button buttonRegistrarse = findViewById(R.id.btn_registro);
        TextView textViewIniciar = findViewById(R.id.tv_registro);
        TextView textViewIniciarCon = findViewById(R.id.tv_iniciarcon);
        TextView textViewTodavia = findViewById(R.id.tv_registro);
        TextView textViewCorreo = findViewById(R.id.tv_correo);
        TextView textViewContrasena = findViewById(R.id.tv_contrasena);
        ImageView imageViewFondo = findViewById(R.id.img_fondo);
        ImageView imageViewLogo = findViewById(R.id.img_logo);
        getWindow().setStatusBarColor(getResources().getColor(R.color.azuloscuro, this.getTheme()));
        buttonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = editTextCorreo.getText().toString().trim();
                String contrasena = editTextContrasena.getText().toString().trim();
                if(!correo.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    if(!contrasena.isEmpty()){
                        autorizacion.signInWithEmailAndPassword(correo, contrasena).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(MainActivity.this,"Inicio de Sesion satisfactorio", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, MenuActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Inicio de Sesión erroneo", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        editTextContrasena.setError("La contraseña no puede estar vacia.");
                    }
                } else if (correo.isEmpty()){
                    editTextCorreo.setError("El correo no puede estar vacio.");
                } else {
                    editTextCorreo.setError("Introduce una direccion de correo válida.");
                }
            }
        });
        buttonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistroActivity.class));
            }
        });
    }
}