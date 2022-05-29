package com.app.WeatherInfo;


import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class JsonHandlingTest {
    @Test
    public void testGetFromFile() throws IOException, JSONException, NonexistentZipCodeException {
        File file = new File("");
        String path = file.getAbsolutePath() + "\\test\\com\\app\\WeatherInfo\\exampleZipCode.json";
        JSONObject json = JsonHandling.getFromFile(path);
        JSONObject zipInfo = json.getJSONObject("results").getJSONArray("00-631").getJSONObject(0);
        String lat = zipInfo.get("latitude").toString();
        String lon = zipInfo.get("longitude").toString();
        assertEquals("52.21550000", lat);
        assertEquals("21.01650000", lon);
    }

    @Test
    public void testWriteToFile() throws JSONException, IOException {
        File file = new File("");
        String path = file.getAbsolutePath() + "\\test\\com\\app\\WeatherInfo\\example.json";

        JSONObject obj = new JSONObject();
        obj.put("test", "passed");

        JsonHandling.writeToFile(obj, path);
        JSONObject testObj = JsonHandling.getFromFile(path);
        String testValue = testObj.get("test").toString();

        assertEquals("passed", testValue);

        JSONObject obj2 = new JSONObject();
        obj2.put("test", "notPassed");
        JsonHandling.writeToFile(obj2, path);
    }

}

