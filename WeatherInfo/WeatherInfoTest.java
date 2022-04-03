package WeatherInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WeatherInfoTest{


    @Test
    public void testIsCodeFormatCorrect1(){
        
        String zipCode = "00-631";
        boolean answer = WeatherInfo.IsCodeFormatCorrect(zipCode);
        assertEquals(true, answer, "Correct code 1");

        zipCode = "00-001";
        answer = WeatherInfo.IsCodeFormatCorrect(zipCode);
        assertEquals(true, WeatherInfo.IsCodeFormatCorrect(zipCode), "Correct code 2");

}
    @Test
    public void testIsCodeFormatCorrect2(){
        String zipCode = "00-0012";
        boolean answer = WeatherInfo.IsCodeFormatCorrect(zipCode);
        assertEquals(false, answer, "Incorrect code: too long");
}

    @Test
    public void testIsCodeFormatCorrect3(){
        String zipCode = "00-00a";
        boolean answer = WeatherInfo.IsCodeFormatCorrect(zipCode);
        assertEquals(false, answer, "Incorrect code: char occur");
}

    @Test
    public void testIsCodeFormatCorrect4(){
        String zipCode = "00001";
        boolean answer = WeatherInfo.IsCodeFormatCorrect(zipCode);
        assertEquals(false, answer, "Incorrect code: no dash");
}

    @Test
    public void testIsCodeFormatCorrect5(){
        String zipCode = "0-001";
        boolean answer = WeatherInfo.IsCodeFormatCorrect(zipCode);
        assertEquals(false, answer, "Incorrect code: dash in wrong place");
}

    @Test
    public void testGetCoords1() throws IOException, JSONException, WeatherInfo.NonexistentZipCodeException{
        Path path = Paths.get(".");
        String pathStr = path.toAbsolutePath().toString() + "/bin/WeatherInfo/exampleZipCode.json";
        JSONObject json = JsonHandling.getFromFile(pathStr);
        WeatherInfo.Coords coordinates = WeatherInfo.getCoords(json, "00-631");
        Double lat = coordinates.getLat();
        Double lon = coordinates.getLon();

        assertEquals(52.21550000, lat);
        assertEquals(21.01650000, lon);
}

    @Test(expected = WeatherInfo.NonexistentZipCodeException.class)
    public void testGetCoordsException() throws IOException, JSONException, WeatherInfo.NonexistentZipCodeException {
        Path path = Paths.get(".");
        String pathStr = path.toAbsolutePath().toString() + "/bin/WeatherInfo/exampleNonexistentZipCode.json";
        JSONObject json =JsonHandling.getFromFile(pathStr);
        WeatherInfo.Coords coordinates = WeatherInfo.getCoords(json, "16-197");
}
    @Test(expected =WeatherInfo.IncorectZipCodeFormatException.class)
    public void  testSetZipCodeIncorectZipCodeFormatException() throws WeatherInfo.IncorectZipCodeFormatException {
        WeatherInfo wi = new WeatherInfo();
        assertEquals("00-001", wi.getZipCode());

        wi.setZipCode("00-00a");
    }

    @Test
    public void testSetZipCode() throws WeatherInfo.IncorectZipCodeFormatException {
        WeatherInfo wi = new WeatherInfo();
        assertEquals("00-001", wi.getZipCode());

        wi.setZipCode("00-631");
        assertEquals("00-631", wi.getZipCode());
}

    public void testSetCoords()  {
        WeatherInfo.Coords coordinates = new WeatherInfo.Coords(0, 0);
        assertEquals(0, coordinates.getLat());
        assertEquals(0, coordinates.getLon());

        coordinates.set(1, 1);
        assertEquals(1, coordinates.getLat());
        assertEquals(1, coordinates.getLon());
    }

    

}