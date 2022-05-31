package com.app.app.settings;

import com.app.app.App;
import com.app.loginapp.Database;
import com.app.loginapp.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Appearance implements Initializable {
    @FXML
    private CheckBox dayMode;
    private User user;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = User.getInstance();
        dayMode.setSelected(user.isDayMode());
        //AutoDayMode.setSelected(user.isDayMode());
        //dayMode.setDisable(AutoDayMode.isSelected());
        //AutoDayMode.setOnAction(event -> {
            //.setDisable(AutoDayMode.isSelected());
            //user.setDAutoMode(AutoDayMode.isSelected());
        //});
        dayMode.setOnAction(event -> {
            Popup popup = new Popup();
            Label background = new Label();
            Label background1 = new Label();
            Button sure = new Button("Apply");
            Button noChanging = new Button("Cancel");
            background.setMinSize(400, 200);
            background.setStyle(" -fx-background-color:  #828281; -fx-background-radius: 10;");
            background1.setMinSize(390, 190);
            background1.setStyle(" -fx-background-color: #e8e8e8; -fx-background-radius: 10;");
            Label back = new Label("To change appearance reopening is necessary");
            back.setMinSize(400, 100);
            sure.setStyle(" -fx-background-color: rgba(0, 0, 0, 0.1)");
            noChanging.setStyle(" -fx-background-color: rgba(0, 0, 0, 0.1)");
            sure.setMinSize(50, 20);
            noChanging.setMinSize(50, 20);
            sure.setLayoutX(75);
            sure.setLayoutY(150);
            noChanging.setLayoutX(275);
            noChanging.setLayoutY(150);
            back.setAlignment(Pos.CENTER);
            back.setFont(new Font(18));
            background1.setLayoutX(5);
            background1.setLayoutY(5);
            popup.getContent().add(background);
            popup.getContent().add(background1);
            popup.getContent().add(back);
            popup.getContent().add(sure);
            popup.getContent().add(noChanging);
            popup.setAutoHide(true);
            if (!popup.isShowing()) {
                Stage stage = (Stage) dayMode.getScene().getWindow();
                popup.show(stage);
            }
            sure.setOnAction(event1 -> {
                popup.hide();
                Platform.runLater(()-> {
                    try {
                        setDayMode();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            });
            noChanging.setOnAction(event1 -> {
                dayMode.setSelected(user.isDayMode());
                popup.hide();
            });
        });
    }

    public void setDayMode() throws SQLException {
        Database.changeAppearance(user.getUsername(), dayMode.isSelected());
        user.setDayMode(dayMode.isSelected());
        //Window thisStage = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        Stage stage = (Stage) dayMode.getScene().getWindow();
        stage.close();
        Window owner = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        Stage stage1 = (Stage) owner.getScene().getWindow();
        try {
            new App().start( new Stage() );
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage1.close();
    }
}
