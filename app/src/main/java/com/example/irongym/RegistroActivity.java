package com.example.irongym;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnCreateAccount.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            AppDatabase.databaseWriteExecutor.execute(() -> {
                int count = AppDatabase.getDatabase(getApplicationContext()).usuarioDao().comprobarUsuarioExiste(username);
                if (count > 0) {
                    runOnUiThread(() -> {
                        Toast.makeText(RegistroActivity.this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Usuario nuevoUsuario = new Usuario();
                    nuevoUsuario.nombre = name;
                    nuevoUsuario.usuario = username;
                    nuevoUsuario.password = password;

                    AppDatabase.getDatabase(getApplicationContext()).usuarioDao().insertUsuario(nuevoUsuario);
                    runOnUiThread(() -> {
                        Toast.makeText(RegistroActivity.this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            });
        });
    }
}
