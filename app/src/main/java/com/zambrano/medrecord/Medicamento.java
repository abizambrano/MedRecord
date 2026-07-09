package com.zambrano.medrecord;

// Clase modelo que representa un medicamento en la aplicacion.
// Cada campo corresponde a una columna en la tabla "medicamentos" de SQLite.
public class Medicamento {
    private int id;
    private String nombre;
    private String descripcion;
    private int dosisMg;
    private String unidad;
    private int idUsuario;
    private String hora; // formato "HH:mm", ej: "08:30"

    public Medicamento(int id, String nombre, String descripcion, int dosisMg, String unidad, String hora,int idUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.dosisMg = dosisMg;
        this.unidad = unidad;
        this.idUsuario = idUsuario;
        this.hora = hora;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getDosisMg() { return dosisMg; }
    public String getUnidad() { return unidad; }
    public int getIdUsuario() { return idUsuario; }
    public String getHora() { return hora; }


    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setDosisMg(int dosisMg) { this.dosisMg = dosisMg; }
    public void setUnidad(String unidad) { this.unidad = unidad; }
    public void setHora(String hora) { this.hora = hora; }
}
