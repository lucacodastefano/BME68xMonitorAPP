package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private boolean Connected;

    @Override
    public void onResume() {
        super.onResume();
    }

    public void openGraphActivity() {
        Intent intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }

    public void ServiceStart() {
        if (!Connected) {
                Intent intent = new Intent(getApplicationContext(),
                MQTTClientService.class);
                startService(intent);
                Connected = true;
        }
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            TextView TempTextView = findViewById((R.id.tempTextView));
            TempTextView.setText(intent.getStringExtra("Temp"));

            TextView CO2TextView = findViewById((R.id.co2TextView));
            CO2TextView.setText(intent.getStringExtra("CO2"));

            TextView GasTextView = findViewById((R.id.gasResTexView));
            GasTextView.setText(intent.getStringExtra("GasRes"));

            TextView PressureTextView = findViewById((R.id.pressureTextView));
            PressureTextView.setText(intent.getStringExtra("Pressure"));

            TextView IAQTextView = findViewById((R.id.IAQTextView));
            IAQTextView.setText(intent.getStringExtra("IAQ"));

            TextView humidityTextView = findViewById((R.id.humidityTextView));
            humidityTextView.setText(intent.getStringExtra("Humidity"));

            TextView breathVocTextView = findViewById((R.id.breathVocTextView));
            breathVocTextView.setText(intent.getStringExtra("BreathVOCEQ"));

            TextView timeTextView = findViewById((R.id.timeTextView));
            timeTextView.setText(intent.getStringExtra("Time"));


        }
    };

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(view -> ServiceStart());

        Button graphButton = findViewById(R.id.GraphButton);
        graphButton.setOnClickListener(view -> openGraphActivity());

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessageReceiver, new IntentFilter("send-data-MainActivity"));

    }

    protected void onDestroy() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

        super.onDestroy();
    }
}