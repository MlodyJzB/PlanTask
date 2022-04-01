package WeatherAPI;

public class WeatherApi {
    public static boolean IsCodeFormatCorrect(String zipCode){

        if(zipCode.length() != 6) return false;

        for(int i = 0; i <6; i++){
            char ch = zipCode.charAt(i);

            if ((i==2)&&(ch!='-')) return false;

            if ((i!=2)&&(!Character.isDigit(ch))) return false;
        }

        return true;
    }
}
