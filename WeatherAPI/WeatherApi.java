package WeatherAPI;

import org.json.JSONException;
import java.io.IOException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;

public class WeatherApi {

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
        jsonStr = new Scanner(inStream, "UTF-8").useDelimiter("\\Z").next();
        JSONObject json = new JSONObject(jsonStr);
        return json;
    }

    public static String getCoords(JSONObject json, String zipcode) throws JSONException{
        JSONObject results = json.getJSONObject("results");
        JSONArray arr = results.getJSONArray(zipcode);
        String lat = arr.getJSONObject(0).get("latitude").toString();
        return lat;
    }

}
