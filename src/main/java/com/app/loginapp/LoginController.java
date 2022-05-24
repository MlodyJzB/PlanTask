package com.app.loginapp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable
{
    private User user;

    @FXML
    Text loginStatusText;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton, onClickRegisterScene;
    @FXML
    private void onClickLogin(ActionEvent event) throws IOException, JSONException {
        user.setUsername(usernameTextField.getText());
        user.setPassword(passwordField.getText());
        if (Database.checkIfUserExists(user.getUsername())) {
            loginStatusText.setStyle("-fx-fill: green");
            loginStatusText.setText("Logged successfully");

            String contents = new String((Files.readAllBytes(Paths.get("panels.json"))));
            JSONObject o = new JSONObject(contents);
            int a = (int) o.get(("which"));
            PutJsonInfo(a, user.getUsername(), true);

            /*URL url = App.class.getResource("App.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Scene scene = new Scene(fxmlLoader.load(), 970, 650);
            Stage appStage = new Stage();
            appStage.setScene(scene);
            appStage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            appStage.show();
            */
            Node node = (Node) event.getSource();
            Stage thisStage = (Stage) node.getScene().getWindow();
            thisStage.close();
            Platform.runLater( () -> {
                try {
                    new LoginApplication().start( new Stage() );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        else  {
            loginStatusText.setStyle("-fx-fill: #b71834");
            loginStatusText.setText("Incorrect username/password");
        }
    }
    public static void PutJsonInfo(int number, String name, boolean mode) throws JSONException, IOException {
        JSONArray info = new JSONArray();
        info.put(number);
        info.put("user.png");
        info.put("P");
        info.put(name);
        info.put(mode);
        String contents = new String((Files.readAllBytes(Paths.get("panels.json"))));
        JSONObject o = new JSONObject(contents);
        o.remove("info"+number);
        o.put("info"+number, info);
        FileWriter writter = new FileWriter("panels.json");
        writter.write(String.valueOf(o));
        writter.close();
    }
    @FXML
    private void switchToRegisterScene(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("register-scene.fxml")));
        Scene scene = ((Node)event.getSource()).getScene();
        scene.setRoot(fxmlLoader);
    }

    public static void limitMaxChar(TextField tf, final int maxLength) {
        //Listener made to check if new value of text-field isn't too long
        //and if it is substring from first char to max length
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                tf.setText(newValue.substring(0, maxLength));
            }
        });
    }

    public static void configureTextFields(TextField[] textFields) {
        for (TextField tf : textFields) {
            limitMaxChar(tf, 20);
            //Checking if change contains spaces and replacing them with ""
            //change can be pasted text or clicked space
            tf.setTextFormatter(new TextFormatter<>(change -> {
                if (change.getText().contains(" ")) {
                    change.setText(change.getText().replace(" ", ""));
                }
                return change;
            }));
        }
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = User.getInstance();
        configureTextFields(new TextField[]{usernameTextField, passwordField});
    }
    @FXML
    public ImageView exitonlogin;
    public void Exit1(MouseEvent mouseEvent) {
        ((Stage) exitonlogin.getScene().getWindow()).close();
    }
}