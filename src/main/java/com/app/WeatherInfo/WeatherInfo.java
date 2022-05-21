package com.app.WeatherInfo;

import org.json.JSONException;
import java.io.File;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONArray;

public class WeatherInfo {

    public WeatherInfo() throws NonexistentZipCodeException, JSONException, IOException {
        this.updateOffline();
    }
    private Coords coordinates = new Coords(0, 0);

    private JSONObject zipJson;
    private JSONObject infoJson;
    private String zipCode;

    private int updateDate;

    public String getZipCode(){return this.zipCode;}


    private static class Coords{


        public Coords(double lat, double lon){
            this.lat = lat;
            this.lon = lon;
        }
        private double lat;
        private double lon;

        public double getLat(){
            return this.lat;
        }

        public double getLon(){
            return this.lon;
        }
    }

    private boolean IsCodeFormatCorrect(String zipCode){

        if(zipCode.length() != 6) return false;

        for(int i = 0; i <6; i++){
            char ch = zipCode.charAt(i);

            if ((i==2)&&(ch!='-')) return false;

            if ((i!=2)&&(!Character.isDigit(ch))) return false;
        }

        return true;
    }

    public int getTemp(int day) throws JSONException{
        String temp = "";
        if (day == 0) temp = this.infoJson.getJSONObject("current").get("temp").toString();
        else temp = this.infoJson.getJSONArray("daily").getJSONObject(day).getJSONObject("temp").get("day").toString();

        return (int) Math.round(Double.valueOf(temp));
    }

    public int getFeelsLike() throws JSONException{
        String temp = this.infoJson.getJSONObject("current").get("feels_like").toString();

        return (int) Math.round(Double.valueOf(temp));
    }

    public int getWindSpeed(int day) throws JSONException{
        String windSpeed = "";
        if (day == 0) windSpeed = this.infoJson.getJSONObject("current").get("wind_speed").toString();
        else windSpeed = this.infoJson.getJSONArray("daily").getJSONObject(day).get("wind_speed").toString();


        return (int) Math.round(Double.valueOf(windSpeed)*3.6);
    }

    public int getCloudsValue(int day) throws JSONException{
        String cloudsValue = "";
        if (day == 0) cloudsValue = this.infoJson.getJSONObject("current").get("clouds").toString();
        else cloudsValue = this.infoJson.getJSONArray("daily").getJSONObject(day).get("clouds").toString();


        return (int) Math.round(Double.valueOf(cloudsValue));
    }

    public int getHumidityValue(int day) throws JSONException{
        String cloudsValue = "";
        if (day == 0) cloudsValue = this.infoJson.getJSONObject("current").get("clouds").toString();
        else cloudsValue = this.infoJson.getJSONArray("daily").getJSONObject(day).get("clouds").toString();


        return (int) Math.round(Double.valueOf(cloudsValue));
    }

    public int getPressureValue(int day) throws JSONException{
        String cloudsValue = "";
        if (day == 0) cloudsValue = this.infoJson.getJSONObject("current").get("pressure").toString();
        else cloudsValue = this.infoJson.getJSONArray("daily").getJSONObject(day).get("pressure").toString();


        return (int) Math.round(Double.valueOf(cloudsValue));
    }

    public int getHumidity(int day) throws JSONException{
        String cloudsValue = "";
        if (day == 0) cloudsValue = this.infoJson.getJSONObject("current").get("humidity").toString();
        else cloudsValue = this.infoJson.getJSONArray("daily").getJSONObject(day).get("humidity").toString();


        return (int) Math.round(Double.valueOf(cloudsValue));
    }

    public String getIcon(int day) throws JSONException{
        String icon = "";
        if (day == 0) icon = this.infoJson.getJSONObject("current").getJSONArray("weather").getJSONObject(0).get("icon").toString();
        else icon = this.infoJson.getJSONArray("daily").getJSONObject(day).getJSONArray("weather").getJSONObject(0).get("icon").toString();

        return icon;
    }

    public int getUpdateDate() throws JSONException{
        return this.updateDate;
    }

