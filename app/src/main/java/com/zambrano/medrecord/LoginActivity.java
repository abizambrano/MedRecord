package com.zambrano.medrecord;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

// Pantalla de inicio de sesion con validacion de campos
public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("medrecord_prefs", MODE_PRIVATE);
        if (prefs.getBoolean("sesion_activa", false)) {
            irAMain(); return;
        }

        setContentView(R.layout.activity_login);

        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        emailInput = findViewById(R.id.emailEditText);
        passwordInput = findViewById(R.id.passwordEditText);

        MaterialButton loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();
            if (validar(email, password)) {
                intentarLogin(email, password);
            }
        });

        TextView goToRegister = findViewById(R.id.goToRegisterText);
        goToRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private boolean validar(String email, String password) {
        boolean valido = true;
        if (email.isEmpty()) {
            emailLayout.setError("El correo es obligatorio");
            valido = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Formato de correo invalido");
            valido = false;
        } else {
            emailLayout.setError(null);
        }
        if (password.isEmpty()) {
            passwordLayout.setError("La contrasena es obligatoria");
            valido = false;
        } else if (password.length() < 6) {
            passwordLayout.setError("Debe tener al menos 6 caracteres");
            valido = false;
        } else {
            passwordLayout.setError(null);
        }
        return valido;
    }

    private void intentarLogin(String email, String password) {
        String hash = PasswordUtils.hashPassword(password);
        UserDatabaseHelper db = new UserDatabaseHelper(this);
        new Thread(() -> {
            boolean exitoso = db.login(email, hash);
            runOnUiThread(() -> {
                if (exitoso) {
                    guardarSesion(email);
                    irAMain();
                } else {
                    emailLayout.setError("Credenciales incorrectas");
                }
            });
        }).start();
    }

    private void guardarSesion(String correo) {
        getSharedPreferences("medrecord_prefs", MODE_PRIVATE).edit()
                .putBoolean("sesion_activa", true)
                .putString("correo_usuario", correo).apply();
    }

    private void irAMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}