package com.zambrano.medrecord;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText nameInput, emailInput, passwordInput, confirmPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.nameEditText);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        emailInput = findViewById(R.id.emailEditText);
        passwordInput = findViewById(R.id.passwordEditText);
        confirmPasswordInput = findViewById(R.id.confirmPasswordEditText);

        MaterialButton registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            String nombre = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();
            String confirm = confirmPasswordInput.getText().toString();

            if (!password.equals(confirm)) {
                passwordLayout.setError("Las contraseñas no coinciden");
                return;
            }
            if (validar(email, password)) {
                registrarUsuario(nombre, email, password);
            }
        });
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
        if (password.length() < 6) {
            passwordLayout.setError("Debe tener al menos 6 caracteres");
            valido = false;
        } else {
            passwordLayout.setError(null);
        }
        return valido;
    }

    private void registrarUsuario(String nombre, String email, String password) {
        UserDatabaseHelper db = new UserDatabaseHelper(this);
        String hash = PasswordUtils.hashPassword(password);
        new Thread(() -> {
            boolean yaExiste = db.buscarPorCorreo(email);
            runOnUiThread(() -> {
                if (yaExiste) {
                    emailLayout.setError("Este correo ya esta registrado");
                } else {
                    db.insertarUsuario(nombre, email, hash);
                    Toast.makeText(this, "Registro exitoso!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }).start();
    }
}