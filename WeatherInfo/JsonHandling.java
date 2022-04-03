package WeatherInfo;

import java.io.IOException;
import org.json.JSONException;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.io.IOUtils;

import java.io.FileWriter;

public class JsonHandling {

    public static JSONObject getFromUrl(String url) throws IOException, JSONException{
        String jsonStr = null;
        URL jsonUrl = new URL(url);
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

    public static JSONObject getFromFile(String path) throws FileNotFoundException, IOException, JSONException{
        File f = new File(path);
        if (!f.exists()) throw new FileNotFoundException();
        InputStream is = new FileInputStream(path);
        String jsonTxt = IOUtils.toString(is, "UTF-8"); 
        JSONObject json = new JSONObject(jsonTxt); 
        return json;
    }

    public static void writeToFile(JSONObject json, String path) throws IOException{
        FileWriter file = new FileWriter(path);

        try {
            file.write(json.toString());
 
        } catch (IOException e) {
            e.printStackTrace();
 
        } finally {
            file.flush();
            file.close();
        }
    }
}
