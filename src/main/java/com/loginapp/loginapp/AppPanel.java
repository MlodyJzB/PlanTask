package com.loginapp.loginapp;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class AppPanel implements Initializable {
    @FXML
    private ImageView exit, menu;
    @FXML
    private AnchorPane pane1, pane2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exit.setOnMouseClicked(event -> System.exit(0));
        pane1.setVisible(false);
        FadeTransition fadeT = new FadeTransition(Duration.seconds(0.5), pane1);
        fadeT.setFromValue(1);
        fadeT.setToValue(0);
        fadeT.play();

        TranslateTransition translateT = new TranslateTransition(Duration.seconds(0.5), pane2);
        translateT.setByX(-169);
        translateT.play();

        menu.setOnMouseClicked(event->{
            pane1.setVisible(true);
            FadeTransition fadeT1 = new FadeTransition(Duration.seconds(0.5), pane1);
            fadeT1.setFromValue(0);
            fadeT1.setToValue(0.15);
            fadeT1.play();

            TranslateTransition translateT1 = new TranslateTransition(Duration.seconds(0.5), pane2);
            translateT1.setByX(+169);
            translateT1.play();
        });
        pane1.setOnMouseClicked(event->{
            FadeTransition fadeT1 = new FadeTransition(Duration.seconds(0.5), pane1);
            fadeT1.setFromValue(0.15);
            fadeT1.setToValue(0);
            fadeT1.play();

            fadeT1.setOnFinished(event1-> pane1.setVisible(false));

            TranslateTransition translateT1 = new TranslateTransition(Duration.seconds(0.5), pane2);
            translateT1.setByX(-169);
            translateT1.play();
        });
    }
}
