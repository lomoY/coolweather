package com.example.lomo.coolweather.util;

/**
 * Created by Lomo on 15/5/25.
 */
public interface HttpCallbackListener {

    void onFinish(String response);
    void onError(Exception e);

}
