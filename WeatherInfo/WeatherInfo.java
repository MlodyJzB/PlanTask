package WeatherInfo;

import org.json.JSONException;

import org.json.JSONObject;
import org.json.JSONArray;

public class WeatherInfo {
    public static class NonexistentZipCodeException 
    extends ZipCodeException {
      public NonexistentZipCodeException(String errorMessage) {
          super(errorMessage);
      }
  }
  
      public static class IncorectZipCodeFormatException 
    extends ZipCodeException {
      public IncorectZipCodeFormatException(String errorMessage) {
          super(errorMessage);
      }
  }

    private String zipCode = "00-001";
    private Coords coordinates = new Coords(0, 0);

    public static class ZipCodeException 
  extends Exception {
    public ZipCodeException(String errorMessage) {
        super(errorMessage);
    }
}

    public void setZipCode(String zipCode) throws WeatherInfo.IncorectZipCodeFormatException{
        if (!IsCodeFormatCorrect(zipCode)) throw new IncorectZipCodeFormatException("Incorect zipCode format; expected XX-XXX, but was: " + zipCode);
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public static class Coords{

        
        public Coords(double lat, double lon){
            this.lat = lat;
            this.lon = lon;
        }
        private double lat;
        private double lon;

        public void set(double lat, double lon){
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat(){
            return this.lat;
        }

        public double getLon(){
            return this.lon;
        }
    }

    public static boolean IsCodeFormatCorrect(String zipCode){

        if(zipCode.length() != 6) return false;

        for(int i = 0; i <6; i++){
            char ch = zipCode.charAt(i);

            if ((i==2)&&(ch!='-')) return false;

            if ((i!=2)&&(!Character.isDigit(ch))) return false;
        }

        return true;
    }


    public static WeatherInfo.Coords getCoords(JSONObject json, String zipcode) throws JSONException, NonexistentZipCodeException{
        JSONObject results = new JSONObject();
        try{
        results = json.getJSONObject("results");
        }
        catch (JSONException a){ throw new NonexistentZipCodeException("Zipcode " + zipcode + " do not exist.");}
        JSONArray arr = results.getJSONArray(zipcode);
        Double lat = Double.valueOf(arr.getJSONObject(0).get("latitude").toString());
        Double lon = Double.valueOf(arr.getJSONObject(0).get("longitude").toString());
        Coords coordinates = new Coords(lat, lon);
        return coordinates;
    }


}
