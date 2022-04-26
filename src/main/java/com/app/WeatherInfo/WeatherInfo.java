package com.app.WeatherInfo;

import org.json.JSONException;
import java.io.File;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONArray;

public class WeatherInfo {

    private String zipCode = "00-001";
    private double temp = 0;
    private double maxTemp = 0;
    private double minTemp = 0;
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

    public double getMinTemp() {
        return this.minTemp;
    }

    public double getMaxTemp() {
        return this.maxTemp;
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

    public void updateMinTemp(JSONObject json) throws JSONException{
        Object tempJson = json.getJSONObject("main").get("temp_min");

        Double minTemp = Double.valueOf(tempJson.toString());
        this.minTemp = minTemp;
    }

    public void updateMaxTemp(JSONObject json) throws JSONException{
        Object tempJson = json.getJSONObject("main").get("temp_max");

        Double maxTemp = Double.valueOf(tempJson.toString());
        this.maxTemp = maxTemp;
    }

    public void updateZipCode() throws IncorrectZipCodeFormatException, java.io.IOException, JSONException, NonexistentZipCodeException {
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

    public void updateFromJson() throws JSONException, IOException {
        File file = new File("");
        String path = file.getAbsolutePath() + "\\src\\main\\java\\com\\app\\WeatherInfo\\info.json";
        JSONObject json = JsonHandling.getFromFile(path);
        this.updateTemp(json);
        this.updateMinTemp(json);
        this.updateMaxTemp(json);
    }
    public void updateFromJson(String pathFromRoot) throws JSONException, IOException {
        File file = new File("");
        String path = file.getAbsolutePath() + pathFromRoot;
        JSONObject json = JsonHandling.getFromFile(path);
        this.updateTemp(json);
        this.updateMinTemp(json);
        this.updateMaxTemp(json);
    }

}
class ZipCodeException
        extends Exception {
    public ZipCodeException(String errorMessage) {
        super(errorMessage);
    }
}

class NonexistentZipCodeException
        extends ZipCodeException {
    public NonexistentZipCodeException(String errorMessage) {
        super(errorMessage);
    }

}

class IncorrectZipCodeFormatException
        extends ZipCodeException {
    public IncorrectZipCodeFormatException(String errorMessage) {
        super(errorMessage);
    }
}

