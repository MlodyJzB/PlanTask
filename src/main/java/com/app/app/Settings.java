package com.app.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Settings implements Initializable {
    public boolean daynight=false;
    @FXML
    TreeView<String> settingsTree;
    @FXML
    StackPane stackPane;

    public void Exit() {
        System.exit(0);
    }

    @FXML
    private ImageView minimalize_button;

    @FXML
    private void Minimize_clicked() {
        Stage stage = (Stage) minimalize_button.getScene().getWindow();
        //stage.setIconified(true);
        stage.setMaximized(!stage.isMaximized());
        //Restore down
        stage.setMaximized(stage.isMaximized());
    }

    private TreeItem<String> makeBranch(String name, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(name);
        item.setExpanded(false);
        parent.getChildren().add(item);
        return item;
    }

    private Pane getPane(String paneName){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(paneName+".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (Pane) root;
    }
    @FXML
    private CheckBox DayNight;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TreeItem<String> root, general, visual;
        root = new TreeItem<>();
        root.setExpanded(true);

        //Bucky
        general = makeBranch("General", root);
        general.setExpanded(true);
        makeBranch("something", general);
        makeBranch("else", general);
        makeBranch("elseElse", general);

        visual = makeBranch("Visual", root);
        makeBranch("visual1", visual);
        makeBranch("visual2", visual);
        settingsTree.setRoot(root);
        settingsTree.setShowRoot(false);
        settingsTree.getSelectionModel()
                .selectedItemProperty().addListener((v, oldValue, newValue) -> {
                    if (newValue != null){
                        if (!stackPane.getChildren().isEmpty())
                            stackPane.getChildren().remove(0);
                        stackPane.getChildren().add(getPane(newValue.getValue()));
                        daynight = DayNight.isSelected();
                    }
                });
    }
}
