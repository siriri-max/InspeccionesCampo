package com.ucenm.inspeccionescampo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UsuarioLogin extends AppCompatActivity {

    private EditText edtCorreo, edtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtCorreo = findViewById(R.id.edtCorreo);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = edtCorreo.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (correo.isEmpty() || password.isEmpty()) {
                    Toast.makeText(UsuarioLogin.this, "Por favor llene todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Navega temporalmente a la pantalla principal
                    Intent intent = new Intent(UsuarioLogin.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
