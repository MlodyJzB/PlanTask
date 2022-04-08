package com.loginapp.loginapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ICSFiles.*;
import net.fortuna.ical4j.data.ParserException;
import org.json.JSONException;

import java.io.IOException;

public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //Load start scene
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("LoginPanel.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) throws ParserException, JSONException, IOException {
        launch();
        //ICSFilesReader ics = new ICSFilesReader();
        //ics.icsToJSON("wasza ścieżka do pliku ics", "test");
    }
}