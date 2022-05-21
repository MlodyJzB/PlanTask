package com.app.app;

import com.app.WeatherInfo.IncorrectZipCodeFormatException;
import com.app.WeatherInfo.NonexistentZipCodeException;
import com.app.WeatherInfo.WeatherInfo;
import com.app.WeatherInfo.ZipCodeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import net.fortuna.ical4j.model.Component;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class Weather implements Initializable {

    public Weather() throws NonexistentZipCodeException, JSONException, IOException {
    }

    public void Exit() {
        System.exit(0);
    }

    private WeatherInfo wi = new WeatherInfo();

    private final SimpleDateFormat dayFormater = new SimpleDateFormat("EEEEEEEEE", Locale.US);
    private SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm dd/MM");
    private LocalDateTime time = LocalDateTime.now();
    private ArrayList<Map<String,Label>> dayInfoLabels;

    @FXML
    private Label City, Day1, Day2, Day3, Day4, Day5, Day6, LastUpdate;

    @FXML
    private Label ZipCodeLabel, CityLabel, LastUpdateLabel;

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


    @FXML
    private TextField ZipCodeField;

    @FXML
    private VBox LocationBox;

    @FXML
    private Button refreshButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.setLocationLabels();
            this.setDayLabels();
            this.setLocationBox();
            this.setDayLabelMap();
            this.setDayInfoLabels(1, Icon1);
            this.setDayInfoLabels(2, Icon2);
        } catch (NonexistentZipCodeException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private ImageView minimalize_button;

    @FXML
    private void Minimize_clicked() {
        Stage stage = (Stage) minimalize_button.getScene().getWindow();
        //stage.setIconified(true);
        stage.setMaximized(!stage.isMaximized());
        //Restore down
        stage.setMaximized(stage.isMaximized());
    }

    public void refreshWeather(ActionEvent event) throws JSONException, IOException {
        String newZipCode = ZipCodeField.getText();
        String zipCodeNoWhite = newZipCode.replaceAll("\\s+","");
        try {
            if (newZipCode == "") this.wi.updateOnline();
            else{
                this.wi.updateOnline(zipCodeNoWhite);
                ZipCodeField.setPromptText(zipCodeNoWhite);
                ZipCodeField.clear();
            }
            this.setDayLabels();
            this.setLastUpdate();
            this.setLocationBox();
            this.setDayInfoLabels(1, Icon1);
            this.setDayInfoLabels(2, Icon2);
        } catch (IncorrectZipCodeFormatException | NonexistentZipCodeException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Incorrect zip code. Try again!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void setDayLabels() throws NonexistentZipCodeException, JSONException {
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

    public void setLocationBox() throws NonexistentZipCodeException, JSONException {
        String style;
        time = LocalDateTime.now();
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

    public void setLocationLabels() throws NonexistentZipCodeException, JSONException {
        ZipCodeField.setPromptText(wi.getZipCode());
        City.setText(wi.getCity());
        this.setLastUpdate();
    }

    public void setLastUpdate() throws NonexistentZipCodeException, JSONException{
        long lastUpdateTime = this.wi.getUpdateDate();

        String lastUpdate = this.dateFormater.format((lastUpdateTime)*1000L);
        LastUpdate.setText(lastUpdate);
    }

    public void setDayInfoLabels(int day, ImageView image) throws JSONException, FileNotFoundException {
        Map<String, Label> dayInfo = this.dayInfoLabels.get(day-1);

        String temp = String.valueOf(this.wi.getTemp(day));
        dayInfo.get("temp").setText(temp + " °C");

        String minTemp = String.valueOf(this.wi.getMinTemp(day));
        dayInfo.get("min").setText(minTemp + " °C");

        String maxTemp = String.valueOf(this.wi.getMaxTemp(day));
        dayInfo.get("max").setText(maxTemp + " °C");

        String wind = String.valueOf(this.wi.getWindSpeed(day));
        dayInfo.get("wind").setText(wind + " km/h");

        String clouds = String.valueOf(this.wi.getCloudsValue(day));
        dayInfo.get("clouds").setText(clouds + "%");

        String humidity = String.valueOf(this.wi.getHumidity(day));
        dayInfo.get("hum").setText(humidity + "%");

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

    public void setDayLabelMap(){
        ArrayList<Map<String,Label>> dayInfoLabels = new ArrayList<>();
        Map<String, Label> day1= new HashMap<>();
        Map<String, Label> day2= new HashMap<>();

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

        this.dayInfoLabels = dayInfoLabels;


    }

}
