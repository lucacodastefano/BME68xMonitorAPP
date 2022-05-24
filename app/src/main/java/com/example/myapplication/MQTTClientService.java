package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Type;
import java.security.Provider;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MQTTClientService extends Service {

    ArrayList<Sensore> readings;
    final int[] times_received = {0};
    String topic = "esp8266/test";
    String serverURI = "tcp://broker.hivemq.com:1883";
    int qos = 0;


    public void loadData() {

        FileManipulator fm = new FileManipulator("graphData.json", getApplicationContext());
        Gson gson = new Gson();
        Type SensoreType = new TypeToken<ArrayList<Sensore>>() {
        }.getType();
        readings = gson.fromJson(fm.readFromFile(), SensoreType);

    }


    public void makeConnection() {
        try {
            String clientId = "Android-APP-MQTT";
            MqttConnectOptions options = new MqttConnectOptions();
            final MqttAndroidClient[] client = {new MqttAndroidClient(this.getApplicationContext(), serverURI,
                    clientId)};

            options.setCleanSession(false);
            options.setConnectionTimeout(0);
            loadData();

            Log.d(TAG, "Tentativo in corso...");

            client[0].setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    client[0].unregisterResources();
                    client[0].close();
                    try {
                        client[0].disconnect();
                        Thread.sleep(5000);
                        makeConnection();
                    } catch (MqttException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Gson gson = new Gson();
                    // creo msg che contiene il messaggio arrivato
                    String msg = new String(message.getPayload());
                    // istanzio una classe gson per la conversione
                    // istanzio una classe sensore e derivo dalla conversione del messaggio tramite gson i dati da inserire
                    Sensore sensore = gson.fromJson(msg, Sensore.class);
                    // aggiungo alle readings se, il tempo è superiore all'ultimo registrato
                    if (readings.get(readings.size() - 1).getTime() > sensore.getTime()) {
                        readings.clear();
                    }
                    readings.add(sensore);
                    // trasformo readings in un json
                    String JsonReadings = gson.toJson(readings);
                    // lo scrivo nel file che mantiene i dati
                    FileManipulator fm = new FileManipulator("graphData.json", getApplicationContext(), JsonReadings);
                    fm.writeToFile();
                    // numero di messaggi arrivati
                    SensoreEnricher sf = new SensoreEnricher(sensore);

                    times_received[0] += 1;

                    Intent intent = new Intent("send-data-MainActivity");
                    intent.putExtra("CO2", sf.getFormattedCO2());
                    intent.putExtra("Temp", sf.getFormattedTemp());
                    intent.putExtra("Pressure", sf.getFormattedPressure());
                    intent.putExtra("Time", String.valueOf(sensore.getTime()));
                    intent.putExtra("IAQ", sf.getFormattedIAQ());
                    intent.putExtra("BreathVOCEQ", sf.getFormattedBreathVOC());
                    intent.putExtra("GasRes", sf.getFormattedGasRes());
                    intent.putExtra("Humidity", sf.getFormattedHumidity());

                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                    Log.d(TAG, "E' arrivato il messaggio n. " + Integer.toString(times_received[0]) + " Contenuto: " + msg);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }

            });

            try {
                Log.d(TAG, "Servizio avviato, callback configurate, tento la connessione...");

                IMqttToken token = client[0].connect(options);

                //client[0].registerResources(this);
                token.setActionCallback(new IMqttActionListener() {
                    @Override

                    public void onSuccess(IMqttToken asyncActionToken) {
                        // We are connected
                        Log.d(TAG, "Connesso al broker ");

                        Log.d(TAG, "configuro il topic: " + topic);


                        try {
                            Log.d(TAG, "Provo a sottoscrivermi al topic... " + topic);
                            IMqttToken subToken = client[0].subscribe(topic, qos);
                            subToken.setActionCallback(new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken asyncActionToken) {
                                    Log.d(TAG, "Sottoscrizione avvenuta! " + topic);
                                }

                                @Override
                                public void onFailure(IMqttToken asyncActionToken,
                                                      Throwable exception) {
                                    Log.d(TAG, "Trovato, subscribe");
                                }
                            });
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.d(TAG, "Trovato, connect");
                        client[0].unregisterResources();
                        client[0].close();
                        try {
                            client[0].disconnect();
                            Thread.sleep(5000);
                            makeConnection();
                        } catch (MqttException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
                Log.d(TAG, "Trovato, try");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG,"Trovato, first try");
        }
    }
        @Override
        public int onStartCommand (Intent intent,int flags, int startId){
            makeConnection();
            return super.onStartCommand(intent, flags, startId);

        }
        @Nullable
        @Override
        public IBinder onBind (Intent intent){
            return null;
        }

    }