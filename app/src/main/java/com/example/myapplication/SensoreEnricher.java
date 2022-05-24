package com.example.myapplication;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SensoreEnricher {
    Sensore sensore;

    private int GasRes, Pressure, Time;
    private float Humidity, IAQ, breathVOCEQ, CarbonDioxide, Temp;

    public SensoreEnricher(Sensore sensore) {
        this.Temp = sensore.getTemp();
        this.Pressure = sensore.getPressure();
        this.GasRes = sensore.getGasResistence();
        this.Humidity = sensore.getHumidity();
        this.breathVOCEQ = sensore.getBreathVOCEQ();
        this.IAQ = sensore.getIAQ();
        this.CarbonDioxide = sensore.getCarbonDioxide();
        this.Time = sensore.getTime();
    }
    public String getFormattedTemp (){
        String formattedValue;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        formattedValue = nf.format(Temp) + " °C";

        return formattedValue;
    }
    public String getFormattedPressure (){
        String formattedValue;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        formattedValue = nf.format(Pressure) + " Pa";

        return formattedValue;
    }
    public String getFormattedGasRes (){
        String formattedValue;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        formattedValue = nf.format(GasRes) + " Ohm";

        return formattedValue;
    }
    public String getFormattedIAQ (){
        String formattedValue;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        formattedValue = nf.format(IAQ);

        return formattedValue;
    }
    public String getFormattedCO2 (){
        String formattedValue;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        formattedValue = nf.format(CarbonDioxide) +" ppm" ;

        return formattedValue;
    }
    public String getFormattedHumidity (){
        String formattedValue;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        formattedValue = nf.format(Humidity) +" %" ;

        return formattedValue;
    }
    public String getFormattedBreathVOC (){
        String formattedValue;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        formattedValue = nf.format(breathVOCEQ) +" ppm" ;

        return formattedValue;
    }
}
