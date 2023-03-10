package com.app.app;

import com.app.loginapp.User;
import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.page.WeekPage;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ResourceBundle;

public class Planner implements Initializable {
    User user;
    @FXML
    private ImageView minimalize_button;
    @FXML
    private AnchorPane diffcolor, pane3;
    @FXML
    private WeekPage weekPage;
    @FXML
    private ImageView minimalize_button1;
    private String NormCol, DiffCol, BackCol;
    @FXML
    private void Minimize_clicked() {
        Stage stage = (Stage) minimalize_button.getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
        stage.setMaximized(stage.isMaximized());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = User.getInstance();
        boolean InfoDayNight = user.isDayMode();
        try {
            this.DayMode(InfoDayNight);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        minimalize_button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                Stage primaryStage = (Stage) minimalize_button1.getScene().getWindow();
                primaryStage.setIconified(true);
            }
        });
    }

    public void bindWeekPage(DetailedDayView detailedDayView) {
        detailedDayView.bind(weekPage, true);
    }

    @FXML
    public void Exit() {
        System.exit(0);
    }

    public void ColourFromDataJson(boolean DayMode) throws IOException, JSONException {
        String contents = new String((Files.readAllBytes(Paths.get("colors.json"))));
        JSONObject o = new JSONObject(contents);
        if(DayMode) {
            NormCol = (String) o.get(("BrightColorNormal"));
            DiffCol = (String) o.get(("BrightColorDifferent"));
            BackCol = (String) o.get(("BrightColorBackground"));
            weekPage.getStylesheets().removeAll();
        }
        else{
            NormCol = (String) o.get(("DarkColorNormal"));
            DiffCol = (String) o.get(("DarkColorDifferent"));
            BackCol = (String) o.get(("DarkColorBackground"));
            weekPage.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Planer.css")).toExternalForm());
        }
    }

    private void DayMode(boolean DayMode) throws JSONException, IOException {
        this.ColourFromDataJson(DayMode);
        pane3.setStyle("-fx-background-color: " + BackCol + "; -fx-background-radius: 0 15 15 0;");
        diffcolor.setStyle("-fx-background-color: " + DiffCol + "; -fx-background-radius: 10;");
        weekPage.setStyle("-fx-background-color: " + NormCol + "; -fx-background-radius: 10;");
    }
}
