package com.app.app.settings;

import com.app.WeatherInfo.NonexistentZipCodeException;
import com.app.app.AppPanel;
import com.app.loginapp.Database;
import com.app.loginapp.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Appearance implements Initializable {
    @FXML
    private CheckBox dayMode;
    private User user;
    private boolean mode;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = User.getInstance();
        try {
            mode = new AppPanel().Mode();
            dayMode.setSelected(mode);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NonexistentZipCodeException e) {
            e.printStackTrace();
        };
    }

    public void setDayMode(ActionEvent event) throws SQLException {
        if(dayMode.isSelected()){
            Database.changeAppearance(user.getUsername(), true);
            System.out.println(Database.getAppearance(user.getUsername()));
        }
        else{
            Database.changeAppearance(user.getUsername(), false);
            System.out.println(Database.getAppearance(user.getUsername()));
        }
    }
}
