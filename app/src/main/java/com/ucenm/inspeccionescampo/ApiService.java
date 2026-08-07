package com.ucenm.inspeccionescampo;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    // Llama a PostPersons.php para enviar/guardar una inspección
    @POST("PostPersons.php")
    Call<Void> enviarInspeccion(@Body Inspeccion inspeccion);

    // Llama a GetPersons.php para obtener la lista desde MySQL
    @GET("GetPersons.php")
    Call<List<Inspeccion>> obtenerInspecciones();
}