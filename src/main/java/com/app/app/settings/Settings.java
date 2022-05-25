package com.app.app.settings;

import com.app.loginapp.User;
import com.calendarfx.view.DetailedDayView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Settings implements Initializable {
    User user;
    public boolean daynight=false;
    private static final Map<String, TreeItem<String>> treeItemsMap = new HashMap<>();
    @FXML
    private TreeView<String> settingsTree;
    @FXML
    private StackPane stackPane;

    public void Exit() {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        stage.close();
    }
    public DetailedDayView detailedDayView1;

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
    private CheckBox DayNight;

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
                if (treeItemName.equals("ics")) {

                com.app.app.settings.Ics controller =loader.getController();
                controller.setUserEventsList(detailedDayView1, LocalDate.now().minusYears(1), LocalDate.now().plusYears(1));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = User.getInstance();
        //daynight = DayNight.isSelected();

        TreeItem<String> root, general;
        root = new TreeItem<>();
        root.setExpanded(true);

        general = makeBranch("General", root);
        general.setExpanded(true);

        List<String> treeItemsFromGeneral = List.of("account", "appearance", "import/export calendar");
        for (String treeItemName : treeItemsFromGeneral)
            makeBranch(treeItemName, general);

        List<String> treeItemsFromAccount = List.of("username", "password");
        TreeItem<String> account = treeItemsMap.get("account");
        for (String treeItemName : treeItemsFromAccount)
            makeBranch(treeItemName, account);

        List<String> treeItemsFromAppearance = List.of("ColorMode");
        TreeItem<String> appearance = treeItemsMap.get("appearance");
        for (String treeItemName : treeItemsFromAppearance)
            makeBranch(treeItemName, appearance);

        List<String> treeItemsFromIcs = List.of("ics");
        TreeItem<String> ics = treeItemsMap.get("import/export calendar");
        for (String treeItemName : treeItemsFromIcs)
            makeBranch(treeItemName, ics);

        settingsTree.setRoot(root);
        settingsTree.setShowRoot(false);
        settingsTree.getSelectionModel()
                .selectedItemProperty().addListener((v, oldSelectedTreeItem, newSelectedTreeItem) -> {
                    if (newSelectedTreeItem != null) {
                        if (!stackPane.getChildren().isEmpty())
                            stackPane.getChildren().remove(0);
                        stackPane.getChildren().add(getPane(newSelectedTreeItem.getValue()));
                    }
                });


    }

}
