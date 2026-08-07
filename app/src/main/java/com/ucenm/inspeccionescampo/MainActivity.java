package com.ucenm.inspeccionescampo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnNuevaInspeccion;
    private ListView listViewInspecciones;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private List<Inspeccion> listaInspecciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNuevaInspeccion = findViewById(R.id.btnNuevaInspeccion);
        listViewInspecciones = findViewById(R.id.listViewInspecciones);
        dbHelper = new DatabaseHelper(this);

        // Abrir el formulario al hacer clic en el botón
        btnNuevaInspeccion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormularioActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cargar las inspecciones cada vez que volvamos a esta pantalla
        cargarInspecciones();
    }


    private void cargarInspecciones() {
        listaInspecciones = dbHelper.obtenerTodasInspecciones();
        List<String> titulos = new ArrayList<>();

        for (Inspeccion insp : listaInspecciones) {
            titulos.add(insp.getTitulo() + "\n" + insp.getDescripcion());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titulos);
        listViewInspecciones.setAdapter(adapter);
    }
}