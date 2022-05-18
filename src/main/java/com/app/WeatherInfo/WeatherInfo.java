package com.app.WeatherInfo;

import org.json.JSONException;
import java.io.File;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONArray;

public class WeatherInfo {

    private Coords coordinates = new Coords(0, 0);

    private JSONObject zipJson;
    private JSONObject infoJson;
    private String zipCode;

    public String getZipCode(){return this.zipCode;}


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

    public Double getTemp() throws JSONException{
        Object tempJson = this.infoJson.getJSONObject("main").get("temp");

        Double temp = Double.valueOf(tempJson.toString());
        return temp;
    }

    public int getFeelsLike() throws JSONException{
        Object tempJson = this.infoJson.getJSONObject("main").get("feels_like");

        int feelsLike = (int) Math.round(Double.valueOf(tempJson.toString()));
        return feelsLike;
    }

    public int getWindSpeed() throws JSONException{
        Object tempJson = this.infoJson.getJSONObject("wind").get("speed");

        int windSpeed = (int) Math.round(Double.valueOf(tempJson.toString())*3.6);
        return windSpeed;
    }

    public int getCloudsValue() throws JSONException{
        Object tempJson = this.infoJson.getJSONObject("clouds").get("all");

        int cloudsValue = (int) Math.round(Double.valueOf(tempJson.toString()));
        return cloudsValue;
    }

    public String getIcon() throws JSONException{
        Object tempJson = this.infoJson.getJSONArray("weather").getJSONObject(0).get("icon");

        String icon = tempJson.toString();
        return icon;
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

    public WeatherInfo.Coords updateCoordsOffline() throws JSONException, NonexistentZipCodeException{
        JSONObject results;
        try{
            results = this.zipJson.getJSONObject("results");
        }
        catch (JSONException a){ throw new NonexistentZipCodeException("Zipcode " + this.zipCode + " do not exist.");}
        JSONArray arr = results.getJSONArray(this.zipCode);
        double lat = Double.valueOf(arr.getJSONObject(0).get("latitude").toString());
        double lon = Double.valueOf(arr.getJSONObject(0).get("longitude").toString());
        Coords coordinates = new Coords(lat, lon);
        return coordinates;
    }

    public WeatherInfo.Coords updateCoordsOnline(JSONObject json) throws JSONException, NonexistentZipCodeException{
        JSONObject results;
        try{
            results = json.getJSONObject("results");
        }
        catch (JSONException a){ throw new NonexistentZipCodeException("Zipcode " + this.zipCode + " do not exist.");}
        JSONArray arr = results.getJSONArray(this.zipCode);
        double lat = Double.valueOf(arr.getJSONObject(0).get("latitude").toString());
        double lon = Double.valueOf(arr.getJSONObject(0).get("longitude").toString());
        Coords coordinates = new Coords(lat, lon);
        return coordinates;
    }


    public void updateInfoJsonOnline() throws JSONException, IOException {
        JsonHandling handle = new JsonHandling();
        JSONObject infoJson = handle.getFromUrl("https://api.openweathermap.org/data/2.5/weather?lat=" + this.coordinates.getLat() + "&lon=" + this.coordinates.getLon() +"&appid=3604feeeebf154336d2e624506e5b388&units=metric");
        File file = new File("");
        String path = file.getAbsolutePath() + "\\src\\main\\java\\com\\app\\WeatherInfo\\Info.json";
        handle.writeToFile(infoJson, path);
        this.infoJson = infoJson;
    }

    public void updateInfoJsonOffline() throws JSONException, IOException, NonexistentZipCodeException {
        File file = new File("");
        String infoPath = file.getAbsolutePath() + "\\src\\main\\java\\com\\app\\WeatherInfo\\info.json";
        JSONObject infoJson = JsonHandling.getFromFile(infoPath);
        this.infoJson = infoJson;
    }

    public void updateInfoJsonOffline(String otherInfoPath) throws JSONException, IOException, NonexistentZipCodeException {
        File file = new File("");
        String infoPath = file.getAbsolutePath() + otherInfoPath;
        JSONObject infoJson = JsonHandling.getFromFile(infoPath);
        this.infoJson = infoJson;
    }

    public void updateZipJsonOnline(String zipCode) throws IncorrectZipCodeFormatException, java.io.IOException, JSONException, NonexistentZipCodeException {
        JsonHandling handle = new JsonHandling();
        JSONObject zipInfo = handle.getFromUrl("https://app.zipcodebase.com/api/v1/search?apikey=f6178de0-b1e6-11ec-ad2d-a971b4172138&codes=" + zipCode);
        File file = new File("");
        String path = file.getAbsolutePath() + "\\src\\main\\java\\com\\app\\WeatherInfo\\zipCode.json";
        handle.writeToFile(zipInfo, path);
        this.zipCode = zipCode;
        this.updateCoordsOnline(zipInfo);
        this.zipJson = zipInfo;
    }

    public void updateZipJsonOffline() throws JSONException, IOException, NonexistentZipCodeException {
        File file = new File("");
        String zipPath = file.getAbsolutePath() + "\\src\\main\\java\\com\\app\\WeatherInfo\\zipCode.json";
        JSONObject zipJson = JsonHandling.getFromFile(zipPath);
        this.zipJson = zipJson;
    }

    public void updateZipJsonOffline(String otherZipPath) throws JSONException, IOException, NonexistentZipCodeException {
        File file = new File("");
        String zipPath = file.getAbsolutePath() + otherZipPath;
        JSONObject zipJson = JsonHandling.getFromFile(zipPath);
        this.zipJson = zipJson;
    }

    public void updateZipCodeOffline() throws NonexistentZipCodeException, JSONException, IOException {
        JSONObject results;
        try{
            results = this.zipJson.getJSONObject("results");
        }
        catch (JSONException e){
            throw new NonexistentZipCodeException("Zipcode " + this.zipCode + " do not exist.");
        }
        this.zipCode = results.keys().next().toString();
    }

    public void updateOnline() throws JSONException, IOException{
        this.updateInfoJsonOnline();
    }

    public void updateOnline(String zipCode) throws JSONException, IOException, NonexistentZipCodeException, IncorrectZipCodeFormatException {
        if (!this.IsCodeFormatCorrect(zipCode)) throw new IncorrectZipCodeFormatException("Incorect zipCode format; expected XX-XXX, but was: " + zipCode);
        this.updateZipJsonOnline(zipCode);
        this.updateInfoJsonOnline();
    }

    public void updateOffline() throws JSONException, IOException, NonexistentZipCodeException {
        this.updateZipJsonOffline();
        this.updateZipCodeOffline();
        this.updateCoordsOffline();
        this.updateInfoJsonOffline();
    }

    public void updateOffline(String otherInfoPath, String otherZipPath) throws JSONException, IOException, NonexistentZipCodeException {
        this.updateZipJsonOffline(otherZipPath);
        this.updateZipCodeOffline();
        this.updateCoordsOffline();
        this.updateInfoJsonOffline(otherInfoPath);
    }

}