    public int getDate(int day) throws JSONException{
        String date = this.infoJson.getJSONArray("daily").getJSONObject(day).get("dt").toString();

        return Integer.valueOf(date);
    }

    public int getMinTemp(int day) throws JSONException{
        String temp = this.infoJson.getJSONArray("daily").getJSONObject(day).getJSONObject("temp").get("min").toString();

        return (int) Math.round(Double.valueOf(temp));
    }

    public int getMaxTemp(int day) throws JSONException{
        String temp = this.infoJson.getJSONArray("daily").getJSONObject(day).getJSONObject("temp").get("max").toString();

        return (int) Math.round(Double.valueOf(temp));
    }

    public int getSunrise(int day) throws JSONException{
        String temp = "";
        if (day == 0) temp = this.infoJson.getJSONObject("current").get("sunrise").toString();
        else temp = this.infoJson.getJSONArray("daily").getJSONObject(day).get("sunrise").toString();

        return (int) Math.round(Double.valueOf(temp));
    }

    public int getSunset(int day) throws JSONException{
        String temp = "";
        if (day == 0) temp = this.infoJson.getJSONObject("current").get("sunset").toString();
        else temp = this.infoJson.getJSONArray("daily").getJSONObject(day).get("sunset").toString();

        return (int) Math.round(Double.valueOf(temp));
    }

    public String getCity() throws JSONException, NonexistentZipCodeException {
        JSONObject results;
        try{
            results = this.zipJson.getJSONObject("results");
        }
        catch (JSONException a){
            throw new NonexistentZipCodeException("Zipcode " + this.zipCode + " do not exist.");
        }
        JSONArray arr = results.getJSONArray(this.zipCode);
        String city = arr.getJSONObject(0).get("city").toString();
        return city;
    }

    public void updateUpdateDate() throws JSONException{
        String date = this.infoJson.getJSONObject("current").get("dt").toString();

        this.updateDate = Integer.valueOf(date);
    }

    private void updateCoordsOffline() throws JSONException, NonexistentZipCodeException{
        JSONObject results;
        try{
            results = this.zipJson.getJSONObject("results");
        }
        catch (JSONException a){ throw new NonexistentZipCodeException("Zipcode " + this.zipCode + " do not exist.");}
        JSONArray arr = results.getJSONArray(this.zipCode);
        double lat = Double.valueOf(arr.getJSONObject(0).get("latitude").toString());
        double lon = Double.valueOf(arr.getJSONObject(0).get("longitude").toString());
        Coords coordinates = new Coords(lat, lon);
        this.coordinates = coordinates;
    }

    private void updateCoordsOnline(JSONObject json) throws JSONException, NonexistentZipCodeException{
        JSONObject results;
        try{
            results = json.getJSONObject("results");
        }
        catch (JSONException a){ throw new NonexistentZipCodeException("Zipcode " + this.zipCode + " do not exist.");}
        JSONArray arr = results.getJSONArray(this.zipCode);
        double lat = Double.valueOf(arr.getJSONObject(0).get("latitude").toString());
        double lon = Double.valueOf(arr.getJSONObject(0).get("longitude").toString());
        Coords coordinates = new Coords(lat, lon);
        this.coordinates = coordinates;
    }


    private void updateInfoJsonOnline() throws JSONException, IOException, NonexistentZipCodeException {
        JsonHandling handle = new JsonHandling();
        JSONObject infoJson = handle.getFromUrl("https://api.openweathermap.org/data/2.5/onecall?lat=" + this.coordinates.getLat() + "&lon=" + this.coordinates.getLon() +"&appid=3604feeeebf154336d2e624506e5b388&units=metric&exclude=hourly,minutely,alerts");
        File file = new File("");
        String path = file.getAbsolutePath() + "\\src\\main\\java\\com\\app\\WeatherInfo\\Info.json";
        handle.writeToFile(infoJson, path);
        this.infoJson = infoJson;
    }

