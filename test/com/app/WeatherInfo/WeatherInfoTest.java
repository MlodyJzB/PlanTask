package com.app.WeatherInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherInfoTest {
    @Test
    public void testIsCodeFormatCorrect1(){

        String zipCode = "00-631";
        boolean answer = WeatherInfo.IsCodeFormatCorrect(zipCode);
        assertTrue(answer, "Correct code 1");

        zipCode = "00-001";
        answer = WeatherInfo.IsCodeFormatCorrect(zipCode);
        assertTrue( answer, "Correct code 2");

    }
    @Test
    public void testIsCodeFormatCorrect2(){
        String zipCode = "00-0012";
        boolean answer = WeatherInfo.IsCodeFormatCorrect(zipCode);
        assertFalse(answer, "Incorrect code: too long");
    }

    @Test
    public void testIsCodeFormatCorrect3(){
        String zipCode = "00-00a";
        boolean answer = WeatherInfo.IsCodeFormatCorrect(zipCode);
        assertFalse(answer, "Incorrect code: char occur");
    }

    @Test
    public void testIsCodeFormatCorrect4(){
        String zipCode = "00001";
        boolean answer = WeatherInfo.IsCodeFormatCorrect(zipCode);
        assertFalse(answer, "Incorrect code: no dash");
    }

    @Test
    public void testIsCodeFormatCorrect5(){
        String zipCode = "0-001";
        boolean answer = WeatherInfo.IsCodeFormatCorrect(zipCode);
        assertFalse(answer, "Incorrect code: dash in wrong place");
    }

//    @Test
//    public void testGetCoordsFromJson1() throws IOException, JSONException, NonexistentZipCodeException, IncorrectZipCodeFormatException {
//        WeatherInfo wi = new WeatherInfo();
//        wi.setZipCode("00-631");
//        File file = new File("");
//        String path = file.getAbsolutePath() + "\\test\\com\\app\\WeatherInfo\\exampleZipCode.json";
//        JSONObject json = JsonHandling.getFromFile(path);
//        WeatherInfo.Coords coordinates = wi.getCoordsFromJson(json);
//        Double lat = coordinates.getLat();
//        Double lon = coordinates.getLon();
//
//        assertEquals(52.21550000, lat);
//        assertEquals(21.01650000, lon);
//    }
//
//    @Test(expected = NonexistentZipCodeException.class)
//    public void testGetCoordsFromJsonException() throws IOException, JSONException, NonexistentZipCodeException {
//        WeatherInfo wi = new WeatherInfo();
//        File file = new File("");
//        String path = file.getAbsolutePath() + "\\test\\com\\app\\WeatherInfo\\exampleNonexistentZipCode.json";
//        JSONObject json =JsonHandling.getFromFile(path);
//        WeatherInfo.Coords coordinates = wi.getCoordsFromJson(json);
//    }
    @Test
    public void testSetCoords()  {
        WeatherInfo.Coords coordinates = new WeatherInfo.Coords(0, 0);
        assertEquals(0, coordinates.getLat());
        assertEquals(0, coordinates.getLon());

        coordinates.set(1, 1);
        assertEquals(1, coordinates.getLat());
        assertEquals(1, coordinates.getLon());
    }

    @Test
    public void testUpdateFromJson() throws IOException, JSONException, NonexistentZipCodeException {
        WeatherInfo wi = new WeatherInfo();
        wi.updateOffline("\\test\\com\\app\\WeatherInfo\\exampleInfo.json", "\\test\\com\\app\\WeatherInfo\\exampleZipCode.json");

        assertEquals(0.82, wi.getTemp());
        assertEquals(-3, wi.getFeelsLike());
        assertEquals(15, wi.getWindSpeed());
        assertEquals(0, wi.getCloudsValue());
        assertEquals("Warszawa", wi.getCity());
        //assertEquals("clear sky", wi.getWeatherDescription());
    }
}

