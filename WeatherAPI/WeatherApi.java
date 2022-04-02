package WeatherAPI;

import org.json.JSONException;
import java.io.IOException;

import java.io.InputStream;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.io.IOUtils;

public class WeatherApi {

    public static class Coords{
        
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

    public static boolean IsCodeFormatCorrect(String zipCode){

        if(zipCode.length() != 6) return false;

        for(int i = 0; i <6; i++){
            char ch = zipCode.charAt(i);

            if ((i==2)&&(ch!='-')) return false;

            if ((i!=2)&&(!Character.isDigit(ch))) return false;
        }

        return true;
    }

    public static JSONObject getCoordsJson(String zipCode) throws IOException, JSONException{
        String jsonStr = null;
        URL jsonUrl = new URL("https://app.zipcodebase.com/api/v1/search?apikey=f6178de0-b1e6-11ec-ad2d-a971b4172138&codes=" + zipCode);
        HttpURLConnection connection = (HttpURLConnection) jsonUrl.openConnection();
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("charset", "utf-8");
        connection.connect();
        InputStream inStream = connection.getInputStream();
        Scanner scanner = new Scanner(inStream, "UTF-8");
        jsonStr = scanner.useDelimiter("\\Z").next();
        scanner.close();
        JSONObject json = new JSONObject(jsonStr);
        return json;
    }

    public static JSONObject fromJsonFile(String path) throws FileNotFoundException, IOException, JSONException{
        File f = new File(path);
        JSONObject emptyJson = new JSONObject();
        if (!f.exists()) return emptyJson;
        InputStream is = new FileInputStream(path);
        String jsonTxt = IOUtils.toString(is, "UTF-8"); 
        JSONObject json = new JSONObject(jsonTxt); 
        return json;
    }


    public static WeatherApi.Coords getCoords(JSONObject json, String zipcode) throws JSONException{
        JSONObject results = json.getJSONObject("results");
        JSONArray arr = results.getJSONArray(zipcode);
        Double lat = Double.valueOf(arr.getJSONObject(0).get("latitude").toString());
        Double lon = Double.valueOf(arr.getJSONObject(0).get("longitude").toString());
        Coords coordinates = new Coords(lat, lon);
        return coordinates;
    }

}