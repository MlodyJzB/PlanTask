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
        String pathStr = path.toAbsolutePath().toString() + "/bin/WeatherInfo/zipCode.json";
        JSONObject json = WeatherInfo.fromJsonFile(pathStr);
        WeatherInfo.Coords coordinates = WeatherInfo.getCoords(json, "00-631");
        Double lat = coordinates.getLat();
        Double lon = coordinates.getLon();

        assertEquals(52.21550000, lat);
        assertEquals(21.01650000, lon);
}

    @Test
    public void testGetCoords2() throws IOException, JSONException, WeatherInfo.NonexistentZipCodeException{
        Path path = Paths.get(".");
        String pathStr = path.toAbsolutePath().toString() + "/bin/WeatherInfo/nonexistentZipCode.json";
        JSONObject json = WeatherInfo.fromJsonFile(pathStr);
        WeatherInfo.Coords coordinates = WeatherInfo.getCoords(json, "16-197");
        Double lat = coordinates.getLat();
        Double lon = coordinates.getLon();

        assertEquals(null, lat);
        assertEquals(null, lon);
    }

    @Test(expected = WeatherInfo.NonexistentZipCodeException.class)
    public void whenExceptionThrown_thenExpectationSatisfied() throws IOException, JSONException, WeatherInfo.NonexistentZipCodeException {
        Path path = Paths.get(".");
        String pathStr = path.toAbsolutePath().toString() + "/bin/WeatherInfo/nonexistentZipCode.json";
        JSONObject json = WeatherInfo.fromJsonFile(pathStr);
        WeatherInfo.Coords coordinates = WeatherInfo.getCoords(json, "16-197");
}


}