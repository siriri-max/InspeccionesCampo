package com.ucenm.inspeccionescampo;

public class Inspeccion {
    private int usuarioId;
    private String titulo;
    private String descripcion;
    private String FechaInspeccion;
    private String ruta_foto;  // <-- Cambiado para coincidir con la BD/PHP
    private String ruta_audio; // <-- Cambiado para coincidir con la BD/PHP
    private double latitud;
    private double longitud;

    // 1. Constructor vacío
    public Inspeccion() {
    }

    // 2. Constructor completo
    public Inspeccion(String titulo, String descripcion, String FechaInspeccion, String ruta_foto, String ruta_audio, double latitud, double longitud) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.FechaInspeccion = FechaInspeccion;
        this.ruta_foto = ruta_foto;
        this.ruta_audio = ruta_audio;
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

    public String getFechaInspeccion() { return FechaInspeccion; }
    public void setFechaInspeccion(String fechaInspeccion) { FechaInspeccion = fechaInspeccion; }

    public String getRuta_foto() { return ruta_foto; }
    public void setRuta_foto(String ruta_foto) { this.ruta_foto = ruta_foto; }

    public String getRuta_audio() { return ruta_audio; }
    public void setRuta_audio(String ruta_audio) { this.ruta_audio = ruta_audio; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
}