package com.app.app.settings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;

public class Ics {
    @FXML
    public Button SaveButton;
    @FXML
    public Button ImportButton;

    private File file;
    private String str;

    public void importIcs(ActionEvent actionEvent) {
        System.out.println("import");
        Stage stage = (Stage) ImportButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ICS Files","*.ics"));
        file = fileChooser.showOpenDialog(stage);
        System.out.println(file.getName());

    }

    public void saveIcs(ActionEvent actionEvent) throws IOException {
        System.out.println("save");
        Stage stage = (Stage) SaveButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ICS Files", "*.ics"));
        File savefile = fileChooser.showSaveDialog(stage);
        FileWriter f = new FileWriter(savefile);
        f.write("gooooo");
        f.flush();
        f.close();

    }
}
