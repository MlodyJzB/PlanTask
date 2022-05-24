package com.app.app.settings;

import com.app.app.App;
import com.app.loginapp.Database;
import com.app.loginapp.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
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
        dayMode.setSelected(Database.getAppearance(user.getUsername()).get(0));
    }

    public void setDayMode(ActionEvent event) throws SQLException {
        if(dayMode.isSelected()){
            Database.changeAppearance(user.getUsername(), true);
        }
        else{
            Database.changeAppearance(user.getUsername(), false);
            //List<Boolean> a = Database.getAppearance(user.getUsername());

        }
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
        Window owner = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        Stage stage1 = (Stage) owner.getScene().getWindow();
        stage1.close();
        Platform.runLater( () -> {
            try {
                new App().start( new Stage() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
