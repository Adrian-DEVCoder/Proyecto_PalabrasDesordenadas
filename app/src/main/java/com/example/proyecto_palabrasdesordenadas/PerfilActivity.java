package com.example.proyecto_palabrasdesordenadas;

import androidx.appcompat.app.AppCompatActivity;
import org.rajawali3d.materials.textures.Texture;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import org.rajawali3d.view.SurfaceView;

import java.util.HashMap;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {
    private Map<Integer, Integer[]> modelAndTextureMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Inicializar el mapa
        modelAndTextureMap = new HashMap<>();
        modelAndTextureMap.put(R.id.icon1, new Integer[]{R.raw.trofeo_corona, R.drawable.gold});
        modelAndTextureMap.put(R.id.icon2, new Integer[]{R.raw.trofeo_espiral, R.drawable.gold});
        modelAndTextureMap.put(R.id.icon3, new Integer[]{R.raw.trofeo_piramide_doble, R.drawable.bronze});
        modelAndTextureMap.put(R.id.icon4, new Integer[]{R.raw.trofeo_volcan_rayos, R.drawable.gold});
        modelAndTextureMap.put(R.id.icon5, new Integer[]{R.raw.trofeo_palacio, R.drawable.pink});
        modelAndTextureMap.put(R.id.icon6, new Integer[]{R.raw.trofeo_laberinto, R.drawable.red});
        modelAndTextureMap.put(R.id.icon7, new Integer[]{R.raw.trofeo_esferoide_pentagonal, R.drawable.gold});
        ImageView icon1 = findViewById(R.id.icon1);
        ImageView icon2 = findViewById(R.id.icon2);
        ImageView icon3 = findViewById(R.id.icon3);
        ImageView icon4 = findViewById(R.id.icon4);
        ImageView icon5 = findViewById(R.id.icon5);
        ImageView icon6 = findViewById(R.id.icon6);
        ImageView icon7 = findViewById(R.id.icon7);

        // Configurar los listeners para cada ImageView
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer[] modelAndTextureIds = modelAndTextureMap.get(v.getId());
                if (modelAndTextureIds != null) {
                    show3DModelDialog(modelAndTextureIds[0], modelAndTextureIds[1]);
                }
            }
        };

        icon1.setOnClickListener(listener);
        icon2.setOnClickListener(listener);
        icon3.setOnClickListener(listener);
        icon4.setOnClickListener(listener);
        icon5.setOnClickListener(listener);
        icon6.setOnClickListener(listener);
        icon7.setOnClickListener(listener);
    }

    private void show3DModelDialog(int modelId, int textureId) {
        // Inflar el layout del diálogo
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_perfil, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Crear y configurar el diálogo
        AlertDialog dialog = builder.create();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // Obtener el SurfaceView y configurarlo
        SurfaceView surfaceView = dialogView.findViewById(R.id.surface_view);
        MyRenderer renderer = new MyRenderer(this);
        surfaceView.setSurfaceRenderer(renderer);

        // Mostrar el diálogo
        dialog.show();

        // Cargar el modelo después de mostrar el diálogo para asegurar que el contexto esté listo
        surfaceView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    renderer.loadModel(modelId, textureId);
                    renderer.getTextureManager().addTexture(new Texture("texture", textureId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}