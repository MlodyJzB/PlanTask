package WeatherInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class JsonHandlingTest{

    @Test
    public void testFromJsonFile() throws IOException, JSONException, WeatherInfo.NonexistentZipCodeException{
        Path path = Paths.get(".");
        String pathStr = path.toAbsolutePath().toString() + "/bin/WeatherInfo/zipCode.json";
        JSONObject json = JsonHandling.fromJsonFile(pathStr);
        WeatherInfo.Coords coordinates = WeatherInfo.getCoords(json, "00-631");
        Double lat = coordinates.getLat();
        Double lon = coordinates.getLon();

        assertEquals(52.21550000, lat);
        assertEquals(21.01650000, lon);
}


}