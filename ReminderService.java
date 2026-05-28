package com.example.progetto;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

public class ReminderService extends Service {
    private static final String CHANNEL_ID = "SchoolReminderChannel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Recuperiamo il testo del promemoria inviato dall'Activity
        String infoNota = intent.getStringExtra("nota_testo");
        if (infoNota == null) infoNota = "Hai degli impegni scolastici da controllare!";

        creaNotificationChannel();
        inviaNotifica(infoNota);

        // Ferma il servizio automaticamente una volta inviata la notifica
        stopSelf();
        return START_NOT_STICKY;
    }

    private void inviaNotifica(String testo) {
        // Intent per riaprire l'app (MainActivity) cliccando sulla notifica
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm) // Icona di sistema temporanea
                .setContentTitle("Promemoria Scolastico")
                .setContentText(testo)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Apre l'activity
                .setAutoCancel(true);

        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.notify(1, builder.build());
        }
    }

    private void creaNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notifiche Registro Scolastico",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Canale per i promemoria del diario scolastico");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}