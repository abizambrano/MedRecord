package com.zambrano.medrecord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    public UserDatabaseHelper(Context context) {
        super(context, "medrecord.db", null, 3); // version 2 por nueva tabla
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabla de usuarios (autenticacion - entregable 9)
        db.execSQL("CREATE TABLE usuarios (" +
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "correo TEXT NOT NULL UNIQUE," +
                "contrasena_hash TEXT NOT NULL)");

        // Tabla de medicamentos (CRUD - entregable 10)
        db.execSQL("CREATE TABLE medicamentos (" +
                "id_medicamento INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "descripcion TEXT," +
                "dosis_mg INTEGER NOT NULL," +
                "unidad TEXT NOT NULL," +
                "hora TEXT," +
                "id_usuario INTEGER NOT NULL," +
                "FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS medicamentos");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

    // ── USUARIOS (entregable 9) ──────────────────────────────
    public long insertarUsuario(String nombre, String correo, String hash) {
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("correo", correo);
        values.put("contrasena_hash", hash);
        return getWritableDatabase().insert("usuarios", null, values);
    }

    public boolean buscarPorCorreo(String correo) {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT id_usuario FROM usuarios WHERE correo = ?", new String[]{correo});
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

    public int obtenerIdUsuario(String correo) {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT id_usuario FROM usuarios WHERE correo = ?", new String[]{correo});
        int id = -1;
        if (cursor.moveToFirst()) id = cursor.getInt(0);
        cursor.close();
        return id;
    }

    // ── MEDICAMENTOS — CRUD COMPLETO ────────────────────────

    // CREATE: inserta un nuevo medicamento y devuelve el ID generado
    public long insertarMedicamento(String nombre, String descripcion, int dosisMg, String unidad, String hora, int idUsuario) {
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("descripcion", descripcion);
        values.put("dosis_mg", dosisMg);
        values.put("unidad", unidad);
        values.put("id_usuario", idUsuario);
        values.put("hora", hora);
        return getWritableDatabase().insert("medicamentos", null, values);
    }

    // READ: devuelve la lista de medicamentos de un usuario
    public List<Medicamento> obtenerMedicamentos(int idUsuario) {
        List<Medicamento> lista = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM medicamentos WHERE id_usuario = ? ORDER BY nombre ASC",
                new String[]{String.valueOf(idUsuario)});
        if (cursor.moveToFirst()) {
            do {
                lista.add(new Medicamento(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_medicamento")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                        cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("dosis_mg")),
                        cursor.getString(cursor.getColumnIndexOrThrow("unidad")),
                        cursor.getString(cursor.getColumnIndexOrThrow("hora")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // UPDATE: actualiza un medicamento existente por su ID
    public int actualizarMedicamento(int id, String nombre, String descripcion, int dosisMg, String unidad, String hora) {
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("descripcion", descripcion);
        values.put("dosis_mg", dosisMg);
        values.put("unidad", unidad);
        values.put("hora", hora);
        return getWritableDatabase().update("medicamentos", values,
                "id_medicamento = ?", new String[]{String.valueOf(id)});
    }

    // DELETE: elimina un medicamento por su ID
    public int eliminarMedicamento(int id) {
        return getWritableDatabase().delete("medicamentos",
                "id_medicamento = ?", new String[]{String.valueOf(id)});
    }
}
