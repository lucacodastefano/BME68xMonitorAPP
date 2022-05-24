package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;



public class FileManipulator {
    private String nameToRead;
    private String nameToWrite;
    private Context context;
    private String data;

    public FileManipulator(String nameToRead, Context context) {
    this. nameToRead = nameToRead;
    this.context = context;
    }

    public FileManipulator(String nameToWrite, Context context, String data) {
        this.context = context;
        this.nameToWrite = nameToWrite;
        this.data = data;
    }

    public void writeToFile() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(nameToWrite, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(nameToRead);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public void setNameToWrite(String nameToWrite) {
        this.nameToWrite = nameToWrite;
    }
}
