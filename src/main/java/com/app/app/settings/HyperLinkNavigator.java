package com.app.app.settings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class HyperLinkNavigator implements Initializable {
    private List<Hyperlink> hyperlinkList = new ArrayList<>();

    @FXML
    VBox vBox;

    private void selectTreeItemOnClick(ActionEvent actionEvent) throws IOException {
        Hyperlink hyperlink = (Hyperlink) actionEvent.getSource();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "settings.fxml"
                )
        );

        Stage stage = (Stage) hyperlink.getScene().getWindow();
        stage.setScene(
                new Scene(loader.load())
        );

        Settings controller = loader.getController();
        controller.selectTreeItem(hyperlink.getText());

        stage.show();
    }

    public List<Hyperlink> getHyperlinkList() {
        return hyperlinkList;
    }

    public void setHyperlinkList(TreeItem<String> parent) {
        hyperlinkList.clear();
        for (TreeItem<String> treeItem : parent.getChildren())
            hyperlinkList.add(new Hyperlink(treeItem.getValue()));
    }

    public void setvBox(String treeItemName) {
        vBox.getChildren().add(
                new Text(treeItemName.substring(0, 1).toUpperCase() +
                                treeItemName.substring(1) +":"
                )
        );
        for (Hyperlink hyperlink : hyperlinkList) {
            hyperlink.setOnAction((actionEvent) -> {
                try {
                    selectTreeItemOnClick(actionEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            vBox.getChildren().add(hyperlink);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
