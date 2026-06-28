package com.zambrano.medrecord;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

// Formulario que sirve tanto para CREAR como para EDITAR un medicamento.
// Si recibe un id_medicamento por Intent, entra en modo edicion y precarga los datos.
public class AgregarMedicamentoActivity extends AppCompatActivity {

    private TextInputEditText etNombre, etDescripcion, etDosis;
    private TextInputLayout layoutNombre, layoutDosis;
    private AutoCompleteTextView spinnerUnidad;
    private UserDatabaseHelper db;
    private int idMedicamento = -1; // -1 significa modo crear, >0 significa modo editar
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_medicamento);

        db = new UserDatabaseHelper(this);

        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etDosis = findViewById(R.id.etDosis);
        layoutNombre = findViewById(R.id.layoutNombre);
        layoutDosis = findViewById(R.id.layoutDosis);
        spinnerUnidad = findViewById(R.id.spinnerUnidad);

        // Configurar dropdown de unidades
        String[] unidades = {"mg", "ml", "pastilla", "capsula", "gota"};
        ArrayAdapter<String> adapterUnidades = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, unidades);
        spinnerUnidad.setAdapter(adapterUnidades);

        // Obtener datos del Intent
        idUsuario = getIntent().getIntExtra("id_usuario", -1);
        idMedicamento = getIntent().getIntExtra("id_medicamento", -1);

        // Si hay id_medicamento, es modo EDITAR: precargar datos
        if (idMedicamento != -1) {
            etNombre.setText(getIntent().getStringExtra("nombre"));
            etDescripcion.setText(getIntent().getStringExtra("descripcion"));
            etDosis.setText(String.valueOf(getIntent().getIntExtra("dosis_mg", 0)));
            spinnerUnidad.setText(getIntent().getStringExtra("unidad"), false);
        }

        MaterialButton btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(v -> guardar());
    }

    private void guardar() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText() != null ? etDescripcion.getText().toString().trim() : "";
        String dosisStr = etDosis.getText().toString().trim();
        String unidad = spinnerUnidad.getText().toString().trim();

        // Validaciones
        if (nombre.isEmpty()) { layoutNombre.setError("El nombre es obligatorio"); return; }
        layoutNombre.setError(null);
        if (dosisStr.isEmpty()) { layoutDosis.setError("La dosis es obligatoria"); return; }
        layoutDosis.setError(null);

        int dosis = Integer.parseInt(dosisStr);

        if (idMedicamento == -1) {
            // CREATE: insertar nuevo medicamento
            long resultado = db.insertarMedicamento(nombre, descripcion, dosis, unidad, idUsuario);
            if (resultado != -1) Toast.makeText(this, "Medicamento guardado", Toast.LENGTH_SHORT).show();
        } else {
            // UPDATE: actualizar medicamento existente
            db.actualizarMedicamento(idMedicamento, nombre, descripcion, dosis, unidad);
            Toast.makeText(this, "Medicamento actualizado", Toast.LENGTH_SHORT).show();
        }
        finish(); // Vuelve a MainActivity, que recargara la lista en onResume()
    }
}
