package com.zambrano.medrecord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    public UserDatabaseHelper(Context context) {
        super(context, "medrecord.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios (" +
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "correo TEXT NOT NULL UNIQUE," +
                "contrasena_hash TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

    public long insertarUsuario(String nombre, String correo, String hash) {
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("correo", correo);
        values.put("contrasena_hash", hash);
        return getWritableDatabase().insert("usuarios", null, values);
    }

    public boolean buscarPorCorreo(String correo) {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT id_usuario FROM usuarios WHERE correo = ?",
                new String[]{correo});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }

    public boolean login(String correo, String hash) {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT id_usuario FROM usuarios WHERE correo = ? AND contrasena_hash = ?",
                new String[]{correo, hash});
        boolean encontrado = cursor.getCount() > 0;
        cursor.close();
        return encontrado;
    }
}