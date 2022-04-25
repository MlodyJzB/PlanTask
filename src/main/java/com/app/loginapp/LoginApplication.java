package com.app.loginapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.ValidationException;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //Load start scene
        URL ur = LoginApplication.class.getResource("LoginPanel.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(ur);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }

    public static void main(String[] args) throws ParserException, JSONException, IOException, ValidationException, ParseException {
        launch();
        //ICSFilesReader ics = new ICSFilesReader();
        //ToICSFileWritter  toics = new ToICSFileWritter();
        //ics.icsToJSON("wasza ścieżka z ics", "test.json");
        //toics.JSONToICS("test.json", "myics.ics");
    }
}