package com.example.coolweather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.gson.Forecast;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import java.io.IOException;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //初始化控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);

        /**SharedPreferences prefs =
         PreferenceManager.getDefaultSharedPreferences(this);

         String weatherString = prefs.getString("weather", null);*/
        /**
         * 从缓存解析数据
         */
        /**if (weatherString != null) {
         //有缓存时直接解析天气数据
         Weather weather = Utility.handlerWeatherResponse(weatherString);
         showWeatherInfo(weather);
         } else {
         //无缓存时去服务器查询天气
         String weatherId = getIntent().getStringExtra("weather_id");
         weatherLayout.setVisibility(View.VISIBLE);
         requestWeather(weatherId);
         }*/

        String weatherId = getIntent().getStringExtra("weather_id");
        weatherLayout.setVisibility(View.VISIBLE);
        requestWeather(weatherId);
        /**if (weatherString == null)
         Log.i("!!!","null");*/
    }

    /**
     * 根据天气id请求城市天气信息
     */
    public void requestWeather(final String weatherId) {
        HeConfig.init("754449629@qq.com", "Bmy20178013");
        HeConfig.switchToFreeServerNode();


        /*String weatherUrl =
         "http://guolin.tech/api/weather?cityid=" + weatherId +
         "&key=b9b7551afe3541df863c4d7df396c344";*/
        //Web API:9593c934aacd4111881a2c4d8d5d585a
        //Android API:b9b7551afe3541df863c4d7df396c344
        //可用：613a5123d89a4f64b9eb3a62580f0d8b
        String weatherUrl =
                "https://free-api.heweather.net/s6/weather/forecast?location=" + weatherId +
                        "&key=613a5123d89a4f64b9eb3a62580f0d8b";
        //Log.i("!!!", weatherId);
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather =
                        Utility.handlerWeatherResponse(responseText);

                Log.i("!!!",
                        "status=" + weather.status + " location=" + weather.basic.cityName + " " +
                                "weatherId=" + weather.basic.weatherId);
                Log.i("!!!", "?" + responseText);
                Log.i("!!!", "weather status:" + weather.status);
                Log.i("!!!", "weather forcast tem_max=" + weather.forecastList.get(0).tem_max);
                Log.i("!!!", "weather forcast date=" + weather.forecastList.get(2).date);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("!!!", "Thread run YES");
                        /**
                         * 天气预报的接口函数
                         */
                        HeWeather.getWeatherForecast(WeatherActivity.this, weatherId,
                                Lang.CHINESE_SIMPLIFIED,
                                Unit.METRIC, new HeWeather.OnResultWeatherForecastBeanListener() {

                                    @Override
                                    public void onError(Throwable throwable) {

                                        Log.i("!!!", "weatherID=" + weatherId);
                                        Log.i("!!!", throwable.getMessage());
                                        throwable.printStackTrace();

                                        Toast.makeText(WeatherActivity.this, "CityID:" + weatherId,
                                                Toast.LENGTH_SHORT).show();
                                        Log.i("!!!", "ConnectError!");
                                    }

                                    @Override
                                    public void onSuccess
                                            (List<interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast> list) {
                                        if (weather != null && "ok".equals(weather.status)) {
                                            SharedPreferences.Editor editor =
                                                    PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                                            editor.putString("weather", responseText);
                                            editor.apply();
                                            showWeatherInfo(weather);
                                        } else {
                                            Toast.makeText(WeatherActivity.this, "获取天气信息失败",
                                                    Toast.LENGTH_SHORT).show();
                                            Log.i("!!!", "111");
                                        }
                                        Toast.makeText(WeatherActivity.this, "Success!!!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败",
                                Toast.LENGTH_SHORT).show();
                        Log.i("!!!", "222");
                    }
                });
            }
        });
    }

    /**
     * 处理并展示Weather实体类的数据
     */
    public void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        //String degree = weather.now.temperature + "℃";
        //String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        //degreeText.setText(degree);
        //weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout,
                    false);
            TextView dateText = (TextView) view.findViewById(R.id.data_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.cond);
            minText.setText(forecast.tem_min);
            maxText.setText(forecast.tem_max);
            forecastLayout.addView(view);
        }
        /**if (weather.aqi != null) {
         aqiText.setText(weather.aqi.city.aqi);
         pm25Text.setText(weather.aqi.city.pm25);
         }
         String comfort = "舒适度：" + weather.suggestion.comfort.info;
         String carWash = "洗车指数：" + weather.suggestion.carWash.info;
         String sport = "运动建议：" + weather.suggestion.sport.info;
         comfortText.setText(comfort);
         carWashText.setText(carWash);
         sportText.setText(sport);
         */
        weatherLayout.setVisibility(View.VISIBLE);
    }
}