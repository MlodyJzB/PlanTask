package com.app.WeatherInfo;

import org.json.JSONException;
import java.io.File;

import org.json.JSONObject;
import org.json.JSONArray;

public class WeatherInfo {
    public static class NonexistentZipCodeException 
    extends ZipCodeException {
      public NonexistentZipCodeException(String errorMessage) {
          super(errorMessage);
      }
  }
  
      public static class IncorrectZipCodeFormatException
    extends ZipCodeException {
      public IncorrectZipCodeFormatException(String errorMessage) {
          super(errorMessage);
      }
  }

    private String zipCode = "00-001";
    private final Coords coordinates = new Coords(0, 0);

    public static class ZipCodeException 
  extends Exception {
    public ZipCodeException(String errorMessage) {
        super(errorMessage);
    }
}

    public void setZipCode(String zipCode) throws WeatherInfo.IncorrectZipCodeFormatException{
        if (!IsCodeFormatCorrect(zipCode)) throw new IncorrectZipCodeFormatException("Incorect zipCode format; expected XX-XXX, but was: " + zipCode);
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return this.zipCode;
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


    public static WeatherInfo.Coords getCoords(JSONObject json, String zipcode) throws JSONException, NonexistentZipCodeException{
        JSONObject results;
        try{
        results = json.getJSONObject("results");
        }
        catch (JSONException a){ throw new NonexistentZipCodeException("Zipcode " + zipcode + " do not exist.");}
        JSONArray arr = results.getJSONArray(zipcode);
        double lat = Double.valueOf(arr.getJSONObject(0).get("latitude").toString());
        double lon = Double.valueOf(arr.getJSONObject(0).get("longitude").toString());
        Coords coordinates = new Coords(lat, lon);
        return coordinates;
    }

    public static Double getTemp(JSONObject json) throws JSONException{
        Object tempJson = json.getJSONObject("main").get("temp");

        Double temp = Double.valueOf(tempJson.toString());
        return temp;
    }
    public void updateZipCode(String zipCode) throws IncorrectZipCodeFormatException, java.io.IOException, JSONException{
        this.zipCode = zipCode;
        JsonHandling handle = new JsonHandling();
        JSONObject zipInfo = handle.getFromUrl("https://app.zipcodebase.com/api/v1/search?apikey=f6178de0-b1e6-11ec-ad2d-a971b4172138&codes=" + zipCode);
        File file = new File("");
        String path = file.getAbsolutePath() + "\\src\\main\\java\\com\\app\\WeatherInfo\\zipCode.json";
        handle.writeToFile(zipInfo, path);
    }



}
