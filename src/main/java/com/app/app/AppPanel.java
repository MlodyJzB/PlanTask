package com.app.app;

import com.app.WeatherInfo.IncorrectZipCodeFormatException;
import com.app.WeatherInfo.NonexistentZipCodeException;
import com.app.WeatherInfo.WeatherInfo;
import com.app.loginapp.LoginPanelController;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.MonthView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private Label Temp, FeelsLike, CloudsValue, WindValue;
    @FXML
    ImageView weatherImage;
    @FXML
    private AnchorPane backgroundColor, diffColor2, normColor4, normColor3, normColor2, normColor1;
    @FXML
    private HBox diffColor1;
    @FXML
    private VBox SideBarcolor;
    @FXML
    private MonthView monthView;
    @FXML
    public Label Label1, Label2, Label3;
    @FXML
    private DetailedDayView detailedDayView;

    private String BackCol;
    private String SideCol;
    private String NormCol;
    private String DiffCol;

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
    private boolean InfoDayNight = true;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        CalendarSource newEvent = AddNewEntries("Przykład", LocalDate.now(), LocalDate.now(), LocalTime.of(14,30), LocalTime.of(23,30));
//        detailedDayView.getCalendarSources().setAll(newEvent);
        Event event = new Event("Event1", LocalDateTime.now(),
                LocalDateTime.of(LocalDate.now(),
                        LocalTime.of(23, 59)
                )
        );
        detailedDayView.getCalendarSources().get(0).getCalendars().get(0).addEntry(event.getEntry());
        try {
            JSONArray a = new LoginPanelController().getInfo(whichUserClicked());
            InfoDayNight = (Boolean) a.get(4);
            ColourFromDataJson(InfoDayNight);
            DayMode();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
            Incoming_events_Vbox.getChildren().setAll(add_event(InfoDayNight, jsonCalendar), add_event(InfoDayNight, jsonCalendar1));
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
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        tr.start();
        AtomicInteger seconds = new AtomicInteger();
        WeatherInfo wi = new WeatherInfo();
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
        ourWindow.setCenter(backgroundColor);
    }

    public HBox add_event(boolean YesNo, JSONObject j) throws JSONException, FileNotFoundException, ParseException {
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
        if(YesNo) {
            hbox.setStyle("-fx-background-color: #ffbf70; -fx-background-radius: 10;");
        }
        else{
            //diff-color4
            hbox.setStyle("-fx-background-color: #4d754c; -fx-background-radius: 10;");
        }
        hbox.setPrefSize(200, 46);
        hbox.setSpacing(10);
        return hbox;
    }

    public void setWeather(WeatherInfo wi) throws NonexistentZipCodeException, JSONException, IOException, IncorrectZipCodeFormatException {

        wi.update();
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
    public int whichUserClicked() throws IOException, JSONException {
        String contents = new String((Files.readAllBytes(Paths.get("panels.json"))));
        JSONObject o = new JSONObject(contents);
        return (int) o.get(("which"));
    };
    public void ColourFromDataJson(boolean DayMode) throws IOException, JSONException {
        String contents = new String((Files.readAllBytes(Paths.get("colors.json"))));
        JSONObject o = new JSONObject(contents);
        Label[] labelColors = new Label[]{Label1, Label2, Label3};
        if(DayMode) {
            NormCol = (String) o.get(("BrightColorNormal"));
            DiffCol = (String) o.get(("BrightColorDifferent"));
            SideCol = (String) o.get(("BrightSideBarColor"));
            BackCol = (String) o.get(("BrightColorBackground"));
            monthView.getStylesheets().removeAll();
            detailedDayView.getStylesheets().removeAll();
        }
        else{
            NormCol = (String) o.get(("DarkColorNormal"));
            DiffCol = (String) o.get(("DarkColorDifferent"));
            SideCol = (String) o.get(("DarkSideBarColor"));
            BackCol = (String) o.get(("DarkColorBackground"));
            monthView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("AppPanel1.css")).toExternalForm());
            detailedDayView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("AppPanel1.css")).toExternalForm());
            Incoming_events_Vbox.setStyle("-fx-background-color: #2b2b2b;");
            for (Label a : labelColors) {
                a.setTextFill(Paint.valueOf("#ffffff"));
            };
        }
    };

    private void DayMode() {
        AnchorPane[] normalColors = new AnchorPane[]{normColor4, normColor3, normColor2, normColor1};
        for (AnchorPane a : normalColors) {
            a.setStyle("-fx-background-color: " + NormCol + "; -fx-background-radius: 10;");
        }
        ;
        backgroundColor.setStyle("-fx-background-color: " + BackCol + "; -fx-background-radius: 0 15 15 0;");
        SideBarcolor.setStyle("-fx-background-color: " + SideCol + "; -fx-background-radius: 15 0 0 15;");
        diffColor2.setStyle("-fx-background-color: " + DiffCol + "; -fx-background-radius: 10;");
        diffColor1.setStyle("-fx-background-color: " + DiffCol + "; -fx-background-radius: 10;");
    }
}
