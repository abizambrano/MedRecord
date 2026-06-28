package com.zambrano.medrecord;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MedicamentoAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private MedicamentoAdapter adapter;
    private UserDatabaseHelper db;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Crear canal de notificaciones al iniciar la app
        NotificationHelper.crearCanal(this);

        // Solicitar permiso de notificaciones en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }

        // Obtener ID del usuario de la sesion activa
        SharedPreferences prefs = getSharedPreferences("medrecord_prefs", MODE_PRIVATE);
        String correo = prefs.getString("correo_usuario", "");
        db = new UserDatabaseHelper(this);
        idUsuario = db.obtenerIdUsuario(correo);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerMedicamentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicamentoAdapter(this);
        recyclerView.setAdapter(adapter);

        // FAB para agregar medicamento
        FloatingActionButton fab = findViewById(R.id.fabAgregar);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AgregarMedicamentoActivity.class);
            intent.putExtra("id_usuario", idUsuario);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarga la lista cada vez que vuelve al Activity (despues de agregar/editar)
        cargarMedicamentos();
        //programarNotificacion("Amoxicilina");
        NotificationHelper.mostrarNotificacion(this, "Amoxicilina");
    }

    private void cargarMedicamentos() {
        List<Medicamento> lista = db.obtenerMedicamentos(idUsuario);
        adapter.setLista(lista);
    }

    @Override
    public void onEdit(Medicamento medicamento) {
        // Navega al formulario con los datos del medicamento para editar
        Intent intent = new Intent(this, AgregarMedicamentoActivity.class);
        intent.putExtra("id_medicamento", medicamento.getId());
        intent.putExtra("nombre", medicamento.getNombre());
        intent.putExtra("descripcion", medicamento.getDescripcion());
        intent.putExtra("dosis_mg", medicamento.getDosisMg());
        intent.putExtra("unidad", medicamento.getUnidad());
        intent.putExtra("id_usuario", idUsuario);
        startActivity(intent);
    }

    @Override
    public void onDelete(final Medicamento medicamento) {
        // AlertDialog de confirmacion antes de eliminar
        new AlertDialog.Builder(this)
                .setTitle("Eliminar medicamento")
                .setMessage("Seguro que deseas eliminar " + medicamento.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    db.eliminarMedicamento(medicamento.getId());
                    cargarMedicamentos();
                    // Snackbar con opcion de deshacer
                    Snackbar.make(recyclerView, medicamento.getNombre() + " eliminado", Snackbar.LENGTH_LONG)
                            .setAction("Deshacer", v -> {
                                db.insertarMedicamento(medicamento.getNombre(),
                                        medicamento.getDescripcion(), medicamento.getDosisMg(),
                                        medicamento.getUnidad(), idUsuario);
                                cargarMedicamentos();
                            }).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Programa una notificacion diaria a las 8am para el primer medicamento de la lista
    private void programarNotificacion(String nombreMedicamento) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.EXTRA_MEDICAMENTO, nombreMedicamento);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}
