package com.app.app.settings;

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

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Settings implements Initializable {

    private static final Map<String, TreeItem<String>> treeItemsMap = new HashMap<>();
    @FXML
    Button okButton, cancelButton, applyButton;
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
        TreeItem<String> treeItem = new TreeItem<>(name);
        treeItem.setExpanded(false);
        treeItemsMap.put(treeItem.getValue(), treeItem);
        parent.getChildren().add(treeItem);
        return treeItem;
    }

    static TreeItem<String> getTreeItem(String treeItemName) {
        return treeItemsMap.get(treeItemName);
    }

    public void selectTreeItem(String treeItemName) {
        TreeItem<String> selectedItem = treeItemsMap.get(treeItemName);
        for(TreeItem<String> treeItem = selectedItem; treeItem.getParent() != null; treeItem = treeItem.getParent() )
            treeItem.getParent().setExpanded( true );
        settingsTree.getSelectionModel().select(selectedItem);
    }

    @FXML
    void okClicked(ActionEvent event) {

    }
    @FXML
    void applyClicked(ActionEvent event) {
        //Database.ChangeUsername()
    }
    @FXML
    void cancelClicked(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    private Pane getPane(String treeItemName){
        Pane pane = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            if (getTreeItem(treeItemName).getChildren().size() > 0) {
                pane = loader.load(
                        getClass().getResourceAsStream(
                                "hyperLinkNavigator.fxml"
                        )
                );
                HyperLinkNavigator controller = loader.getController();
                controller.setHyperlinkList(getTreeItem(treeItemName));
                controller.setvBox(treeItemName);
            } else {
                pane = loader.load(
                        getClass().getResourceAsStream(
                                treeItemName+".fxml"
                        )
                );
            }
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

        List<String> treeItemsFromGeneral = List.of("account", "appearance");
        for (String treeItemName : treeItemsFromGeneral)
            makeBranch(treeItemName, general);

        List<String> treeItemsFromAccount = List.of("username", "password");
        TreeItem<String> account = treeItemsMap.get("account");
        for (String treeItemName : treeItemsFromAccount)
            makeBranch(treeItemName, account);

        settingsTree.setRoot(root);
        settingsTree.setShowRoot(false);
        settingsTree.getSelectionModel()
                .selectedItemProperty().addListener((v, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (!stackPane.getChildren().isEmpty())
                            stackPane.getChildren().remove(0);
                        stackPane.getChildren().add(getPane(newValue.getValue()));
                    }
                });

    }

}
