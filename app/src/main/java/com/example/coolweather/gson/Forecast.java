package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {
    public String date;

    @SerializedName("tmp_max")
    public String tem_max;

    @SerializedName("tmp_min")
    public String tem_min;

    @SerializedName("cond_txt_d")
    public String cond;

}
