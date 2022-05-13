package com.app.loginapp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class LoginPanelController implements Initializable {
    private User user;
    public ImageView User1, User2, User3, User4;
    public Image image;
    @FXML
    public void goToLoginOrApp1() throws JSONException, IOException {
        whichUserClickedEdit(1);
        if(getInfo(1).get(3).equals(getInfo(5).get(3))){
            goToLogin();
        }
        else{
            System.out.println("to app");
        }
    }
    @FXML
    public void goToLoginOrApp2() throws JSONException, IOException {
        whichUserClickedEdit(2);
        if(getInfo(2).get(3).equals(getInfo(5).get(3))){
            goToLogin();
        }
        else{
            System.out.println("to app");
        }
    }
    @FXML
    public void goToLoginOrApp3() throws JSONException, IOException {
        whichUserClickedEdit(3);
        if(getInfo(3).get(3).equals(getInfo(5).get(3))){
            goToLogin();
        }
        else{
            System.out.println("to app");
        }
    }
    @FXML
    public void goToLoginOrApp4() throws JSONException, IOException {
        whichUserClickedEdit(4);
        if(getInfo(4).get(3).equals(getInfo(5).get(3))){
            goToLogin();
        }
        else{
            System.out.println("to app");
        }
    }

    public void goToLogin() throws IOException, JSONException {
        FXMLLoader pane = new FXMLLoader(getClass().getResource("register-scene.fxml"));
        Parent root = (Parent) pane.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }
    @FXML
    public void goToApp() throws IOException {

        FXMLLoader pane = new FXMLLoader(getClass().getResource("/app/App.fxml"));
        Parent root = (Parent) pane.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }
    @FXML
    public ImageView exit;
    public void Exit() {
        System.exit(0);
    }
    @FXML
    public TextField UserName1, UserName2, UserName3, UserName4;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.update();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void update() throws JSONException, IOException {
        Image image1 = new Image(new FileInputStream("src/main/resources/Images/"+getInfo(1).get(1)));
        User1.setImage(image1);
        UserName1.setText((String) getInfo(1).get(3));
        image1 = new Image(new FileInputStream("src/main/resources/Images/"+getInfo(2).get(1)));
        User2.setImage(image1);
        UserName2.setText((String) getInfo(2).get(3));
        image1 = new Image(new FileInputStream("src/main/resources/Images/"+getInfo(3).get(1)));
        User3.setImage(image1);
        UserName3.setText((String) getInfo(3).get(3));
        image1 = new Image(new FileInputStream("src/main/resources/Images/"+getInfo(4).get(1)));
        User4.setImage(image1);
        UserName4.setText((String) getInfo(4).get(3));
    }
    public void whichUserClickedEdit(int number) throws IOException, JSONException {
        String contents = new String((Files.readAllBytes(Paths.get("panels.json"))));
        JSONObject o = new JSONObject(contents);
        o.remove("which");
        o.put("which", number);
        FileWriter writter = new FileWriter("panels.json");
        writter.write(String.valueOf(o));
        writter.close();
    }
    public JSONArray getInfo(int which_one) throws IOException, JSONException {
        String contents = new String((Files.readAllBytes(Paths.get("panels.json"))));
        JSONObject o = new JSONObject(contents);
        JSONArray array = o.getJSONArray("info"+which_one);
        return array;
    }
    public void PutJsonDefault(int number) throws JSONException, IOException {
        JSONArray info = new JSONArray();
        info.put(number);
        info.put("img.png");
        info.put("F");
        info.put("");
        info.put(true);
        String contents = new String((Files.readAllBytes(Paths.get("panels.json"))));
        JSONObject o = new JSONObject(contents);
        o.remove("info"+number);
        o.put("info"+number, info);
        FileWriter writter = new FileWriter("panels.json");
        writter.write(String.valueOf(o));
        writter.close();
        Platform.runLater( () -> {
            try {
                new LoginApplication().start( new Stage() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void remove1(ActionEvent event) throws JSONException, IOException {
        PutJsonDefault(1);
    }
    public void remove2(ActionEvent event) throws JSONException, IOException {
        PutJsonDefault(2);
    }
    public void remove3(ActionEvent event) throws JSONException, IOException {
        PutJsonDefault(3);
    }
    public void remove4(ActionEvent event) throws JSONException, IOException {
        PutJsonDefault(4);
    }
    @FXML
    private void onMouseEntered(MouseEvent event) {
        Button enteredButton = (Button) event.getSource();
        enteredButton.setStyle("-fx-background-color: #b58251");
    }
    @FXML
    private void onMouseExited(MouseEvent event) {
        Button enteredButton = (Button) event.getSource();
        enteredButton.setStyle("-fx-background-color: #dba563");
    }
}
