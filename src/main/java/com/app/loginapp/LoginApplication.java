package com.app.loginapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

public class LoginApplication extends Application {
    long lastRefreshTime = 0;
    @Override
    public void start(Stage stage) throws IOException {
        //Load start scene
        URL ur = LoginApplication.class.getResource("LoginPanel.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(ur);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        scene.addPreLayoutPulseListener(() -> {
            long refreshTime = System.nanoTime();
            lastRefreshTime = refreshTime;
        });
        stage.show();
    }

    public static void main(String[] args) throws ParseException {
        launch();
        //ICSFilesReader ics = new ICSFilesReader();
        //ToICSFileWritter  toics = new ToICSFileWritter();
        //ics.icsToJSON("wasza ścieżka z ics", "test.json");
        //toics.JSONToICS("test.json", "myics.ics");
    }
}