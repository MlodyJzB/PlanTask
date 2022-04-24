package com.app.loginapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class LoginPanelController implements Initializable {
    private LoginInfo loginInfo;
    public ImageView User1, User2, User3, User4;
    public Image image;
    @FXML
    public void goToLoginOrApp1() throws JSONException, IOException {
        //String source = event.getPickResult().getIntersectedNode().getId();
        if(getInfo(1).get(3).equals(getInfo(5).get(3))){
            System.out.println("ti login");
            goToLogin();
        }
        else{
            System.out.println("to app");
        }
    }
    @FXML
    public void goToLoginOrApp2() throws JSONException, IOException {
        if(getInfo(2).get(3).equals(getInfo(5).get(3))){
            goToLogin();
        }
        else{
            System.out.println("to app");
        }
    }
    @FXML
    public void goToLoginOrApp3() throws JSONException, IOException {
        System.out.println("c");
        if(getInfo(3).get(3).equals(getInfo(5).get(3))){
            goToLogin();
        }
        else{
            System.out.println("to app");
        }
    }
    @FXML
    public void goToLoginOrApp4() throws JSONException, IOException {
        System.out.println("d");
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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loginInfo = LoginInfo.getInstance();
    }
    public JSONArray getInfo(int which_one) throws IOException, JSONException {
        String contents = new String((Files.readAllBytes(Paths.get("panels.json"))));
        JSONObject o = new JSONObject(contents);
        JSONArray array = o.getJSONArray("info"+which_one);
        return array;
    }

}
