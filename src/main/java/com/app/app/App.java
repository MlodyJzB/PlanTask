package com.app.app;

import com.app.loginapp.LoginApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.ValidationException;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //Load start scene
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("App.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(); }
}
