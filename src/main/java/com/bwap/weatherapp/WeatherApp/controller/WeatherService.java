package com.bwap.weatherapp.WeatherApp.controller;

import elemental.json.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WeatherService {

    private OkHttpClient client;
    private Response response;
    private String CityName;
    private String unit;
    private String API = "2986700aaab44dc38ba142514220303";


    public JSONObject getWeather(){
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.weatherapi.com/v1/current.json?key=2986700aaab44dc38ba142514220303&q="+getCityName()+"&aqi=no")
                .build();

        try {
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        }catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return null;
    }


    public JSONObject returnCurrentInfo() throws JSONException {
        JSONObject currentInfo = getWeather().getJSONObject("current");
        return currentInfo;
    }

    public JSONObject returnConditionInfo() throws JSONException {
        JSONObject currentInfo = getWeather().getJSONObject("current");
        JSONObject conditionInfo = currentInfo.getJSONObject("condition");
        return conditionInfo;
    }


    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
