package com.app.app.settings;

import com.app.loginapp.User;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class Settings implements Initializable {

    @FXML
    private TreeView<String> settingsTree;
    @FXML
    private StackPane stackPane;

    public void Exit() {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.close();
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
        Pane pane = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            pane = loader.load(
                    getClass().getResourceAsStream(
                            paneName+".fxml"
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TreeItem<String> root, general;
        root = new TreeItem<>();
        root.setExpanded(true);
        general = makeBranch("General", root);
        general.setExpanded(true);

        List<String> treeItemNamesList = List.of("account", "appearance");
        for (String treeItemName : treeItemNamesList) {
            makeBranch(treeItemName, general);
        }

        settingsTree.setRoot(root);
        settingsTree.setShowRoot(false);
        settingsTree.getSelectionModel()
                .selectedItemProperty().addListener((v, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (!stackPane.getChildren().isEmpty())
                            stackPane.getChildren().remove(0);
                        if (!newValue.getValue().equals("General"))
                            stackPane.getChildren().add(getPane(newValue.getValue()));
                    }
                });
    }

}
