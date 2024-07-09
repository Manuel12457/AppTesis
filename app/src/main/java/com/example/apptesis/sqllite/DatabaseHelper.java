package com.example.apptesis.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.apptesis.clases.Categoria;
import com.example.apptesis.clases.CategoriaSQLite;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "eventos.db";
    private static final int DATABASE_VERSION = 1;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE eventos (evento_id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT NOT NULL, duracion TEXT NOT NULL, tiempo_aviso TEXT NOT NULL, notas TEXT NOT NULL, fecha_hora TEXT NOT NULL)");
        db.execSQL("CREATE TABLE categorias (categoria_id INTEGER PRIMARY KEY AUTOINCREMENT, categoria TEXT NOT NULL, descripcion TEXT NOT NULL, imagen TEXT NOT NULL, id_evento INTEGER, FOREIGN KEY (id_evento) REFERENCES eventos(evento_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS categorias");
        db.execSQL("DROP TABLE IF EXISTS eventos");
        onCreate(db);
    }

    public int updateEvent(int eventId, String titulo, String fecha_hora, String duracion,  String tiempo_aviso, String notas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("fecha_hora", fecha_hora);
        values.put("duracion", duracion);
        values.put("tiempo_aviso", tiempo_aviso);
        values.put("notas", notas);

        // Updating row
        return db.update("eventos", values, "evento_id = ?", new String[]{String.valueOf(eventId)});
    }

    public ArrayList<CategoriaSQLite> getCategoriesByEventId(int eventId) {
        ArrayList<CategoriaSQLite> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM categorias WHERE id_evento = ?",
                new String[]{String.valueOf(eventId)}
        );

        if (cursor.moveToFirst()) {
            do {
                String id = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("categoria_id")));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("categoria"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("imagen"));
                String eventoId = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("id_evento")));

                CategoriaSQLite cat = new CategoriaSQLite(id, category, description, image, eventoId);
                categories.add(cat);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return categories;
    }

    public int deleteCategory(int categoriaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("categorias", "categoria_id = ?", new String[]{String.valueOf(categoriaId)});
    }
    public int deleteEvento(int eventoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("eventos", "evento_id = ?", new String[]{String.valueOf(eventoId)});
    }
}
