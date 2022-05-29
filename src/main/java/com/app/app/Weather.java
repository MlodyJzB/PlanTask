package com.app.app;

import com.app.WeatherInfo.IncorrectZipCodeFormatException;
import com.app.WeatherInfo.NonexistentZipCodeException;
import com.app.WeatherInfo.WeatherInfo;
import com.app.loginapp.Database;
import com.app.loginapp.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class Weather implements Initializable {
    User user;
    public Weather() throws NonexistentZipCodeException, JSONException, IOException {}
    public void Exit() {System.exit(0);}

    private final WeatherInfo wi = new WeatherInfo();

    private final SimpleDateFormat dayFormater = new SimpleDateFormat("EEEEEEEEE", Locale.US);
    private final SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm dd/MM");
    private ArrayList<Map<String,Label>> dayInfoLabels;

    @FXML
    private Label City, Day1, Day2, Day3, Day4, Day5, Day6, LastUpdate;
    @FXML
    private Label ZipCodeLabel, CityLabel, LastUpdateLabel;
    @FXML
    private ImageView minimalize_button;

    // Today
    @FXML
    private Label Wind, Clouds, Hum, Pres, FeelsLike, Uvi, Dew;
    @FXML
    private Label Temp, Min, Max, Sunset, Sunrise;
    @FXML
    private ImageView Icon;

    // Day1
    @FXML
    private Label Wind1, Clouds1, Hum1, Pres1;
    @FXML
    private Label Temp1, Min1, Max1, Sunset1, Sunrise1;
    @FXML
    private ImageView Icon1;

    //2
    @FXML
    private Label Wind2, Clouds2, Hum2, Pres2;
    @FXML
    private Label Temp2, Min2, Max2, Sunset2, Sunrise2;
    @FXML
    private ImageView Icon2;

    // Day3
    @FXML
    private Label Wind3, Clouds3, Hum3, Pres3;
    @FXML
    private Label Temp3, Min3, Max3, Sunset3, Sunrise3;
    @FXML
    private ImageView Icon3;

    // Day4
    @FXML
    private Label Wind4, Clouds4, Hum4, Pres4;
    @FXML
    private Label Temp4, Min4, Max4, Sunset4, Sunrise4;
    @FXML
    private ImageView Icon4;

    // Day5
    @FXML
    private Label Wind5, Clouds5, Hum5, Pres5;
    @FXML
    private Label Temp5, Min5, Max5, Sunset5, Sunrise5;
    @FXML
    private ImageView Icon5;

    // Day6
    @FXML
    private Label Wind6, Clouds6, Hum6, Pres6;
    @FXML
    private Label Temp6, Min6, Max6, Sunset6, Sunrise6;
    @FXML
    private ImageView Icon6;

    @FXML
    private TextField ZipCodeField;

    @FXML
    private VBox LocationBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = user.getInstance();
        try {
            this.setLocationLabels();
            this.setDayLabelMap();
            this.setInfo();

        } catch (NonexistentZipCodeException|JSONException|FileNotFoundException e) {
            e.printStackTrace();
        }
        minimalize_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                Stage primaryStage = (Stage) minimalize_button.getScene().getWindow();
                primaryStage.setIconified(true);
            }
        });
        try {
            String[] colorArray = new AppPanel().colorArray(user.isDayMode());
            BackCol = colorArray[0];
            NormCol = colorArray[2];
            DiffCol = colorArray[3];
            DarkMode(user.isDayMode());}
        catch (JSONException | IOException | NonexistentZipCodeException e) {e.printStackTrace();}
    }
    private String BackCol, NormCol, DiffCol;
    @FXML
    private AnchorPane diffColor1;
    @FXML
    private HBox normcol1, normcol2, diffColor2, diffColor3, diffColor4, diffColor5, diffColor6, diffColor7, diffColor8;
    @FXML
    private VBox backcolor, normcol3, normcol4, normcol5, normcol6, normcol7, normcol8;
    public void DarkMode(boolean mode){
        VBox[] normalColors = new VBox[]{normcol3, normcol4, normcol5, normcol6, normcol7, normcol8};
        for (VBox a : normalColors) {a.setStyle("-fx-background-color: " + NormCol + "; -fx-background-radius: 10;");}
        HBox[] diffColors = new HBox[]{ diffColor3, diffColor4, diffColor5, diffColor6, diffColor7, diffColor8};
        for (HBox a : diffColors) {a.setStyle("-fx-background-color: " + DiffCol + ";");}
        backcolor.setStyle("-fx-background-color: " + BackCol + "; -fx-background-radius: 0 15 15 0;");
        diffColor1.setStyle("-fx-background-color: " + DiffCol + "; -fx-background-radius: 10;");
        diffColor2.setStyle("-fx-background-color: " + DiffCol + "; -fx-background-radius: 10;");
        normcol1.setStyle("-fx-background-color: " + NormCol + "; -fx-background-radius: 10;");
        normcol2.setStyle("-fx-background-color: " + NormCol + "; -fx-background-radius: 10;");
    }

    @FXML
    private ImageView minimalize_button1;

    @FXML
    private void Minimize_clicked() {
        Stage stage = (Stage) minimalize_button1.getScene().getWindow();
        //stage.setIconified(true);
        stage.setMaximized(!stage.isMaximized());
        //Restore down
        stage.setMaximized(stage.isMaximized());
    }

    public void refreshWeather(ActionEvent event) throws JSONException {
        String newZipCode = ZipCodeField.getText();
        String zipCodeNoWhite = newZipCode.replaceAll("\\s+","");
        try {
            if (newZipCode.equals("")) {
                this.wi.updateOnline();
                this.setLastUpdate();
                this.setLocationLabels();
            }
            else{
                this.wi.updateOnline(zipCodeNoWhite);
                this.setLocationLabels(zipCodeNoWhite);
                ZipCodeField.clear();
                Database.changeZip(user.getUsername(), zipCodeNoWhite);
            }
            this.setInfo();

        } catch (IncorrectZipCodeFormatException | NonexistentZipCodeException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Incorrect zip code. Try again!", ButtonType.OK);
            alert.showAndWait();
        } catch(IOException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "No internet connection!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void setDayLabels() throws JSONException {
        long timeDay1 = this.wi.getSunrise(1);
        long timeDay2 = this.wi.getSunrise(2);
        long timeDay3 = this.wi.getSunrise(3);
        long timeDay4 = this.wi.getSunrise(4);
        long timeDay5 = this.wi.getSunrise(5);
        long timeDay6 = this.wi.getSunrise(6);

        String day = this.dayFormater.format((timeDay1)*1000);
        Day1.setText(day);

        day = this.dayFormater.format((timeDay2)*1000);
        Day2.setText(day);

        day = this.dayFormater.format((timeDay3)*1000);
        Day3.setText(day);

        day = this.dayFormater.format((timeDay4)*1000);
        Day4.setText(day);

        day = this.dayFormater.format((timeDay5)*1000);
        Day5.setText(day);

        day = this.dayFormater.format((timeDay6)*1000);
        Day6.setText(day);
    }

    public void setLocationBox() throws JSONException {
        String style;
        LocalDateTime time = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
        long timeNow = time.atZone(zoneId).toEpochSecond();

        String hour = dateFormater.format(timeNow*1000L).substring(0,2);
        String sunsetHour = dateFormater.format(this.wi.getSunset(0)*1000L).substring(0,2);
        String sunriseHour = dateFormater.format(this.wi.getSunrise(0)*1000L).substring(0,2);

        if((Integer.valueOf(hour) >= Integer.valueOf(sunsetHour))||(Integer.valueOf(hour) < Integer.valueOf(sunriseHour))){
            style = "-fx-background-color: #0d104a; -fx-background-radius: 10;";
            City.setTextFill(Color.WHITE);
            LastUpdate.setTextFill(Color.WHITE);
            ZipCodeLabel.setTextFill(Color.WHITE);
            CityLabel.setTextFill(Color.WHITE);
            LastUpdateLabel.setTextFill(Color.WHITE);
        }
        else{
            style = "-fx-background-color: #84def5; -fx-background-radius: 10;";
            City.setTextFill(Color.BLACK);
            LastUpdate.setTextFill(Color.BLACK);
            ZipCodeLabel.setTextFill(Color.BLACK);
            CityLabel.setTextFill(Color.BLACK);
            LastUpdateLabel.setTextFill(Color.BLACK);
        }
        LocationBox.setStyle(style);

    }

    public void setLocationLabels() throws NonexistentZipCodeException, JSONException{
        ZipCodeField.setPromptText(wi.getZipCode());
        City.setText(wi.getCity());
        this.setLastUpdate();
    }
    public void setLocationLabels(String zipCode) throws NonexistentZipCodeException, JSONException {
        ZipCodeField.setPromptText(zipCode);
        City.setText(wi.getCity());
        this.setLastUpdate();
    }

    public void setLastUpdate() throws NonexistentZipCodeException, JSONException{
        long lastUpdateTime = this.wi.getUpdateDate();

        String lastUpdate = this.dateFormater.format((lastUpdateTime)*1000L);
        LastUpdate.setText(lastUpdate);
    }

    public void setTodayInfoLabels() throws JSONException, FileNotFoundException {
        this.setDayInfoLabels(0, Icon);

        String feelsLike = String.valueOf(this.wi.getFeelsLike());
        String uvi = String.valueOf(this.wi.getUvi());
        String dewPoint = String.valueOf(this.wi.getDewPoint());

        FeelsLike.setText(feelsLike + "°C");
        Uvi.setText(uvi);
        Dew.setText(dewPoint + "°C");
    }

    public void setDayInfoLabels(int day, ImageView image) throws JSONException, FileNotFoundException {
        Map<String, Label> dayInfo = this.dayInfoLabels.get(day);

        String temp = String.valueOf(this.wi.getTemp(day));
        dayInfo.get("temp").setText(temp + "°C");

        String minTemp = String.valueOf(this.wi.getMinTemp(day));
        dayInfo.get("min").setText(minTemp + "°C");

        String maxTemp = String.valueOf(this.wi.getMaxTemp(day));
        dayInfo.get("max").setText(maxTemp + "°C");

        String wind = String.valueOf(this.wi.getWindSpeed(day));
        dayInfo.get("wind").setText(wind + " km/h");

        String clouds = String.valueOf(this.wi.getCloudsValue(day));
        dayInfo.get("clouds").setText(clouds + " %");

        String humidity = String.valueOf(this.wi.getHumidity(day));
        dayInfo.get("hum").setText(humidity + " %");

        String pres = String.valueOf(this.wi.getPressureValue(day));
        dayInfo.get("pres").setText(pres + " hPa");

        String sunriseUnix = String.valueOf(this.wi.getSunrise(day));
        String sunriseHour = this.dateFormater.format(Long.valueOf(sunriseUnix)*1000L).substring(0,5);
        dayInfo.get("sunrise").setText(sunriseHour);

        String sunsetUnix = String.valueOf(this.wi.getSunset(day));
        String sunsetHour = this.dateFormater.format(Long.valueOf(sunsetUnix)*1000L).substring(0,5);
        dayInfo.get("sunset").setText(sunsetHour);

        String icon = String.valueOf(this.wi.getIcon(day));

        Image im = null;
        im = new Image(new FileInputStream("src/main/resources/Images/WeatherIcons/" + icon + ".png"));
        image.setImage(im);
    }

    private void setInfo() throws NonexistentZipCodeException, JSONException, FileNotFoundException {
        this.setDayLabels();
        this.setLocationBox();
        this.setTodayInfoLabels();
        this.setDayInfoLabels(1, Icon1);
        this.setDayInfoLabels(2, Icon2);
        this.setDayInfoLabels(3, Icon3);
        this.setDayInfoLabels(4, Icon4);
        this.setDayInfoLabels(5, Icon5);
        this.setDayInfoLabels(6, Icon6);
    }

    public void setDayLabelMap(){
        ArrayList<Map<String,Label>> dayInfoLabels = new ArrayList<>();
        Map<String, Label> day0= new HashMap<>();
        Map<String, Label> day1= new HashMap<>();
        Map<String, Label> day2= new HashMap<>();
        Map<String, Label> day3= new HashMap<>();
        Map<String, Label> day4= new HashMap<>();
        Map<String, Label> day5= new HashMap<>();
        Map<String, Label> day6= new HashMap<>();

        // Add day1
        day0.put("temp", Temp);
        day0.put("min", Min);
        day0.put("max", Max);
        day0.put("wind", Wind);
        day0.put("clouds", Clouds);
        day0.put("hum", Hum);
        day0.put("pres", Pres);
        day0.put("sunset", Sunset);
        day0.put("sunrise", Sunrise);
        dayInfoLabels.add(day0);

        // Add day1
        day1.put("temp", Temp1);
        day1.put("min", Min1);
        day1.put("max", Max1);
        day1.put("wind", Wind1);
        day1.put("clouds", Clouds1);
        day1.put("hum", Hum1);
        day1.put("pres", Pres1);
        day1.put("sunset", Sunset1);
        day1.put("sunrise", Sunrise1);
        dayInfoLabels.add(day1);

        // Add day2
        day2.put("temp", Temp2);
        day2.put("min", Min2);
        day2.put("max", Max2);
        day2.put("wind", Wind2);
        day2.put("clouds", Clouds2);
        day2.put("hum", Hum2);
        day2.put("pres", Pres2);
        day2.put("sunset", Sunset2);
        day2.put("sunrise", Sunrise2);
        dayInfoLabels.add(day2);

        // Add day3
        day3.put("temp", Temp3);
        day3.put("min", Min3);
        day3.put("max", Max3);
        day3.put("wind", Wind3);
        day3.put("clouds", Clouds3);
        day3.put("hum", Hum3);
        day3.put("pres", Pres3);
        day3.put("sunset", Sunset3);
        day3.put("sunrise", Sunrise3);
        dayInfoLabels.add(day3);

        // Add day4
        day4.put("temp", Temp4);
        day4.put("min", Min4);
        day4.put("max", Max4);
        day4.put("wind", Wind4);
        day4.put("clouds", Clouds4);
        day4.put("hum", Hum4);
        day4.put("pres", Pres4);
        day4.put("sunset", Sunset4);
        day4.put("sunrise", Sunrise4);
        dayInfoLabels.add(day4);

        day5.put("temp", Temp5);
        day5.put("min", Min5);
        day5.put("max", Max5);
        day5.put("wind", Wind5);
        day5.put("clouds", Clouds5);
        day5.put("hum", Hum5);
        day5.put("pres", Pres5);
        day5.put("sunset", Sunset5);
        day5.put("sunrise", Sunrise5);
        dayInfoLabels.add(day5);

        // Add day4
        day6.put("temp", Temp6);
        day6.put("min", Min6);
        day6.put("max", Max6);
        day6.put("wind", Wind6);
        day6.put("clouds", Clouds6);
        day6.put("hum", Hum6);
        day6.put("pres", Pres6);
        day6.put("sunset", Sunset6);
        day6.put("sunrise", Sunrise6);
        dayInfoLabels.add(day6);

        this.dayInfoLabels = dayInfoLabels;
    }

}
