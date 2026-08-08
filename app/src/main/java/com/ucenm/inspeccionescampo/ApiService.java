package com.ucenm.inspeccionescampo;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    // 1. Enviar los datos generales de la inspección a inspecciones.php
    @POST("inspecciones.php")
    Call<Void> enviarInspeccion(@Body Inspeccion inspeccion);

    // 2. Obtener la lista de inspecciones desde MySQL
    @GET("GetPersons.php") // (O cámbialo a "inspecciones.php" si usas el mismo archivo para el GET)
    Call<List<Inspeccion>> obtenerInspecciones();

    // 3. Subir la FOTO al servidor PHP con su ID de inspección
    @Multipart
    @POST("upload_foto.php")
    Call<RespuestaServidor> subirFoto(
            @Part MultipartBody.Part foto,
            @Part("InspeccionId") RequestBody inspeccionId
    );

    // 4. Subir el AUDIO al servidor PHP con su ID de inspección
    @Multipart
    @POST("upload_audio.php")
    Call<RespuestaServidor> subirAudio(
            @Part MultipartBody.Part audio,
            @Part("InspeccionId") RequestBody inspeccionId
    );
}