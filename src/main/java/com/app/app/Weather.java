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
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Weather implements Initializable {
    public Weather() throws NonexistentZipCodeException, JSONException, IOException {
    }

    public void Exit() {
        System.exit(0);
    }

    private WeatherInfo wi = new WeatherInfo();

    private final SimpleDateFormat dayFormater = new SimpleDateFormat("EEEEEEEEE", Locale.US);
    private SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm dd/MM");

    @FXML
    private Label City, Day1, Day2, Day3, Day4, Day5, Day6, LastUpdate;

    @FXML
    private TextField ZipCodeField;

    @FXML
    private ImageView weatherImage;

    @FXML
    private Button refreshButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.setLocationLabels();
            this.setDayLabels();
        } catch (NonexistentZipCodeException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshWeather(ActionEvent event) throws JSONException, IOException {
        String newZipCode = ZipCodeField.getText();
        String zipCodeNoWhite = newZipCode.replaceAll("\\s+","");
        try {
            if (newZipCode == "") this.wi.updateOnline();
            else{
                this.wi.updateOnline(zipCodeNoWhite);
                ZipCodeField.setPromptText(zipCodeNoWhite);
            }
            this.setDayLabels();
            this.setLastUpdate();
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

    public void setLocationLabels() throws NonexistentZipCodeException, JSONException {
        ZipCodeField.setPromptText(wi.getZipCode());
        City.setText(wi.getCity());
        this.setLastUpdate();
    }

    public void setLastUpdate() throws NonexistentZipCodeException, JSONException{
        long lastUpdateTime = this.wi.getUpdateDate();

        String lastUpdate = this.dateFormater.format((lastUpdateTime)*1000);
        LastUpdate.setText(lastUpdate);
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

}
