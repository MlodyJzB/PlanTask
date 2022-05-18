package com.app.WeatherInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherInfoTest {

    @Test
    public void testUpdateOfflineDay0() throws IOException, JSONException, NonexistentZipCodeException {
        WeatherInfo wi = new WeatherInfo();
        wi.updateOffline("\\test\\com\\app\\WeatherInfo\\exampleInfo.json", "\\test\\com\\app\\WeatherInfo\\exampleZipCode.json");

        assertEquals(9, wi.getTemp(0));
        assertEquals(6, wi.getFeelsLike());
        assertEquals(26, wi.getWindSpeed(0));
        assertEquals(0, wi.getCloudsValue(0));
        assertEquals("01n", wi.getIcon(0));
        assertEquals(1652821592, wi.getUpdateDate());
        assertEquals(1652755110, wi.getSunrise(0));
        assertEquals(1652811981, wi.getSunset(0));
        assertEquals("Warszawa", wi.getCity());
    }

    @Test
    public void testUpdateOfflineDay1() throws IOException, JSONException, NonexistentZipCodeException {
        WeatherInfo wi = new WeatherInfo();
        wi.updateOffline("\\test\\com\\app\\WeatherInfo\\exampleInfo.json", "\\test\\com\\app\\WeatherInfo\\exampleZipCode.json");

        assertEquals(15, wi.getTemp(1));
        assertEquals(23, wi.getWindSpeed(1));
        assertEquals(100, wi.getCloudsValue(1));
        assertEquals("04d", wi.getIcon(1));
        assertEquals(1652781600, wi.getDate(1));
        assertEquals(1652821592, wi.getUpdateDate());
        assertEquals(9, wi.getMinTemp(1));
        assertEquals(17, wi.getMaxTemp(1));
        assertEquals(1652755110, wi.getSunrise(1));
        assertEquals(1652811981, wi.getSunset(1));
    }

    @Test
    public void testUpdateOfflineDay4() throws IOException, JSONException, NonexistentZipCodeException {
        WeatherInfo wi = new WeatherInfo();
        wi.updateOffline("\\test\\com\\app\\WeatherInfo\\exampleInfo.json", "\\test\\com\\app\\WeatherInfo\\exampleZipCode.json");

        assertEquals(24, wi.getTemp(4));
        assertEquals(21, wi.getWindSpeed(4));
        assertEquals(82, wi.getCloudsValue(4));
        assertEquals("10d", wi.getIcon(4));
        assertEquals(1653040800, wi.getDate(4));
        assertEquals(1652821592, wi.getUpdateDate());
        assertEquals(13, wi.getMinTemp(4));
        assertEquals(26, wi.getMaxTemp(4));
        assertEquals(1653014057, wi.getSunrise(4));
        assertEquals(1653071453, wi.getSunset(4));
    }
}

