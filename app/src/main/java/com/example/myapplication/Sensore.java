package com.example.myapplication;
import android.util.Log;

import org.json.*;

public class Sensore {

    private int GasRes, Pressure, Time;
    private float Humidity, IAQ, breathVOCEQ, CarbonDioxide, Temp;

    public Sensore(int GasRes, float Temp, int Pressure, int Time, float CarbonDioxide, float breathVOCEQ, float IAQ, float Humidity) {

        this.Temp = Temp;
        this.Pressure = Pressure;
        this.GasRes = GasRes;
        this.Humidity = Humidity;
        this.breathVOCEQ = breathVOCEQ;
        this.IAQ = IAQ;
        this.CarbonDioxide = CarbonDioxide;
        this.Time = Time;

    }

    public int getGasResistence() {
        return GasRes;
    }

    public float getTemp() {
        return Temp;
    }

    public int getTime() {
        return Time / 1000;
    }

    public int getPressure() {
        return Pressure;
    }

    public float getHumidity() {
        return Humidity;
    }

    public float getBreathVOCEQ() {
        return breathVOCEQ;
    }

    public float getIAQ() { return IAQ; }

    public float getCarbonDioxide() { return CarbonDioxide; }

}