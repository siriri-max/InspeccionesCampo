package com.ucenm.inspeccionescampo;

public class Inspeccion {
    private int usuarioId;
    private String titulo;
    private String descripcion;
    private String FechaInspeccion; // <-- Agregado para que coincida con MySQL/PHP
    private String rutaFoto;
    private String rutaAudio;
    private double latitud;
    private double longitud;

    // 1. Constructor vacío
    public Inspeccion() {
    }

    // 2. Constructor completo
    public Inspeccion(String titulo, String descripcion, String FechaInspeccion, String rutaFoto, String rutaAudio, double latitud, double longitud) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.FechaInspeccion = FechaInspeccion;
        this.rutaFoto = rutaFoto;
        this.rutaAudio = rutaAudio;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // 3. Getters y Setters
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaInspeccion() { return FechaInspeccion; } // <-- El método que faltaba
    public void setFechaInspeccion(String fechaInspeccion) { FechaInspeccion = fechaInspeccion; }

    public String getRutaFoto() { return rutaFoto; }
    public void setRutaFoto(String rutaFoto) { this.rutaFoto = rutaFoto; }

    public String getRutaAudio() { return rutaAudio; }
    public void setRutaAudio(String rutaAudio) { this.rutaAudio = rutaAudio; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
}