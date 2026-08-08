package com.ucenm.inspeccionescampo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnNuevaInspeccion;
    private ListView listViewInspecciones;
    private ApiService apiService;
    private List<Inspeccion> listaInspecciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNuevaInspeccion = findViewById(R.id.btnNuevaInspeccion);
        listViewInspecciones = findViewById(R.id.listViewInspecciones);

        // Inicializamos la interfaz de Retrofit
        apiService = ApiClient.getApiService();

        // Abrir el formulario al hacer clic en el botón
        btnNuevaInspeccion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormularioActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cargar las inspecciones desde el servidor cada vez que volvamos a esta pantalla
        cargarInspeccionesRemotas();
    }

    private void cargarInspeccionesRemotas() {
        Call<List<Inspeccion>> call = apiService.obtenerInspecciones();
        call.enqueue(new Callback<List<Inspeccion>>() {
            @Override
            public void onResponse(Call<List<Inspeccion>> call, Response<List<Inspeccion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaInspecciones = response.body();
                    List<String> titulos = new ArrayList<>();

                    for (Inspeccion insp : listaInspecciones) {
                        // Mostramos título, descripción y fecha que vienen de MySQL
                        titulos.add(insp.getTitulo() + "\n" + insp.getDescripcion() + "\nFecha: " + insp.getFechaInspeccion());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            MainActivity.this,
                            android.R.layout.simple_list_item_1,
                            titulos
                    );
                    listViewInspecciones.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "Error al obtener datos del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Inspeccion>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}