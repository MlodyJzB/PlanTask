package WeatherAPI;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.*;

public class WeatherApiTest{

    @Test
    public void testIsCodeFormatCorrect1(){
        
        String zipCode = "00-631";
        boolean answer = WeatherApi.IsCodeFormatCorrect(zipCode);
        assertEquals(true, answer, "Correct code 1");

        zipCode = "00-001";
        answer = WeatherApi.IsCodeFormatCorrect(zipCode);
        assertEquals(true, WeatherApi.IsCodeFormatCorrect(zipCode), "Correct code 2");

}
    @Test
    public void testIsCodeFormatCorrect2(){
        String zipCode = "00-0012";
        boolean answer = WeatherApi.IsCodeFormatCorrect(zipCode);
        assertEquals(false, answer, "Incorrect code: too long");
}

    @Test
    public void testIsCodeFormatCorrect3(){
        String zipCode = "00-00a";
        boolean answer = WeatherApi.IsCodeFormatCorrect(zipCode);
        assertEquals(false, answer, "Incorrect code: char occur");
}

    @Test
    public void testIsCodeFormatCorrect4(){
        String zipCode = "00001";
        boolean answer = WeatherApi.IsCodeFormatCorrect(zipCode);
        assertEquals(false, answer, "Incorrect code: no dash");
}

    @Test
    public void testIsCodeFormatCorrect5(){
        String zipCode = "0-001";
        boolean answer = WeatherApi.IsCodeFormatCorrect(zipCode);
        assertEquals(false, answer, "Incorrect code: dash in wrong place");
}

}