package com.example.proyecto_palabrasdesordenadas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class DBHandler extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME = "PalabrasDesordenadas.sqlite";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "palabras";
    private static final String ID_COL = "id";
    private static final String SPANISH_WORD_COL = "spanish";
    private static final String ENGLISH_WORD_COL = "english";

    public DBHandler(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SPANISH_WORD_COL + " TEXT, "
                + ENGLISH_WORD_COL + " TEXT)";
        sqLiteDatabase.execSQL(query);

        // Leer el archivo JSON y cargar los datos en la base de datos
        try{
            InputStream is = context.getAssets().open("PalabrasDesordenadas.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            JSONArray jsonArray = new JSONArray(new String(buffer, "UTF-8"));

            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String spanish = jsonObject.getString("spanish");
                String english = jsonObject.getString("english");
                ContentValues values = new ContentValues();
                values.put(SPANISH_WORD_COL, spanish);
                values.put(ENGLISH_WORD_COL, english);
                sqLiteDatabase.insert(TABLE_NAME, null, values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String generarPalabraAleatoria() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numeroAleatorio = new Random().nextInt(200) +  1;
        String sNumero = String.valueOf(numeroAleatorio);
        Cursor cursor = null;
        String palabraGenerada = null;
        try {
            cursor = db.rawQuery("SELECT " + SPANISH_WORD_COL + " FROM " + TABLE_NAME + " WHERE " + ID_COL + " = ?", new String[]{sNumero});
            if (cursor.moveToFirst()) {
                palabraGenerada = cursor.getString(0);
            } else {
                System.out.println("Error al obtener la palabra de la base de datos. ID: "+palabraGenerada);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return palabraGenerada;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
