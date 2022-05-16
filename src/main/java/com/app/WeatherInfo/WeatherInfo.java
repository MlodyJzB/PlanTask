package com.app.WeatherInfo;

import org.json.JSONException;
import java.io.File;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONArray;

public class WeatherInfo {

    private String zipCode;
    private String city;
    private double temp = 0;
    private double feelsLike = 0;

    private int windSpeed = 0;

    private double cloudsValue = 0;

    private String icon = "";

    private Coords coordinates = new Coords(0, 0);


    public void setZipCode(String zipCode) throws IncorrectZipCodeFormatException{
        if (!IsCodeFormatCorrect(zipCode)) throw new IncorrectZipCodeFormatException("Incorect zipCode format; expected XX-XXX, but was: " + zipCode);
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return this.zipCode;
    }
    public double getTemp() {
        return this.temp;
    }

    public double getFeelsLike() {
        return this.feelsLike;
    }

    public double getCloudsValue() {
        return this.cloudsValue;
    }

    public double getWindSpeed() {
        return this.windSpeed;
    }

    public String getIcon() {
        return this.icon;
    }

    public String getCity() {
        return this.city;
    }


    public static class Coords{


        public Coords(double lat, double lon){
            this.lat = lat;
            this.lon = lon;
        }
        private double lat;
        private double lon;

        public void set(double lat, double lon){
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat(){
            return this.lat;
        }

        public double getLon(){
            return this.lon;
        }
    }

    public static boolean IsCodeFormatCorrect(String zipCode){

        if(zipCode.length() != 6) return false;

        for(int i = 0; i <6; i++){
            char ch = zipCode.charAt(i);

            if ((i==2)&&(ch!='-')) return false;

            if ((i!=2)&&(!Character.isDigit(ch))) return false;
        }

        return true;
    }


    public WeatherInfo.Coords getCoordsFromJson(JSONObject json) throws JSONException, NonexistentZipCodeException{
        JSONObject results;
        try{
            results = json.getJSONObject("results");
        }
        catch (JSONException a){ throw new NonexistentZipCodeException("Zipcode " + this.getZipCode() + " do not exist.");}
        JSONArray arr = results.getJSONArray(this.getZipCode());
        double lat = Double.valueOf(arr.getJSONObject(0).get("latitude").toString());
        double lon = Double.valueOf(arr.getJSONObject(0).get("longitude").toString());
        Coords coordinates = new Coords(lat, lon);
        return coordinates;
    }

    public void updateTemp(JSONObject json) throws JSONException{
        Object tempJson = json.getJSONObject("main").get("temp");

        Double temp = Double.valueOf(tempJson.toString());
        this.temp = temp;
    }

    public void updateFeelsLike(JSONObject json) throws JSONException{
        Object tempJson = json.getJSONObject("main").get("feels_like");

        Double feelsLike = Double.valueOf(tempJson.toString());
        this.feelsLike = feelsLike;
    }

    public void updateWindSpeed(JSONObject json) throws JSONException{
        Object tempJson = json.getJSONObject("wind").get("speed");

        int windSpeed = (int) Math.round(Double.valueOf(tempJson.toString())*3.6);
        this.windSpeed = windSpeed;
    }

    public void updateCloudsValue(JSONObject json) throws JSONException{
        Object tempJson = json.getJSONObject("clouds").get("all");

        Double cloudsValue = Double.valueOf(tempJson.toString());
        this.cloudsValue = cloudsValue;
    }

    public void updateIcon(JSONObject json) throws JSONException{
        Object tempJson = json.getJSONArray("weather").getJSONObject(0).get("icon");

        String icon = tempJson.toString();
        this.icon = icon;
    }

    public void updateCity(JSONObject json) throws JSONException, NonexistentZipCodeException {
        JSONObject results;
        try{
            results = json.getJSONObject("results");
        }
        catch (JSONException a){
            this.city = "";
            throw new NonexistentZipCodeException("Zipcode " + this.getZipCode() + " do not exist.");
        }
        JSONArray arr = results.getJSONArray(this.getZipCode());
        String city = arr.getJSONObject(0).get("city").toString();
        this.city = city;
    }

    public void updateZipCode(String zipCode) throws IncorrectZipCodeFormatException, java.io.IOException, JSONException, NonexistentZipCodeException {
        this.setZipCode(zipCode);
        JsonHandling handle = new JsonHandling();
        JSONObject zipInfo = handle.getFromUrl("https://app.zipcodebase.com/api/v1/search?apikey=f6178de0-b1e6-11ec-ad2d-a971b4172138&codes=" + zipCode);
        File file = new File("");
        String path = file.getAbsolutePath() + "\\src\\main\\java\\com\\app\\WeatherInfo\\zipCode.json";
        this.coordinates = this.getCoordsFromJson(zipInfo);
        handle.writeToFile(zipInfo, path);
    }
    public void updateInfoJson() throws JSONException, IOException {
        JsonHandling handle = new JsonHandling();
        JSONObject zipInfo = handle.getFromUrl("https://api.openweathermap.org/data/2.5/weather?lat=" + this.coordinates.getLat() + "&lon=" + this.coordinates.getLon() +"&appid=3604feeeebf154336d2e624506e5b388&units=metric");
        File file = new File("");
        String path = file.getAbsolutePath() + "\\src\\main\\java\\com\\app\\WeatherInfo\\Info.json";
        handle.writeToFile(zipInfo, path);
    }

    public void updateFromJson() throws JSONException, IOException, NonexistentZipCodeException {
        File file = new File("");
        String path = file.getAbsolutePath() + "\\src\\main\\java\\com\\app\\WeatherInfo\\info.json";
        JSONObject json = JsonHandling.getFromFile(path);
        this.updateTemp(json);
        this.updateFeelsLike(json);
        this.updateWindSpeed(json);
        this.updateCloudsValue(json);
        this.updateIcon(json);

        path = file.getAbsolutePath() + "\\src\\main\\java\\com\\app\\WeatherInfo\\zipCode.json";
        json = JsonHandling.getFromFile(path);
        this.readZipCode(json);
        this.updateCity(json);
    }

    public void readZipCode(JSONObject json) throws NonexistentZipCodeException {
        JSONObject results;
        try{
            results = json.getJSONObject("results");
        }
        catch (JSONException e){
            this.zipCode = " ";
            throw new NonexistentZipCodeException("Zipcode " + this.getZipCode() + " do not exist.");
        }
        this.zipCode = results.keys().next().toString();
    }
    public void updateFromJson(String otherPath) throws JSONException, IOException, NonexistentZipCodeException {
        File file = new File("");
        String path = file.getAbsolutePath() + otherPath;
        JSONObject json = JsonHandling.getFromFile(path);
        this.updateTemp(json);
        this.updateFeelsLike(json);
        this.updateWindSpeed(json);
        this.updateCloudsValue(json);
        this.updateIcon(json);
        this.readZipCode(json);

        path = file.getAbsolutePath() + "\\src\\main\\java\\com\\app\\WeatherInfo\\zipCode.json";
        json = JsonHandling.getFromFile(path);
        this.readZipCode(json);
        this.updateCity(json);
    }

    public void update() throws JSONException, IOException, NonexistentZipCodeException {
        this.updateInfoJson();
        this.updateFromJson();
    }

    public void update(String zipCode) throws JSONException, IOException, NonexistentZipCodeException, IncorrectZipCodeFormatException {
        this.updateZipCode(zipCode);
        this.update();
    }

}

