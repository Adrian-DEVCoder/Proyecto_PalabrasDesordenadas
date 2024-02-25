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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autorizacion;
    private SoundManager soundManager;
    public static int sonido;
    public static float volson = 100f;
    public static int progSound = 100;
    public static int progMusic = 100;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sonido = 1;
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
        soundManager = new SoundManager();
        soundManager.setVolume(volson);
        getWindow().setStatusBarColor(getResources().getColor(R.color.azuloscuro, this.getTheme()));
        Intent intent = new Intent(this, BackgroundSoundService.class);
        intent.putExtra("song_selection",1 );
        startService(intent);
        buttonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(MainActivity.this, sonido);
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
                soundManager.playSound(MainActivity.this, sonido);
                startActivity(new Intent(MainActivity.this, RegistroActivity.class));
            }
        });

        ibuttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playSound(MainActivity.this, sonido);
                signInWithGoogle();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.btn_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Toast.makeText(MainActivity.this, "Inicio de sesión con Google fallido", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        autorizacion.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = autorizacion.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, MenuActivity.class));
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Inicio de sesión fallido: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}