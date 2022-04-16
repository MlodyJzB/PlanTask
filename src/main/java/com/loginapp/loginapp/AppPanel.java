package com.loginapp.loginapp;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppPanel implements Initializable {
    @FXML
    private ImageView exit;
    @FXML
    private BorderPane ourWindow;
    @FXML
    private AnchorPane pane3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //exit.setOnMouseClicked(event -> System.exit(0));

        /*TranslateTransition translateT = new TranslateTransition(Duration.seconds(0.5), pane2);
        translateT.setByX(-169);
        translateT.play();
        TranslateTransition translateT3 = new TranslateTransition(Duration.seconds(0.5), pane1);
        translateT3.setByX(-48);
        translateT3.play();

        menu.setOnMouseClicked(event->{
            TranslateTransition translateT1 = new TranslateTransition(Duration.seconds(0.5), pane2);
            TranslateTransition translateT2 = new TranslateTransition(Duration.seconds(0.5), pane1);
            translateT1.setByX(+169);
            translateT2.setByX(+48);
            translateT1.play();
            translateT2.play();
        });
        pane1.setOnMouseClicked(event->{
            TranslateTransition translateT1 = new TranslateTransition(Duration.seconds(0.5), pane2);
            TranslateTransition translateT2 = new TranslateTransition(Duration.seconds(0.5), pane1);
            translateT1.setByX(-169);
            translateT2.setByX(-48);
            translateT1.play();
            translateT2.play();
        });*/

    }
    @FXML
    public void home(MouseEvent e){
        ourWindow.setCenter(pane3);
    }

    private void LoadSite(String site){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(site+".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ourWindow.setCenter(root);
    }

    public void planner(ActionEvent event) {
        LoadSite("planner");
    }
    public void Exit() {
        System.exit(0);
    }
}