    private void updateInfoJsonOffline() throws JSONException, IOException, NonexistentZipCodeException {
        File file = new File("");
        String infoPath = file.getAbsolutePath() + "\\src\\main\\java\\com\\app\\WeatherInfo\\info.json";
        JSONObject infoJson = JsonHandling.getFromFile(infoPath);
        this.infoJson = infoJson;
    }

    private void updateInfoJsonOffline(String otherInfoPath) throws JSONException, IOException, NonexistentZipCodeException {
        File file = new File("");
        String infoPath = file.getAbsolutePath() + otherInfoPath;
        JSONObject infoJson = JsonHandling.getFromFile(infoPath);
        this.infoJson = infoJson;
    }

    private void updateZipJsonOnline(String zipCode) throws java.io.IOException, JSONException, NonexistentZipCodeException {
        JsonHandling handle = new JsonHandling();
        JSONObject zipInfo = handle.getFromUrl("https://app.zipcodebase.com/api/v1/search?apikey=f6178de0-b1e6-11ec-ad2d-a971b4172138&codes=" + zipCode);
        File file = new File("");
        String path = file.getAbsolutePath() + "\\src\\main\\java\\com\\app\\WeatherInfo\\zipCode.json";
        updateZipCodeOnline(zipInfo);
        handle.writeToFile(zipInfo, path);
        this.updateCoordsOnline(zipInfo);
        this.zipJson = zipInfo;
    }

    private void updateZipJsonOffline() throws JSONException, IOException, NonexistentZipCodeException {
        File file = new File("");
        String zipPath = file.getAbsolutePath() + "\\src\\main\\java\\com\\app\\WeatherInfo\\zipCode.json";
        JSONObject zipJson = JsonHandling.getFromFile(zipPath);
        this.zipJson = zipJson;
    }

    private void updateZipJsonOffline(String otherZipPath) throws JSONException, IOException, NonexistentZipCodeException {
        File file = new File("");
        String zipPath = file.getAbsolutePath() + otherZipPath;
        JSONObject zipJson = JsonHandling.getFromFile(zipPath);
        this.zipJson = zipJson;
    }

    private void updateZipCodeOffline() throws NonexistentZipCodeException, JSONException, IOException {
        JSONObject results;
        try{
            results = this.zipJson.getJSONObject("results");
        }
        catch (JSONException e){
            throw new NonexistentZipCodeException("Zipcode " + this.zipCode + " do not exist.");
        }
        this.zipCode = results.keys().next().toString();
    }

    private void updateZipCodeOnline(JSONObject zipJson) throws NonexistentZipCodeException, JSONException, IOException {
        JSONObject results;
        try{
            results = zipJson.getJSONObject("results");
            this.zipCode = results.keys().next().toString();
        }
        catch (JSONException e){
            throw new NonexistentZipCodeException("Zipcode " + this.zipCode + " do not exist.");
        }
    }

    public void updateOnline() throws JSONException, IOException, NonexistentZipCodeException {
        this.updateInfoJsonOnline();
        this.updateUpdateDate();
    }

    public void updateOnline(String zipCode) throws JSONException, IOException, NonexistentZipCodeException, IncorrectZipCodeFormatException {
        if (!this.IsCodeFormatCorrect(zipCode)) throw new IncorrectZipCodeFormatException("Incorect zipCode format; expected XX-XXX, but was: " + zipCode);
        this.updateZipJsonOnline(zipCode);
        this.updateInfoJsonOnline();
        this.updateUpdateDate();
    }

    public void updateOffline() throws JSONException, IOException, NonexistentZipCodeException {
        this.updateZipJsonOffline();
        this.updateZipCodeOffline();
        this.updateCoordsOffline();
        this.updateInfoJsonOffline();
        this.updateUpdateDate();
    }

    public void updateOffline(String otherInfoPath, String otherZipPath) throws JSONException, IOException, NonexistentZipCodeException {
        this.updateZipJsonOffline(otherZipPath);
        this.updateZipCodeOffline();
        this.updateCoordsOffline();
        this.updateInfoJsonOffline(otherInfoPath);
        this.updateUpdateDate();
    }

}

