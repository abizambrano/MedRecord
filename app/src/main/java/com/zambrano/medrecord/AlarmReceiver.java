package com.zambrano.medrecord;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// BroadcastReceiver que recibe la alarma del AlarmManager
// y muestra la notificacion al usuario.
public class AlarmReceiver extends BroadcastReceiver {

    public static final String EXTRA_MEDICAMENTO = "nombre_medicamento";

    @Override
    public void onReceive(Context context, Intent intent) {
        String nombreMed = intent.getStringExtra(EXTRA_MEDICAMENTO);
        if (nombreMed == null) nombreMed = "tu medicamento";

        // Muestra la notificacion usando el helper
        NotificationHelper.mostrarNotificacion(context, nombreMed);
    }
}
