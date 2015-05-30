package com.example.lomo.coolweather.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lomo.coolweather.R;
import com.example.lomo.coolweather.util.HttpCallbackListener;
import com.example.lomo.coolweather.util.HttpUtil;
import com.example.lomo.coolweather.util.Utility;

import org.w3c.dom.Text;

/**
 * Created by Lomo on 15/5/29.
 */
public class WeatherActivity extends Activity {

    private LinearLayout weatherInfoLayout;
    /**
     * 用于显示城市名
     */
    private TextView cityNameText;
    private TextView publishText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView currentDateText;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        //初始化控件
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDateText = (TextView) findViewById(R.id.current_date);

        String countyCode = getIntent().getStringExtra("county_code");//获取所选县的county_code

        if (!TextUtils.isEmpty(countyCode)) {
            //有县级代号时就去查询天气
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode); //新的数据接口中，countyCode等于weatherCode
            //最终的结果是showWeather

        } else {
            //没有县级代号时就直接显示本地天气
            showWeather();
        }
    }

    /**
     * 查询县级代号对应的天气代号
     */

    private void queryWeatherCode(String countyCode) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromServer(address, "countyCode");
    }

    /**
     * 查询天气代号对应的天气
     */

    private void queryWeatherInfo(String weatherCode) {
        //String address = "http://m.weather.com.cn/data/" + weatherCode + ".html";
        String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        queryFromServer(address, "weatherCode");
    }

    /**
     * 根据传入的地址的类型去向服务器查询天气代号或者天气信息
     */

    private void queryFromServer(final String address, final String type) {

        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

            @Override
            public void onFinish(final String response) {
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        //从服务器返回的数据中解析出天气代号
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherCode".equals(type)) {
                    //处理服务器返回的天气信息
                    Utility.handleWeatherResponse(WeatherActivity.this, response);//解析JSON数据存储到本地以及SharedPreferences
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            showWeather();//从本地的SharedPreferences中取出相应的数据，给控件赋值
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    /**
     * 从SharePreferences 文件中读取存储的天气信息，并显示到界面上。
     */

    private void showWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(preferences.getString("city_name", ""));
        temp1Text.setText(preferences.getString("temp1", ""));
        temp2Text.setText(preferences.getString("temp2", ""));
        weatherDespText.setText(preferences.getString("weather_desp", ""));
        publishText.setText("今天" + preferences.getString("publish_time", "") + "发布");
        currentDateText.setText(preferences.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);


    }


}
