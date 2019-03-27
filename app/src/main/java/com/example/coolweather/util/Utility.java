package com.example.coolweather.util;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.coolweather.WeatherActivity;
import com.example.coolweather.db.City;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;
import com.example.coolweather.gson.Weather;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); ++i){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response, int provinceId){
        if (!TextUtils.isEmpty(response)){
            try{
                Log.i("###",response);
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceID(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的区级数据
     */
    public static boolean handleCountyResponse(String response, int cityId){
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties = new JSONArray(response);
                for (int i =0; i < allCounties.length(); i ++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherID(countyObject.getString("weather_id"));
                    county.setCityID(cityId);
                    Log.i("!!!","cityId");
                    county.save();
                }
                return true;
            } catch (JSONException e){
                e.printStackTrace();
                Log.i("!!!","cityId");

            }
        }
        return false;
    }

    /**
     * 将返回的JSON数据解析成Weather实体类
     * @param response
     */
    public static Weather handlerWeatherResponse(String response){
        Log.i("!!!",response);
        try{

            //Log.i("!!!","JSON 1");
            JSONObject jsonObject = new JSONObject(response);
            //Gson gson = new Gson();

            //Log.i("!!!","JSON 2");
            //Log.i("!!!",jsonObject.getString("status"));
            //Weather weather = gson.fromJson(response,Weather.class);
            //Log.i("!!!","JSON 3");

            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");

            //Log.i("!!!","JSON 3");
            //Log.i("!!!",weather.status);
            //return weather;
            String weatherContent = jsonArray.getJSONObject(0).toString();
            Log.i("!!!","weatherContent=" + weatherContent);
            //Log.i("!!!","JSON 4");
            Weather weather = new Gson().fromJson(weatherContent,Weather.class);

            return weather;

        } catch (Exception e){
            Log.i("!!!","ERROR");
            e.printStackTrace();
        }
        return null;
    }


}
/**
    public static String JSONTokener(String str_json) {
        // consume an optional byte order mark (BOM) if it exists
        if (str_json != null && str_json.startsWith("\ufeff")) {
            istr_json = str_json.substring(1);
        }
        return str_json;
    }
*/