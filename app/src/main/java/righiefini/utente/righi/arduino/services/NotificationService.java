package righiefini.utente.righi.arduino.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import righiefini.utente.righi.arduino.MainActivity;
import righiefini.utente.righi.arduino.R;

/**
 * Created by Utente on 06/03/2018.
 */

public class NotificationService extends Service {
    String ip;
    String ultimoStatoP;
    String ultimoStatoV;
    Context context;

    public NotificationService() {
    }

    ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public NotificationService(String IP, Context cx) {
        ip = IP;
        context = cx;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String statoPompa = intent.getStringExtra("pompa");
                String statoVentola = intent.getStringExtra("ventola");

                if (!(ultimoStatoP.equals(statoPompa))) {
                    ultimoStatoP = statoPompa;
                    String message = "La pompa è " + statoPompa;
                    notifica(message);
                }
                if (!(ultimoStatoV.equals(statoVentola))) {
                    ultimoStatoV = statoVentola;
                    String message = "La ventilazione è " + statoVentola;
                    notifica(message);
                }


            }
        }, new IntentFilter("dati"));
    }

    private void notifica(String message) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.serra)
                .setContentTitle("Serra")
                .setContentText(message)
                .setVibrate(new long[]{3000, 3000, 3000, 3000, 3000})
                .setLights(Color.BLUE, 1000, 2000)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent());
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification.build());
    }

    private PendingIntent pendingIntent() {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("ip", ip);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
