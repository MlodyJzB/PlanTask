package com.app.app;

import com.app.WeatherInfo.IncorrectZipCodeFormatException;
import com.app.WeatherInfo.NonexistentZipCodeException;
import com.app.WeatherInfo.WeatherInfo;
import com.app.WeatherInfo.ZipCodeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ResourceBundle;

public class Weather implements Initializable {
    public void Exit() {
        System.exit(0);
    }

    private String NormCol, DiffCol, BackCol;

    @FXML
    private ChoiceBox Mode;

    @FXML
    private Label City;

    @FXML
    private TextField ZipCodeField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WeatherInfo wi = new WeatherInfo();
        try {
            wi.updateFromJson();
        } catch (JSONException e) {
            ZipCodeField.setPromptText("1");
        } catch (IOException e) {
            ZipCodeField.setPromptText("2");
        } catch (NonexistentZipCodeException e) {
            throw new RuntimeException(e);
        }
        ZipCodeField.setPromptText(wi.getZipCode());
        City.setText(wi.getCity());
    }

    public void ColourFromDataJson(boolean DayMode) throws IOException, JSONException {
        String contents = new String((Files.readAllBytes(Paths.get("colors.json"))));
        JSONObject o = new JSONObject(contents);
        if(DayMode) {
            NormCol = (String) o.get(("BrightColorNormal"));
            DiffCol = (String) o.get(("BrightColorDifferent"));
            BackCol = (String) o.get(("BrightColorBackground"));
        }
        else{
            NormCol = (String) o.get(("DarkColorNormal"));
            DiffCol = (String) o.get(("DarkColorDifferent"));
            BackCol = (String) o.get(("DarkColorBackground"));
            }
    };

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
    @FXML
    private void onClickRefresh(ActionEvent event) throws JSONException, IOException, IncorrectZipCodeFormatException {
        String zipCode = ZipCodeField.getText();
        String zipCodeNoWhite = zipCode.replaceAll("\\s+","");
        WeatherInfo wi = new WeatherInfo();
        try{
            wi.update(zipCodeNoWhite);
            ZipCodeField.setPromptText(zipCodeNoWhite);
        }
        catch (IncorrectZipCodeFormatException | NonexistentZipCodeException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Incorrect zip code. Try again!", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
