package com.ucenm.inspeccionescampo;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

        apiService = ApiClient.getApiService();

        // Llamada inicial para cargar las inspecciones al abrir la pantalla
        cargarInspeccionesRemotas();

        btnNuevaInspeccion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormularioActivity.class);
            startActivity(intent);
        });

        // Menú de opciones multimedia al hacer clic en cualquier inspección de la lista
        listViewInspecciones.setOnItemClickListener((parent, view, position, id) -> {
            Inspeccion inspeccionSeleccionada = listaInspecciones.get(position);

            CharSequence[] opciones = {"Ver Foto", "Reproducir Audio"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Opciones de Inspección");
            builder.setItems(opciones, (dialog, which) -> {
                if (which == 0) {
                    // Opción 0: Ver Foto
                    String rutaFoto = inspeccionSeleccionada.getRuta_foto();
                    if (rutaFoto != null && !rutaFoto.isEmpty() && !rutaFoto.equals("foto_pendiente.jpg")) {
                        String urlFoto = "http://192.168.100.174/inspecciones_api/" + rutaFoto;

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(urlFoto));
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Esta inspección no tiene una foto válida asociada", Toast.LENGTH_SHORT).show();
                    }
                } else if (which == 1) {
                    // Opción 1: Reproducir Audio
                    String rutaAudio = inspeccionSeleccionada.getRuta_audio();
                    if (rutaAudio != null && !rutaAudio.isEmpty() && !rutaAudio.equals("audio_pendiente.3gp")) {
                        String urlAudio = "http://192.168.100.174/inspecciones_api/uploads/audios/" + rutaAudio;

                        try {
                            MediaPlayer mediaPlayer = new MediaPlayer();
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.setDataSource(urlAudio);
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(mp -> {
                                mp.start();
                                Toast.makeText(this, "Reproduciendo audio del servidor...", Toast.LENGTH_SHORT).show();
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error al reproducir el audio", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Esta inspección no tiene un audio válido asociada", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                        // Validamos que el título y la descripción NO estén vacíos o nulos
                        String titulo = insp.getTitulo();
                        String descripcion = insp.getDescripcion();

                        if (titulo != null && !titulo.trim().isEmpty() && descripcion != null && !descripcion.trim().isEmpty()) {
                            // Si tienen texto, los agregamos a la lista
                            titulos.add(titulo + "\n" + descripcion + "\nFecha: " + insp.getFechaInspeccion());
                        }
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