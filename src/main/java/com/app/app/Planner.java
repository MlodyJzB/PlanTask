package com.app.app;

import com.app.loginapp.LoginPanelController;
import com.calendarfx.view.page.WeekPage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
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

    public void Exit() {
        System.exit(0);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boolean a = false;
        try {
            a = (boolean) new LoginPanelController().getInfo(new AppPanel().whichUserClicked()).get(4);
            this.DayMode(a);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private ImageView minimalize_button;
    @FXML
    private AnchorPane diffcolor, pane3;
    @FXML
    private WeekPage normcol1;
    private String NormCol, DiffCol, BackCol;
    @FXML
    private void Minimize_clicked() {
        Stage stage = (Stage) minimalize_button.getScene().getWindow();
        //stage.setIconified(true);
        stage.setMaximized(!stage.isMaximized());
        //Restore down
        stage.setMaximized(stage.isMaximized());
    }
    public void ColourFromDataJson(boolean DayMode) throws IOException, JSONException {
        String contents = new String((Files.readAllBytes(Paths.get("colors.json"))));
        JSONObject o = new JSONObject(contents);
        if(DayMode) {
            NormCol = (String) o.get(("BrightColorNormal"));
            DiffCol = (String) o.get(("BrightColorDifferent"));
            BackCol = (String) o.get(("BrightColorBackground"));
            normcol1.getStylesheets().removeAll();
        }
        else{
            NormCol = (String) o.get(("DarkColorNormal"));
            DiffCol = (String) o.get(("DarkColorDifferent"));
            BackCol = (String) o.get(("DarkColorBackground"));
            normcol1.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Planer.css")).toExternalForm());
        }
    };

    private void DayMode(boolean DayMode) throws JSONException, IOException {
        this.ColourFromDataJson(DayMode);
        pane3.setStyle("-fx-background-color: " + BackCol + "; -fx-background-radius: 0 15 15 0;");
        diffcolor.setStyle("-fx-background-color: " + DiffCol + "; -fx-background-radius: 10;");
        normcol1.setStyle("-fx-background-color: " + NormCol + "; -fx-background-radius: 10;");
    }
}
