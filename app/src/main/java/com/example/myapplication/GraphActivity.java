package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

public class GraphActivity<someList> extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
    ArrayList<ILineDataSet> dataSetSelected = new ArrayList<ILineDataSet>();
    LineData data;
    ArrayList<Sensore> readings;
    Map<String,Float> mp = new HashMap<String, Float>();

    private void refresh() {
        LineChart chart = (LineChart) findViewById(R.id.chart);
        startActivity(getIntent());
        finish();
        overridePendingTransition(0, 0);
    }
/*
    public void saveData() {
        LineChart chart = (LineChart) findViewById(R.id.chart);
        //chart.saveToGallery("Data.jpg");
        chart.saveToPath("SavedData","/storage/emulated/0/Pictures");
        Log.d(TAG,"Data saved");
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        LineChart chart = (LineChart) findViewById(R.id.chart);
        chart.setAutoScaleMinMaxEnabled(true);
        Button refreshButton = (Button) findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(graph -> refresh());

        FileManipulator fm = new FileManipulator("graphData.json", getApplicationContext());
        Gson gson = new Gson();
        Type SensoreType = new TypeToken<ArrayList<Sensore>>() {
        }.getType();
        readings = gson.fromJson(fm.readFromFile(), SensoreType);


        ArrayList<Entry> GasRes_m = new ArrayList<>();
        ArrayList<Entry> Humidity_m = new ArrayList<>();
        ArrayList<Entry> BreathVOCEQ_m = new ArrayList<>();
        ArrayList<Entry> CarbonDioxide_m = new ArrayList<>();
        ArrayList<Entry> IAQ_m = new ArrayList<>();
        ArrayList<Entry> Pressure_m = new ArrayList<>();
        ArrayList<Entry> Temp_m = new ArrayList<>();

        Float sumOfHumidityVal = 0f;
        Float sumOfGasResVal = 0f;
        Float sumOfTempVal = 0f;
        Float sumOfIAQVal = 0f;
        Float sumOfCarbonDioxideVal = 0f;
        Float sumOfPressureVal = 0f;
        Float sumOfBreathVOCVal = 0f;

        for(Sensore sr : readings) {

            GasRes_m.add(new Entry (sr.getTime(),sr.getGasResistence()));
            sumOfGasResVal += sr.getGasResistence();

            Humidity_m.add(new Entry (sr.getTime(),sr.getHumidity()));
            sumOfHumidityVal += sr.getHumidity();

            BreathVOCEQ_m.add(new Entry (sr.getTime(),sr.getBreathVOCEQ()));
            sumOfBreathVOCVal += sr.getBreathVOCEQ();

            CarbonDioxide_m.add(new Entry (sr.getTime(),sr.getCarbonDioxide()));
            sumOfCarbonDioxideVal += sr.getCarbonDioxide();

            IAQ_m.add(new Entry (sr.getTime(),sr.getIAQ()));
            sumOfIAQVal += sr.getIAQ();

            Pressure_m.add(new Entry (sr.getTime(),sr.getPressure() / 100000));
            sumOfPressureVal += sr.getPressure();

            Temp_m.add(new Entry (sr.getTime(),sr.getTemp()));
           sumOfTempVal += sr.getTemp();

        }

        mp.put("Humidity", sumOfHumidityVal / Humidity_m.size());
        mp.put("BreathVOC", sumOfBreathVOCVal / BreathVOCEQ_m.size());
        mp.put("Temperature", sumOfTempVal / Temp_m.size());
        mp.put("Pressure", sumOfPressureVal / Pressure_m.size());
        mp.put("IAQ", sumOfIAQVal / IAQ_m.size());
        mp.put("CO2", sumOfCarbonDioxideVal / CarbonDioxide_m.size());
        mp.put("GasRes", sumOfGasResVal / GasRes_m.size());


        LineDataSet GasRes_m_Set = new LineDataSet(GasRes_m, "GasRes");
        GasRes_m_Set.setColor(Color.parseColor("#E1DABD"));
        GasRes_m_Set.setCircleColor(Color.parseColor("#E1DABD"));
        GasRes_m_Set.setLineWidth(3.0f);

        LineDataSet Humidity_m_Set = new LineDataSet(Humidity_m, "Humidity");
        Humidity_m_Set.setColor(Color.parseColor("#E1DABD"));
        Humidity_m_Set.setCircleColor(Color.parseColor("#E1DABD"));
        Humidity_m_Set.setLineWidth(3.0f);

        LineDataSet BreathVOCEQ_m_Set = new LineDataSet(BreathVOCEQ_m, "BreathVOC");
        BreathVOCEQ_m_Set.setColor(Color.parseColor("#E1DABD"));
        BreathVOCEQ_m_Set.setCircleColor(Color.parseColor("#E1DABD"));
        BreathVOCEQ_m_Set.setLineWidth(3.0f);

        LineDataSet CarbonDioxide_m_Set = new LineDataSet(CarbonDioxide_m, "CO2");
        CarbonDioxide_m_Set.setLineWidth(3.0f);
        CarbonDioxide_m_Set.setColor(Color.parseColor("#ABC798"));
        CarbonDioxide_m_Set.setCircleColor(Color.parseColor("#ABC798"));

        LineDataSet IAQ_m_set = new LineDataSet(IAQ_m, "IAQ");
        IAQ_m_set.setLineWidth(3.0f);
        IAQ_m_set.setColor(Color.parseColor("#ABC798"));
        IAQ_m_set.setCircleColor(Color.parseColor("#ABC798"));

        LineDataSet Pressure_m_Set = new LineDataSet(Pressure_m, "Pressure");
        Pressure_m_Set.setLineWidth(3.0f);
        Pressure_m_Set.setColor(Color.parseColor("#ABC798"));
        Pressure_m_Set.setCircleColor(Color.parseColor("#ABC798"));

        LineDataSet Temp_m_Set = new LineDataSet(Temp_m, "Temperature");
        Temp_m_Set.setLineWidth(3.0f);
        Temp_m_Set.setColor(Color.parseColor("#ABC798"));
        Temp_m_Set.setCircleColor(Color.parseColor("#ABC798"));


        dataSets.add(GasRes_m_Set);
        dataSets.add(Humidity_m_Set);
        dataSets.add(BreathVOCEQ_m_Set);
        dataSets.add(CarbonDioxide_m_Set);
        dataSets.add(IAQ_m_set);
        dataSets.add(Pressure_m_Set);
        dataSets.add(Temp_m_Set);


        data = new LineData(dataSetSelected);


        chart.setData(data);

        chart.animateX(500);

        SharedPreferences Settings = this.getSharedPreferences("SpinnerPosition", MODE_PRIVATE);
        String myString = Settings.getString("SpinnerItem","");

        Spinner spinner = (Spinner) findViewById(R.id.spinner6);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        int spinnerPosition = adapter.getPosition(myString);

        spinner.setSelection(spinnerPosition);

        spinner.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        TextView middleValueView = findViewById(R.id.MiddleValueView);
        TextView maxValueView = findViewById(R.id.MaxValueView);
        LineChart chart = (LineChart) findViewById(R.id.chart);
        String mSpnValue=adapterView.getItemAtPosition(i).toString();

    for (ILineDataSet dataset : dataSets){
        if (dataset.getLabel().equals(mSpnValue)){
            middleValueView.setText(Float.toString(mp.get(adapterView.getItemAtPosition(i).toString())));
            maxValueView.setText(Float.toString(dataset.getYMax()));

            SharedPreferences myPrefs = this.getSharedPreferences("SpinnerPosition", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = myPrefs.edit();
            prefsEditor.putString("SpinnerItem", mSpnValue);
            prefsEditor.commit();

           dataSetSelected.removeAll(dataSets);
           dataSetSelected.add(dataset);

           chart.setData(data);
           chart.invalidate();


        }
    }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}