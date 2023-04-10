package com.example.plantmi;

import java.io.Serializable;

public class SensorAir implements Serializable {
    private String humidity;
    private String temperature;
    private String type;

    public SensorAir(){
    }

    public String getType(){return type;}
    public String getTemperature(){return temperature;}
    public String getHumidity(){return humidity;}
}
