package righiefini.utente.righi.arduino;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import righiefini.utente.righi.arduino.services.NotificationService;
import righiefini.utente.righi.arduino.services.Services;

public class MainActivity extends Activity {
    private EditText ip1;
    private EditText ip2;
    private EditText ip3;
    private EditText ip4;
    private Button pompa;
    private Button ventola;
    private Button dati;
    private TextView statoPompa;
    private TextView statoVentola;
    private TextView valUmudità;
    private TextView valTemp;
    private TextView temp;
    private TextView umAria;
    private TextView valUmAria;
    private TextView livAcqua;
    private TextView valAcqua;
    private Button automatica;
    private Switch notifiche;
    private TextView txNotifiche;
    private Services service;
    private NotificationService notificationService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                notificationService.onDestroy();
                service.onDestroy();
                if (getIntent().getStringExtra("ip").toString() != null) {
                    writeIp(getIntent().getStringExtra("ip").toString());
                }
            }
        });


        pompa = (Button) findViewById(R.id.Pompa);
        ventola = (Button) findViewById(R.id.Ventola);
        txNotifiche = (TextView) findViewById(R.id.notifiche);
        notifiche = (Switch) findViewById(R.id.StatoNotifiche);
        statoPompa = (TextView) findViewById(R.id.statopompa);
        statoVentola = (TextView) findViewById(R.id.statoventola);
        valTemp = (TextView) findViewById(R.id.valTemp);
        temp = (TextView) findViewById(R.id.temp);
        umAria = (TextView) findViewById(R.id.umiditàAria);
        valUmAria = (TextView) findViewById(R.id.valUmiditàAria);
        livAcqua = (TextView) findViewById(R.id.livAcqua);
        valAcqua = (TextView) findViewById(R.id.valAcqua);
        dati = (Button) findViewById(R.id.dati);
        ip1 = (EditText) findViewById(R.id.ip1);
        ip2 = (EditText) findViewById(R.id.ip2);
        ip3 = (EditText) findViewById(R.id.ip3);
        ip4 = (EditText) findViewById(R.id.ip4);

        valUmudità = (TextView) findViewById(R.id.valUmidità);
        automatica = (Button) findViewById(R.id.modAutomatica);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                final Value value = (Value) intent.getSerializableExtra("valori");
                if (value != null) {
                    service.onDestroy();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            statoVentola.setText(value.getStatoVentola());
                            statoPompa.setText(value.staoPompa);
                            valUmudità.setText(value.getUmiditàTerreno());
                            valTemp.setText(value.getTemperatura());
                            valUmAria.setText(value.getUmiditàAria());
                            valAcqua.setText(value.getAcqua());
                            if (value.getAutomatica().equals("attiva")) {
                                automatica.setBackgroundColor(Color.GREEN);
                                automatica.setText("ABILITATA");
                            } else {
                                automatica.setBackgroundColor(Color.RED);
                                automatica.setText("DISABILITATA");
                            }
                        }
                    });

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Thread.sleep(60000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            service = new Services(getIp());
                            service.onCreate();
                        }
                    }).start();

                }

            }
        }, new IntentFilter("dati"));
        notifiche.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (notifiche.isChecked()) {
                    if (connessione()) {
                        notificationService = new NotificationService(getIp(),getBaseContext());
                        notificationService.onCreate();

                    } else {
                        Toast.makeText(getBaseContext(), "Inserire un ip valido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    notifiche.setChecked(false);
                    if(!(notificationService.equals(null))){
                    notificationService.onDestroy();}


                }
            }
        });
        dati.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(final View v) {
                if (connessione()) {
                    if (verIP()) {
                        service = new Services(getIp());
                        service.onCreate();
                    } else {
                        Toast.makeText(getBaseContext(), "IP non valido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Internet non disponibile", Toast.LENGTH_SHORT).show();
                }

            }
        });

        pompa.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (connessione()) {
                    esegui("pompa");
                } else {
                    Toast.makeText(getApplicationContext(), "Internet non disponibile", Toast.LENGTH_SHORT).show();
                }


            }
        });
        ventola.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (connessione()) {
                    esegui("ventola");
                } else {
                    Toast.makeText(getApplicationContext(), "Internet non disponibile", Toast.LENGTH_SHORT).show();
                }


            }
        });
        automatica.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (connessione()) {
                    esegui("automatica");
                } else {
                    Toast.makeText(getApplicationContext(), "Internet non disponibile", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private String getIp() {
        String indirizzoIp = ip1.getText().toString() + "." + ip2.getText().toString() + "." + ip3.getText().toString() + "." + ip4.getText().toString();
        return indirizzoIp;
    }

    private boolean verIP() {
        if (ip1.getText().equals("")) {
            return false;
        }
        if (ip2.getText().toString().equals("")) {
            return false;
        }
        if (ip3.getText().toString().equals("")) {
            return false;
        }
        if (ip4.getText().toString().equals("")) {
            return false;
        } else
            return verValIp();
    }

    private boolean verValIp() {
        int num = 0;

        num = new Integer(ip1.getText().toString());
        if (num > 255) {
            return false;
        }
        num = new Integer(ip2.getText().toString());
        if (num > 255) {
            return false;
        }
        num = new Integer(ip3.getText().toString());
        if (num > 255) {
            return false;
        }
        num = new Integer(ip4.getText().toString());
        if (num > 255) {
            return false;
        }
        return true;
    }

    private void esegui(String comando) {
        if (verIP()) {
            final String indirizzoIp = ip1.getText().toString() + "." + ip2.getText().toString() + "." + ip3.getText().toString() + "." + ip4.getText().toString();
            new Comandi(indirizzoIp, this).execute(comando);
        } else {
            Toast.makeText(getBaseContext(), "Controllare l'IP", Toast.LENGTH_LONG).show();

        }
    }

    private void writeIp(String ip) {
        ArrayList<Integer> arrayIndex = new ArrayList<>();
        for (int i = 0; i < ip.length(); i++) {
            if (ip.charAt(i) == '.') {
                arrayIndex.add(i);
            }
        }
        ip1.setText(ip.substring(0, arrayIndex.get(0) - 1));
        ip2.setText(ip.subSequence(arrayIndex.get(0) + 1, arrayIndex.get(1) - 1));
        ip3.setText(ip.subSequence(arrayIndex.get(1) + 1, arrayIndex.get(2) - 1));
        ip4.setText(ip.subSequence(arrayIndex.get(2) + 1, arrayIndex.get(3)));

    }

    private boolean connessione() {
        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

