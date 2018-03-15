package righiefini.utente.righi.arduino.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import righiefini.utente.righi.arduino.Value;

/**
 * Created by Utente on 03/03/2018.
 */

public class Services extends Service {
    String ip;

    Context context = this;
    public static final String ACTION_LOCATION_BROADCAST = Services.class.getName() + "LocationBroadcast";
    private JSONObject jsonObject;

    public Services(String Ip) {
        switch (ip = Ip) {
        }


    }

    public Services() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String url = "http://" + ip + "/php/datiIrrigazione.php/.json";
        final OkHttpClient client = new OkHttpClient.Builder().
                readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        RequestBody body = new FormBody.Builder()
                .build();
        final Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    jsonObject = new JSONObject(response.body().string());
                    final Value value = new Value();
                    value.setAcqua(jsonObject.get("acqua").toString());
                    value.setStaoPompa(jsonObject.get("pompa").toString());
                    value.setStatoVentola(jsonObject.get("ventola").toString());
                    value.setUmiditàTerreno(jsonObject.get("terreno").toString());
                    value.setTemperatura(jsonObject.get("temperatura").toString());
                    value.setUmiditàAria(jsonObject.get("umidità") + "%");
                    value.setAutomatica(jsonObject.get("automatica").toString());
                    Intent intent = new Intent("dati");
                    intent.putExtra("valori", value);
                    intent.putExtra("pompa",value.getStaoPompa());
                    intent.putExtra("ventola",value.getStatoVentola());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
