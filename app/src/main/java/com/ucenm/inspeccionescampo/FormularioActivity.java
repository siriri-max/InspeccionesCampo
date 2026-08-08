package com.ucenm.inspeccionescampo;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 101;
    private static final int REQUEST_PERMISSIONS = 102;
    private static final int REQUEST_LOCATION = 103;

    private EditText edtTitulo, edtDescripcion;
    private ImageView imgPreview;
    private Button btnFoto, btnGrabarAudio, btnUbicacion, btnGuardar;
    private TextView txtCoordenadas;

    private Bitmap fotoBitmap;
    private MediaRecorder mediaRecorder;
    private String rutaAudio = "";
    private boolean isRecording = false;
    private double latitud = 0.0;
    private double longitud = 0.0;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        edtTitulo = findViewById(R.id.edtTitulo);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        imgPreview = findViewById(R.id.imgPreview);
        btnFoto = findViewById(R.id.btnTomarFoto);
        btnGrabarAudio = findViewById(R.id.btnGrabarAudio);
        btnUbicacion = findViewById(R.id.btnObtenerUbicacion);
        btnGuardar = findViewById(R.id.btnGuardarInspeccion);
        txtCoordenadas = findViewById(R.id.txtCoordenadas);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        solicitarPermisos();

        btnFoto.setOnClickListener(v -> abrirCamara());

        btnGrabarAudio.setOnClickListener(v -> {
            if (isRecording) {
                detenerGrabacion();
            } else {
                iniciarGrabacion();
            }
        });

        btnUbicacion.setOnClickListener(v -> obtenerUbicacionGPS());

        btnGuardar.setOnClickListener(v -> guardarInspeccion());
    }

    private void solicitarPermisos() {
        String[] permisos = {
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permisos, REQUEST_PERMISSIONS);
        }
    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data != null) {
            fotoBitmap = (Bitmap) data.getExtras().get("data");
            imgPreview.setImageBitmap(fotoBitmap);
        }
    }

    private void iniciarGrabacion() {
        // Cambiado a .m4a para compatibilidad moderna
        File fileAudio = new File(getExternalCacheDir(), "audio_inspeccion_" + System.currentTimeMillis() + ".m4a");
        rutaAudio = fileAudio.getAbsolutePath();

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        // Formato MPEG_4 y codificador AAC
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        mediaRecorder.setOutputFile(rutaAudio);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            btnGrabarAudio.setText("Detener Grabación");
            Toast.makeText(this, "Grabando audio...", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void detenerGrabacion() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            btnGrabarAudio.setText("Grabar Audio");
            Toast.makeText(this, "Audio guardado correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerUbicacionGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Obteniendo coordenadas...", Toast.LENGTH_SHORT).show();

            fusedLocationClient.getCurrentLocation(
                            com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            latitud = location.getLatitude();
                            longitud = location.getLongitude();
                            txtCoordenadas.setText("Lat: " + latitud + " | Long: " + longitud);
                            Toast.makeText(this, "Ubicación obtenida con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Asegúrate de tener el GPS activado", Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
    }

    private void guardarInspeccion() {
        String titulo = edtTitulo.getText().toString().trim();
        String descripcion = edtDescripcion.getText().toString().trim();

        if (titulo.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor completa el título y la descripción", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", 1);

        String fechaActual = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date());

        Inspeccion nuevaInspeccion = new Inspeccion();
        nuevaInspeccion.setUsuarioId(usuarioId);
        nuevaInspeccion.setTitulo(titulo);
        nuevaInspeccion.setDescripcion(descripcion);
        nuevaInspeccion.setFechaInspeccion(fechaActual);
        nuevaInspeccion.setLatitud(latitud);
        nuevaInspeccion.setLongitud(longitud);

        nuevaInspeccion.setRuta_foto("");
        nuevaInspeccion.setRuta_audio("");

        ApiService apiService = ApiClient.getApiService();

        Call<Void> callInspeccion = apiService.enviarInspeccion(nuevaInspeccion);
        callInspeccion.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(FormularioActivity.this, "¡Inspección registrada con éxito!", Toast.LENGTH_SHORT).show();

                    if (fotoBitmap != null) {
                        subirFotoServidor(apiService);
                    }

                    if (!rutaAudio.isEmpty()) {
                        subirAudioServidor(apiService);
                    }

                    finish();
                } else {
                    Toast.makeText(FormularioActivity.this, "Error al guardar en el servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(FormularioActivity.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void subirFotoServidor(ApiService apiService) {
        try {
            File file = new File(getExternalCacheDir(), "foto_temp.jpg");
            java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
            fotoBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();

            okhttp3.RequestBody requestFile = okhttp3.RequestBody.create(okhttp3.MediaType.parse("image/*"), file);
            okhttp3.MultipartBody.Part bodyFoto = okhttp3.MultipartBody.Part.createFormData("foto", file.getName(), requestFile);

            okhttp3.RequestBody idBody = okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), "16");

            Call<RespuestaServidor> call = apiService.subirFoto(bodyFoto, idBody);
            call.enqueue(new Callback<RespuestaServidor>() {
                @Override
                public void onResponse(Call<RespuestaServidor> call, Response<RespuestaServidor> response) {}

                @Override
                public void onFailure(Call<RespuestaServidor> call, Throwable t) {}
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void subirAudioServidor(ApiService apiService) {
        File file = new File(rutaAudio);
        if (!file.exists()) return;

        // Aquí es donde actualizas la línea del MediaType a "audio/mp4"
        okhttp3.RequestBody requestFile = okhttp3.RequestBody.create(okhttp3.MediaType.parse("audio/mp4"), file);
        okhttp3.MultipartBody.Part bodyAudio = okhttp3.MultipartBody.Part.createFormData("audio", file.getName(), requestFile);
        okhttp3.RequestBody idBody = okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), "16");

        Call<RespuestaServidor> call = apiService.subirAudio(bodyAudio, idBody);
        call.enqueue(new Callback<RespuestaServidor>() {
            @Override
            public void onResponse(Call<RespuestaServidor> call, Response<RespuestaServidor> response) {}

            @Override
            public void onFailure(Call<RespuestaServidor> call, Throwable t) {}
        });
    }

    private void reproducirAudio() {
        if (rutaAudio != null && !rutaAudio.isEmpty()) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(rutaAudio);
                mediaPlayer.prepare();
                mediaPlayer.start();
                Toast.makeText(this, "Reproduciendo audio...", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al reproducir audio", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Graba un audio primero", Toast.LENGTH_SHORT).show();
        }
    }
}