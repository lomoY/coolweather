package com.example.lomo.coolweather.model;

/**
 * Created by Lomo on 15/5/23.
 */
public class County {
    private int id;
    private String countyName;
    private String countyCode;
    private int cityId;


    public int getId() {
        return id;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public int getCityId() {
        return cityId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
