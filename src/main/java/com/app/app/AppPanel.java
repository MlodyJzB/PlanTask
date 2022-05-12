package com.app.app;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DetailedDayView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class AppPanel implements Initializable {
    @FXML
    private Text HourInfo, DateInfo;
    @FXML
    private BorderPane ourWindow;
    @FXML
    private AnchorPane pane3;
    @FXML
    private VBox Incoming_events_Vbox;
    @FXML
    private Label Temp, FeelsLike, WeatherDescription, CloudsValue, WindValue;

    @FXML
    private Button refreshButton;

    @FXML ImageView weatherImage;
    private Label Tempe, MinTemp, MaxTemp, WeatherDescription, CloudsValue, WindValue;
    @FXML
    private DetailedDayView aaa;
    private CalendarSource AddNewEntries(String addedEvent, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime){
        Calendar calendar = new Calendar("Test");
        CalendarSource calendarSource = new CalendarSource("source");

        Entry<String> entry = new Entry<>(addedEvent);
        entry.setInterval(startDate);
        entry.changeStartDate(startDate);
        entry.changeEndDate(endDate);
        entry.changeStartTime(startTime);
        entry.changeEndTime(endTime);
        calendar.addEntry(entry);

        calendarSource.getCalendars().addAll(calendar);
        return calendarSource;
    };
    private volatile boolean stop = false;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CalendarSource newEvent = AddNewEntries("Przykład", LocalDate.now(), LocalDate.now(), LocalTime.of(14,30), LocalTime.of(23,30));
        aaa.getCalendarSources().setAll(newEvent);
        int[] a = {5, 23, 12, 40};

        JSONObject jsonCalendar = new JSONObject();
        JSONObject jsonCalendar1 = new JSONObject();
        List<JSONObject> listOfEvents = new ArrayList();
        try {
            jsonCalendar.put("event_name", "work");
            jsonCalendar.put("start", "10:30");
            jsonCalendar.put("end", "12:45");
            jsonCalendar.put("description", "description");
            jsonCalendar1.put("event_name", "shopping");
            jsonCalendar1.put("start", "13:00");
            jsonCalendar1.put("end", "14:45");
            jsonCalendar1.put("description", "description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listOfEvents.add(jsonCalendar);
        listOfEvents.add(jsonCalendar1);


        /*
        Tu dodaje się info o zadaniach w jsonie i wrzuczmy do funkcji add_event (byle by
        typ wydarzenia był torzsamy z nazwą zdj co je opisuje w Images, czas w stringu 13:21
        (powyżej przykład))
        */


        try {
            Incoming_events_Vbox.getChildren().setAll(add_event(jsonCalendar), add_event(jsonCalendar1));
        } catch (JSONException | FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }

        //exit.setOnMouseClicked(event -> System.exit(0));

        /*TranslateTransition translateT = new TranslateTransition(Duration.seconds(0.5), pane2);
        translateT.setByX(-169);
        translateT.play();
        TranslateTransition translateT3 = new TranslateTransition(Duration.seconds(0.5), pane1);
        translateT3.setByX(-48);
        translateT3.play();

        menu.setOnMouseClicked(event->{
            TranslateTransition translateT1 = new TranslateTransition(Duration.seconds(0.5), pane2);
            TranslateTransition translateT2 = new TranslateTransition(Duration.seconds(0.5), pane1);
            translateT1.setByX(+169);
            translateT2.setByX(+48);
            translateT1.play();
            translateT2.play();
        });
        pane1.setOnMouseClicked(event->{
            TranslateTransition translateT1 = new TranslateTransition(Duration.seconds(0.5), pane2);
            TranslateTransition translateT2 = new TranslateTransition(Duration.seconds(0.5), pane1);
            translateT1.setByX(-169);
            translateT2.setByX(-48);
            translateT1.play();
            translateT2.play();
        });*/
        Thread tr = new Thread(()-> {
            while(!stop){
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy/MM/dd");
                LocalDateTime date = LocalDateTime.now();
                DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalDateTime time = LocalDateTime.now();
                String hourFormatted = dtf1.format(date);
                String dateFormatted = dtf.format(time);
                HourInfo.setText(hourFormatted);
                DateInfo.setText(dateFormatted);
                int minutes = Integer.valueOf(hourFormatted.substring(3,5));
                int seconds = Integer.valueOf(hourFormatted.substring(6,8));
//                if((minutes == 0)&&(seconds == 0)) {
//                    Platform.runLater(() -> {
//                        try {
//                            setWeather(wi);
//                        } catch (NonexistentZipCodeException e) {
//                            e.printStackTrace();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (IncorrectZipCodeFormatException e) {
//                            e.printStackTrace();
//                        }
//                    });
//                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        tr.start();
        AtomicInteger seconds = new AtomicInteger();
        Thread tr1 = new Thread(()-> {
            while(!stop){
                if(seconds.get() == 0) {
                    Platform.runLater(() -> {
                        try {
                            setWeather(wi);
                        } catch (NonexistentZipCodeException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (IncorrectZipCodeFormatException e) {
                            e.printStackTrace();
                        }
                    });
                    seconds.getAndIncrement();
                    if (seconds.get() == 1800) seconds.set(0);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        tr1.start();
    };
    @FXML
    private ImageView minimalize_button;

    private String enteredButtonStyle;

    @FXML
    private void onMouseEntered(MouseEvent event) {
        Button enteredButton = (Button) event.getSource();
        enteredButtonStyle = enteredButton.getStyle();
        enteredButton.setStyle("-fx-background-color: rgb(255,132,31)");
    }

    @FXML
    private void onMouseEnteredRefreshWeather(MouseEvent event) {
        Button enteredButton = (Button) event.getSource();
        enteredButtonStyle = enteredButton.getStyle();
        enteredButton.setStyle("-fx-background-color: rgb(178,148,113)");
    }

    @FXML
    private void onMouseExited(MouseEvent event) {
        Button enteredButton = (Button) event.getSource();
        enteredButton.setStyle(enteredButtonStyle);
    }


    @FXML
    private void Minimize_clicked() {
        Stage stage = (Stage) minimalize_button.getScene().getWindow();
        //stage.setIconified(true);
        stage.setMaximized(!stage.isMaximized());
        //Restore down
        stage.setMaximized(stage.isMaximized());
    }

    private void LoadSite(String site){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(site+".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ourWindow.setCenter(root);
    }
    public void planner(ActionEvent event) {
        LoadSite("planner");
    }
    public void calendar(ActionEvent event) {
        LoadSite("calendar");
    }
    public void statics(ActionEvent event) { LoadSite("statistics"); }
    public void settings(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "settings/settings.fxml"
                )
        );

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);

        Scene scene = new Scene(loader.load());
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        //Settings controller = loader.getController();
        stage.show();
    }
    public void Exit() {
        stop = true;
        Stage stage = (Stage) ourWindow.getScene().getWindow();
        stage.close();
    }

    public void homepanel(ActionEvent event) {
        ourWindow.setCenter(pane3);
    }

    public HBox add_event(JSONObject j) throws JSONException, FileNotFoundException, ParseException {
        String img = j.getString("event_name");
        Image image1 = new Image(new FileInputStream("src/main/resources/Images/"+img+".png"));
        ImageView image = new ImageView(image1);
        AnchorPane pane1 = new AnchorPane();
        Label to= new Label(j.getString("start"));
        Label from= new Label(j.getString("end"));
        Label duration= new Label();

        DateFormat df = new SimpleDateFormat("HH:mm");
        Date date1= df.parse(j.getString("start"));
        Date date2= df.parse(j.getString("end"));
        long duratio = date2.getTime()-date1.getTime();

        String dura = Long.toString(((duratio / (60 * 60 * 1000))%24));
        String dura1 =  Long.toString(((duratio / (1000*60)) % 60));
        duration.setText(dura+":"+dura1);

        pane1.setPrefSize(60, 46);
        AnchorPane.setTopAnchor(image, 0.0);
        AnchorPane.setLeftAnchor(image, 0.0);
        image.setFitHeight(46.0);
        image.setFitWidth(46.0);
        pane1.getChildren().add(image);

        to.setFont(new Font("Serif", 18));
        to.setLayoutY(46.0);
        to.setPrefSize(200, 46);
        to.setAlignment(Pos.CENTER);

        from.setFont(new Font("Serif", 18));
        from.setAlignment(Pos.CENTER);
        from.setPrefSize(200, 46);

        duration.setFont(new Font("Serif", 18));
        duration.setAlignment(Pos.CENTER);
        duration.setPrefSize(200, 46);

        HBox hbox = new HBox(pane1, to, from, duration);
        hbox.setStyle("-fx-background-color: #ffbf70; -fx-background-radius: 5;");
        hbox.setPrefSize(200, 46);
        hbox.setSpacing(10);
        return hbox;
    }

    public void setWeather(WeatherInfo wi) throws NonexistentZipCodeException, JSONException, IOException, IncorrectZipCodeFormatException {

        wi.update("15-199");
        String temp = String.valueOf(Math.round(wi.getTemp()));
        String feelsLike = String.valueOf(Math.round(wi.getFeelsLike()));
        String windSpeed = String.valueOf(Math.round(wi.getWindSpeed()));
        String cloudsValue = String.valueOf(Math.round(wi.getCloudsValue()));
        String icon = wi.getIcon();

        Temp.setText(temp + "°C");
        FeelsLike.setText("Feels like: " + feelsLike + "°C");
        WindValue.setText(windSpeed + " km/h");
        CloudsValue.setText(cloudsValue + " %");

        File file = new File("");
        String imagesPath = file.getAbsolutePath() + "\\src\\main\\resources\\Images\\weatherIcons\\";
        weatherImage.setImage(new Image(imagesPath + icon + ".png"));
    }

    public void refreshWeather(ActionEvent event) throws NonexistentZipCodeException, JSONException, IOException, IncorrectZipCodeFormatException {
        WeatherInfo wi = new WeatherInfo();
        setWeather(wi);
    }
}
