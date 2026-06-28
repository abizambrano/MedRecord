package com.zambrano.medrecord;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

// Clase de utilidad que centraliza la creacion de notificaciones.
// En Android 8+ (API 26) es obligatorio crear un NotificationChannel
// antes de poder mostrar notificaciones.
public class NotificationHelper {

    public static final String CHANNEL_ID = "medrecord_recordatorios";
    public static final String CHANNEL_NAME = "Recordatorios de Medicamentos";
    private static final int NOTIFICATION_ID = 1001;

    // Crea el canal de notificaciones (solo se ejecuta en API 26+)
    public static void crearCanal(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            canal.setDescription("Recordatorios para tomar medicamentos a tiempo");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(canal);
        }
    }

    // Muestra la notificacion del recordatorio
    public static void mostrarNotificacion(Context context, String nombreMedicamento) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle("Recordatorio MedRecord")
                .setContentText("Es hora de tomar: " + nombreMedicamento)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) manager.notify(NOTIFICATION_ID, builder.build());
    }
}
