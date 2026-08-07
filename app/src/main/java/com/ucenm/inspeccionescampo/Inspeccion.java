package com.ucenm.inspeccionescampo;

public class Inspeccion {
    private String titulo;
    private String descripcion;
    private String rutaFoto;
    private String rutaAudio;
    private double latitud;
    private double longitud;

    // Constructor
    public Inspeccion(String titulo, String descripcion, String rutaFoto, String rutaAudio, double latitud, double longitud) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.rutaFoto = rutaFoto;
        this.rutaAudio = rutaAudio;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Getters y Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getRutaFoto() { return rutaFoto; }
    public void setRutaFoto(String rutaFoto) { this.rutaFoto = rutaFoto; }

    public String getRutaAudio() { return rutaAudio; }
    public void setRutaAudio(String rutaAudio) { this.rutaAudio = rutaAudio; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
}