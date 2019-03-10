package com.example.coolweather;

import com.google.gson.Gson;

public class day {

    /**
     * id : 937
     * name : 苏州
     * weather_id : CN101190401
     */

    private int id;
    private String name;
    private String weather_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(String weather_id) {
        this.weather_id = weather_id;
    }

    /**
    public static void main(String[] args) {
        Gson gson=new Gson();
        day day=gson.fromJson(jsonString, com.example.coolweather.day.class);
        day.id
    }*/
}
