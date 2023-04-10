package com.example.plantmi;

import java.io.Serializable;

public class SensorLevel implements Serializable {
    private String value;
    private String type;

    public SensorLevel(){
    }

    public String getType(){return type;}
    public String getValue(){return value;}

}
