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

    public StackPane getStackPane() {
        return stackPane;
    }


    private TreeItem<String> makeBranch(String name, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(name);
        item.setExpanded(false);
        parent.getChildren().add(item);
        return item;
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
            System.out.println(treeItemName+".fxml");
            FXMLLoader loader = new FXMLLoader();
            pane = loader.load(
                    getClass().getResourceAsStream(
                            treeItemName+".fxml"
                    )
            );
            if (getTreeItem(treeItemName).getChildren().size() > 0) {
                HyperLinkNavigator controller = loader.getController();
                controller.setHyperlinkList(getTreeItem(treeItemName));
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

        System.out.println(root.getChildren().get(0).getValue());

        List<String> treeItemsFromGeneral = List.of("account", "appearance");
        for (String treeItemName : treeItemsFromGeneral) {
            treeItemsMap.put(treeItemName, makeBranch(treeItemName, general));
        }

        List<String> treeItemsFromAccount = List.of("username", "password");
        TreeItem<String> account = treeItemsMap.get("account");
        for (String treeItemName : treeItemsFromAccount) {
            treeItemsMap.put(treeItemName, makeBranch(treeItemName, account));
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

//        applyButton.disableProperty().bind(Bindings.createBooleanBinding(
//                usernameChanged::get, usernameChanged
//        ));
    }

}
