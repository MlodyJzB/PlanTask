package com.app.WeatherInfo;


import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class JsonHandlingTest {
    @Test
    public void testFromJsonFile() throws IOException, JSONException, WeatherInfo.NonexistentZipCodeException{
        File file = new File("");
        String path = file.getAbsolutePath() + "\\test\\com\\app\\WeatherInfo\\exampleZipCode.json";
        JSONObject json = JsonHandling.getFromFile(path);
        WeatherInfo.Coords coordinates = WeatherInfo.getCoords(json, "00-631");
        Double lat = coordinates.getLat();
        Double lon = coordinates.getLon();

        assertEquals(52.21550000, lat);
        assertEquals(21.01650000, lon);
    }
}