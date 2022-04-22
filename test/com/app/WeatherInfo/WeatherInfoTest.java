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

    @Test
    public void testGetCoords1() throws IOException, JSONException, WeatherInfo.NonexistentZipCodeException{
        File file = new File("");
        String path = file.getAbsolutePath() + "\\test\\com\\app\\WeatherInfo\\exampleZipCode.json";
        JSONObject json = JsonHandling.getFromFile(path);
        WeatherInfo.Coords coordinates = WeatherInfo.getCoords(json, "00-631");
        Double lat = coordinates.getLat();
        Double lon = coordinates.getLon();

        assertEquals(52.21550000, lat);
        assertEquals(21.01650000, lon);
    }

    @Test(expected = WeatherInfo.NonexistentZipCodeException.class)
    public void testGetCoordsException() throws IOException, JSONException, WeatherInfo.NonexistentZipCodeException {
        File file = new File("");
        String path = file.getAbsolutePath() + "\\test\\com\\app\\WeatherInfo\\exampleNonexistentZipCode.json";
        JSONObject json =JsonHandling.getFromFile(path);
        WeatherInfo.Coords coordinates = WeatherInfo.getCoords(json, "16-197");
    }
    @Test(expected =WeatherInfo.IncorrectZipCodeFormatException.class)
    public void  testSetZipCodeIncorrectZipCodeFormatException() throws WeatherInfo.IncorrectZipCodeFormatException {
        WeatherInfo wi = new WeatherInfo();
        assertEquals("00-001", wi.getZipCode());

        wi.setZipCode("00-00a");
    }

    @Test
    public void testSetZipCode() throws WeatherInfo.IncorrectZipCodeFormatException {
        WeatherInfo wi = new WeatherInfo();
        assertEquals("00-001", wi.getZipCode());

        wi.setZipCode("00-631");
        assertEquals("00-631", wi.getZipCode());
    }
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
    public void testGetTemp() throws IOException, JSONException {
        File file = new File("");
        String path = file.getAbsolutePath() + "\\test\\com\\app\\WeatherInfo\\exampleInfo.json";
        JSONObject json =JsonHandling.getFromFile(path);
        Double temp = WeatherInfo.getTemp(json);
        assertEquals(0.82, temp);
    }
}