package righiefini.utente.righi.arduino;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Utente on 02/01/2018.
 */

public class Comandi extends AsyncTask<String, Void, Void> {
    ProgressDialog progressDialog;

    Context context;

    String ip = null;

    public Comandi(String ip, Context cx) {
        this.ip = ip;
        context = cx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("in corso");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.SECONDS)
                .connectTimeout(2, TimeUnit.SECONDS).build();
        RequestBody body = new FormBody.Builder()
                .add("comando", params[0])
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url("http://" + this.ip + "/php/irrigazione.php")
                .build();

        try {
            client.newCall(request).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        progressDialog.dismiss();
    }

}
