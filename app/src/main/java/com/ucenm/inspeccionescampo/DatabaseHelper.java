package com.ucenm.inspeccionescampo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inspecciones.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_INSPECCIONES = "inspecciones";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_RUTA_FOTO = "ruta_foto";
    public static final String COLUMN_RUTA_AUDIO = "ruta_audio";
    public static final String COLUMN_LATITUD = "latitud";
    public static final String COLUMN_LONGITUD = "longitud";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_INSPECCIONES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITULO + " TEXT, " +
                COLUMN_DESCRIPCION + " TEXT, " +
                COLUMN_RUTA_FOTO + " TEXT, " +
                COLUMN_RUTA_AUDIO + " TEXT, " +
                COLUMN_LATITUD + " REAL, " +
                COLUMN_LONGITUD + " REAL" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSPECCIONES);
        onCreate(db);
    }

    // Método para insertar una nueva inspección
    public boolean insertarInspeccion(Inspeccion inspeccion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, inspeccion.getTitulo());
        values.put(COLUMN_DESCRIPCION, inspeccion.getDescripcion());
        values.put(COLUMN_RUTA_FOTO, inspeccion.getRutaFoto());
        values.put(COLUMN_RUTA_AUDIO, inspeccion.getRutaAudio());
        values.put(COLUMN_LATITUD, inspeccion.getLatitud());
        values.put(COLUMN_LONGITUD, inspeccion.getLongitud());

        long result = db.insert(TABLE_INSPECCIONES, null, values);
        db.close();
        return result != -1;
    }

    // Método para obtener todas las inspecciones guardadas
    public List<Inspeccion> obtenerTodasInspecciones() {
        List<Inspeccion> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INSPECCIONES, null);

        if (cursor.moveToFirst()) {
            do {
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION));
                String foto = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RUTA_FOTO));
                String audio = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RUTA_AUDIO));
                double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUD));
                double lng = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUD));

                // Usamos el constructor vacío y los setters para evitar conflictos de parámetros
                Inspeccion insp = new Inspeccion();
                insp.setTitulo(titulo);
                insp.setDescripcion(descripcion);
                insp.setRutaFoto(foto);
                insp.setRutaAudio(audio);
                insp.setLatitud(lat);
                insp.setLongitud(lng);

                lista.add(insp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }
}