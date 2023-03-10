package com.app.app;

import com.app.WeatherInfo.IncorrectZipCodeFormatException;
import com.app.WeatherInfo.NonexistentZipCodeException;
import com.app.WeatherInfo.WeatherInfo;
import com.app.loginapp.Database;
import com.app.loginapp.LoginApplication;
import com.app.loginapp.User;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.MonthView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.Window;
import org.controlsfx.control.Notifications;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AppPanel implements Initializable {
    User user;
    private WeatherInfo wi = new WeatherInfo();
    @FXML
    private Text HourInfo, DateInfo;
    @FXML
    private BorderPane ourWindow;
    @FXML
    private VBox Incoming_events_Vbox, SideBarcolor;
    @FXML
    private Button weatherTitleButton;
    @FXML
    private Label Temp, MinTemp, MaxTemp, Clouds, Wind, Hum, Sunrise, Sunset, Label1, Label2;
    @FXML
    private ImageView WeatherIcon;
    @FXML
    private AnchorPane backgroundColor, diffColor2, normColor4, normColor3, normColor2, normColor1;
    @FXML
    private HBox diffColor1;
    @FXML
    private MonthView monthView;
    @FXML
    private DetailedDayView detailedDayView;
    @FXML
    private ImageView minimalize_button, minimalize_button1;


    private Map<LocalDate, List<Entry<?>>> userEventsMap = new HashMap<>();
    private String BackCol, SideCol, NormCol, DiffCol;

    private SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm");


    public String[] colorArray(boolean mode) throws JSONException, IOException {
        ColourFromDataJson(mode, false);
        return new String[]{BackCol, SideCol, NormCol, DiffCol};
    }

    public AppPanel() throws NonexistentZipCodeException, JSONException, IOException {}
    private volatile boolean stop = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = User.getInstance();
        user.setDayMode(Database.getAppearance(user.getUsername()));
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Task is on");
            };
        };
        try {
            t.schedule(tt, this.wi.getSunset(0));
        } catch (JSONException e) {e.printStackTrace();};
        try {
            ColourFromDataJson(user.isDayMode(), true);
        } catch (IOException | JSONException e) {e.printStackTrace();}
        DayMode();
        minimalize_button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                Stage primaryStage = (Stage) minimalize_button1.getScene().getWindow();
                primaryStage.setIconified(true);
            }
        });
        Calendar calendar = detailedDayView.getCalendarSources().get(0).getCalendars().get(0);
        List<Entry<String>> entryList = Database.getUserEntries(user.getUsername(),
                LocalDateTime.now().minusYears(1), LocalDateTime.now().plusYears(1));
        for (Entry<String> entry : entryList)
            calendar.addEntry(entry);

        detailedDayView.bind(monthView, true);

        calendar.addEventHandler(calendarEvent -> {
            if (calendarEvent.isEntryAdded()) {
                System.out.println(calendarEvent.getEntry());
                Database.addEvent(calendarEvent.getEntry(), user.getUsername()
                );
            } else if (calendarEvent.isEntryRemoved()) {
                System.out.println(calendarEvent.getEntry());
                Database.removeEvent(calendarEvent.getEntry(), user.getUsername()
                );
            } else {
                System.out.println(calendarEvent.getEntry());
                if (calendarEvent.getOldInterval() != null) {
                    Database.changeEventInterval(calendarEvent.getOldInterval(),
                            calendarEvent.getEntry(), user.getUsername()
                    );
                } else if (calendarEvent.getOldText() != null) {
                    Database.changeEventTitle(calendarEvent.getOldText(),
                            calendarEvent.getEntry(), user.getUsername()
                    );
                } else if (calendarEvent.getOldFullDay() != calendarEvent.getEntry().isFullDay()) {
                    System.out.println("Changed fullDay!");
                    Database.changeEventFullDay(calendarEvent.getOldFullDay(),
                            calendarEvent.getEntry(), user.getUsername()
                    );
                } else {
                    System.out.println("Changed recurrence!");
                    Database.ChangeEventRecurringAndRrule(
                            calendarEvent.getEntry(), user.getUsername()
                    );
                }
            }
        });

        /*
        Tu dodaje si?? info o zadaniach w jsonie i wrzuczmy do funkcji add_event (byle by
        typ wydarzenia by?? torzsamy z nazw?? zdj co je opisuje w Images, czas w stringu 13:21
        (powy??ej przyk??ad))
        */

        try {
            incomingEv();
        } catch (JSONException | FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }
        String onlineZip = Database.getZipCode(user.getUsername());
        String localZip = wi.getZipCode();

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

        try {
            if (onlineZip.equals(localZip)) {
                wi.updateOnline();
            }
            else{
                wi.updateOnline(onlineZip);
            }
            this.setWeather();
        } catch (JSONException|IOException|NonexistentZipCodeException|IncorrectZipCodeFormatException e) {
            e.printStackTrace();
        }
        int updateTime = 1800;
        AtomicInteger seconds = new AtomicInteger();
        Thread weatherUpdate = new Thread(()-> {
            while(!stop){
                if(seconds.get() == updateTime) {
                    seconds.set(0);
                    Platform.runLater(() -> {
                        try {
                            this.wi.updateOnline();
                            this.setWeather();
                        } catch (NonexistentZipCodeException | JSONException | IOException |
                                 IncorrectZipCodeFormatException e) {
                            e.printStackTrace();
                        }
                    });
                    seconds.getAndIncrement();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        class RemindTask extends TimerTask {
            RemindTask(String text) {this.text=text;}
            String text;
            public void run() {
                Platform.runLater(()->showNotification(text).showInformation());
            }
            public Notifications showNotification(String text){
                Notifications notificationBuilder = Notifications.create()
                        .title("Event is starting")
                        .text(text)
                        .darkStyle()
                        .position(Pos.BOTTOM_RIGHT);
                return notificationBuilder;
            }
        }
        weatherUpdate.start();
        Timeline timeline = new Timeline(
                new KeyFrame(javafx.util.Duration.seconds(10),
                        e -> {
                            Timer timer = new Timer();
                            for (var incomingStart:listToSchedule.keySet()) {
                                java.util.Calendar notif = java.util.Calendar.getInstance();
                                notif.set(java.util.Calendar.HOUR_OF_DAY, incomingStart.getHour());
                                notif.set(java.util.Calendar.MINUTE, incomingStart.getMinute());
                                notif.set(java.util.Calendar.SECOND, incomingStart.getSecond());
                                Date time = notif.getTime();
                                timer.schedule(new RemindTask(listToSchedule.get(incomingStart)), time, 10);
                            }
                            try {
                                incomingEv();
                            } catch (JSONException | FileNotFoundException | ParseException ex) {
                                ex.printStackTrace();
                            }
                            timer.purge();
                            timer.cancel();
                        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private String enteredButtonStyle;
    @FXML
    private void onMouseEntered(MouseEvent event) {
        Button enteredButton = (Button) event.getSource();
        enteredButtonStyle = enteredButton.getStyle();
        enteredButton.setStyle("-fx-background-color: "+DiffCol + "; -fx-background-radius: 15;");
    }
    @FXML
    private void onMouseExited(MouseEvent event) {
        Button enteredButton = (Button) event.getSource();
        enteredButton.setStyle(enteredButtonStyle);
    }

    @FXML
    private void onMouseEnteredWeather(MouseEvent event) {
        Button enteredButton = (Button) event.getSource();
        enteredButtonStyle = enteredButton.getStyle();
        enteredButton.setStyle("-fx-background-color: "+SideCol+"; -fx-background-radius: 10;");
    }

    @FXML
    private void onMouseExitedWeather(MouseEvent event) {
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
    public void planner(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "planner.fxml"
                )
        );
        ourWindow.setCenter(loader.load());
        Planner controller = loader.getController();
        controller.bindWeekPage(detailedDayView);

    }
    public void calendar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "calendar.fxml"
                )
        );
        ourWindow.setCenter(loader.load());
        com.app.app.Calendar controller = loader.getController();
        controller.setUserEventsList(detailedDayView, LocalDate.now().minusYears(1), LocalDate.now().plusYears(1));
    }
    public void weather(ActionEvent event){
        LoadSite("weather");
    }
    public void GoBackToLoginPanel(ActionEvent event){
        Window owner = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        Stage stage1 = (Stage) owner.getScene().getWindow();
        try {
            new LoginApplication().start( new Stage() );
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage1.close();
    }
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
        com.app.app.settings.Settings controller = loader.getController();
        controller.detailedDayView =detailedDayView;
        stage.setScene(scene);
        //Settings controller = loader.getController();
        stage.show();
    }
    public void Exit() {
        stop = true;
        System.exit(0);
    }

    public void homepanel(ActionEvent event) throws NonexistentZipCodeException, JSONException, IOException, IncorrectZipCodeFormatException {
        wi.updateOffline();
        this.setWeather();
        try {
            incomingEv();
        } catch (JSONException | FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }
        ourWindow.setCenter(backgroundColor);
    }

    public HBox add_event(JSONObject j) throws JSONException, FileNotFoundException, ParseException {
        AnchorPane pane1 = new AnchorPane();
        Label name= new Label(j.getString("event_name"));
        Label from= new Label(j.getString("start"));
        Label to= new Label(j.getString("end"));
        Label duration= new Label();

        DateFormat df = new SimpleDateFormat("HH:mm");
        Date date1= df.parse(j.getString("start"));
        Date date2= df.parse(j.getString("end"));
        long t1 = date1.getTime();
        long t2 = date2.getTime();
        long duratio=-3600000;
        if(t2<t1){
            t2=t2+82800000+3600000;
        }
        duratio =  t2 - t1;
        String dura = Long.toString(((duratio / (60 * 60 * 1000))%24));
        String dura1 =  Long.toString(((duratio / (1000*60)) % 60));
        duration.setText(dura+":"+dura1+" h");

        pane1.setPrefSize(100, 46);

        name.setFont(new Font("Serif", 18));
        name.setLayoutY(46.0);
        name.setPrefSize(300, 46);
        name.setAlignment(Pos.BASELINE_LEFT);

        from.setFont(new Font("Serif", 18));
        from.setAlignment(Pos.CENTER);
        from.setPrefSize(200, 46);

        duration.setFont(new Font("Serif", 18));
        duration.setAlignment(Pos.CENTER);
        duration.setPrefSize(200, 46);

        to.setFont(new Font("Serif", 18));
        to.setAlignment(Pos.CENTER);
        to.setPrefSize(200, 46);

        HBox hbox = new HBox(pane1, name, duration, from, to);
        hbox.setStyle("-fx-background-color: "+DiffCol+"; -fx-background-radius: 10;");
        hbox.setPrefSize(200, 46);
        hbox.setSpacing(10);
        return hbox;
    }

    public void setWeather() throws NonexistentZipCodeException, JSONException, IOException, IncorrectZipCodeFormatException {

        String temp = String.valueOf(this.wi.getTemp(0));
        String minTemp = String.valueOf(this.wi.getMinTemp(0));
        String maxTemp = String.valueOf(this.wi.getMaxTemp(0));
        String wind = String.valueOf(this.wi.getWindSpeed(0));
        String clouds = String.valueOf(this.wi.getCloudsValue(0));
        String hum = String.valueOf(this.wi.getHumidity(0));
        String icon = this.wi.getIcon(0);
        long sunrise = this.wi.getSunrise(0);
        long sunset = this.wi.getSunset(0);

        String sunriseH = dateFormater.format(sunrise*1000L);
        String sunsetH = dateFormater.format(sunset*1000L);

        Temp.setText(temp + "??C");
        MinTemp.setText(minTemp + "??C");
        MaxTemp.setText(maxTemp + "??C");
        Wind.setText(wind + " km/h");
        Clouds.setText(clouds + "%");
        Hum.setText(hum + "%");
        Sunrise.setText(sunriseH);
        Sunset.setText(sunsetH);

        Image im = null;
        im = new Image(new FileInputStream("src/main/resources/Images/WeatherIcons/" + icon + ".png"));
        WeatherIcon.setImage(im);
    }

    public void ColourFromDataJson(boolean DayMode, boolean workingOnApp) throws IOException, JSONException {
        String contents = new String((Files.readAllBytes(Paths.get("colors.json"))));
        JSONObject o = new JSONObject(contents);
        Label[] labelColors = new Label[]{Label1, Label2};
        if(DayMode) {
            NormCol = (String) o.get(("BrightColorNormal"));
            DiffCol = (String) o.get(("BrightColorDifferent"));
            SideCol = (String) o.get(("BrightSideBarColor"));
            BackCol = (String) o.get(("BrightColorBackground"));
            if(workingOnApp) {
                monthView.getStylesheets().removeAll();
                detailedDayView.getStylesheets().removeAll();
            }
        }
        else{
            NormCol = (String) o.get(("DarkColorNormal"));
            DiffCol = (String) o.get(("DarkColorDifferent"));
            SideCol = (String) o.get(("DarkSideBarColor"));
            BackCol = (String) o.get(("DarkColorBackground"));
            if(workingOnApp) {
                monthView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("AppPanel1.css")).toExternalForm());
                detailedDayView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("AppPanel1.css")).toExternalForm());
                Incoming_events_Vbox.setStyle("-fx-background-color: #2b2b2b;");
                for (Label a : labelColors) {
                    a.setTextFill(Paint.valueOf("#ffffff"));
                }
            }
        }
    }

    private void DayMode() {
        AnchorPane[] normalColors = new AnchorPane[]{normColor4, normColor3, normColor2, normColor1};
        for (AnchorPane a : normalColors) {
            a.setStyle("-fx-background-color: " + NormCol + "; -fx-background-radius: 10;");
        }
        backgroundColor.setStyle("-fx-background-color: " + BackCol + "; -fx-background-radius: 0 15 15 0;");
        SideBarcolor.setStyle("-fx-background-color: " + SideCol + "; -fx-background-radius: 15 0 0 15;");
        diffColor2.setStyle("-fx-background-color: " + DiffCol + "; -fx-background-radius: 10;");
        weatherTitleButton.setStyle("-fx-background-color: " + DiffCol + "; -fx-background-radius: 10;");
        diffColor1.setStyle("-fx-background-color: " + DiffCol + "; -fx-background-radius: 10;");
    }

    public void setUserEventsMap(DetailedDayView detailedDayView, LocalDate startRangeDate, LocalDate endRangeDate) {
        com.calendarfx.model.Calendar calendar = detailedDayView.getCalendarSources().get(0).getCalendars().get(0);
        userEventsMap = calendar.findEntries(
                startRangeDate,
                endRangeDate,
                ZonedDateTime.now().getZone()
        );
    }
    private HashMap<LocalDateTime, String> listToSchedule = new HashMap<>();
    public void incomingEv() throws JSONException, FileNotFoundException, ParseException {
        setUserEventsMap(detailedDayView, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        List<Entry<?>> userEntriesList;
         userEntriesList = userEventsMap.get(LocalDate.now());
        List<Event> userEventsList = new ArrayList<>();
        if(!(userEntriesList==null)) {
            for (Entry<?> entry : userEntriesList)
                userEventsList.add(Event.toEvent(entry));
        }
        Incoming_events_Vbox.getChildren().setAll();
        listToSchedule.clear();
        for(var ev: userEventsList)
        {
            if((Duration.between(ev.getStartDateTime(), LocalDateTime.now()).toHours()<=2 &&
                    LocalDateTime.now().isBefore(ev.getStartDateTime())) ||
                    (Duration.between(ev.getStartDateTime(), LocalDateTime.now()).toMinutes()<=10) &&
                            LocalDateTime.now().isAfter(ev.getStartDateTime()) ) {
                if(LocalDateTime.now().isBefore(ev.getStartDateTime())) {
                    listToSchedule.put(ev.getStartDateTime(), ev.getTitle());
                }
                JSONObject jsonCalendar = new JSONObject();
                jsonCalendar.put("event_name", ev.getTitle());
                jsonCalendar.put("start", String.valueOf(ev.getStartDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
                jsonCalendar.put("end", String.valueOf(ev.getEndDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
                Incoming_events_Vbox.getChildren().add(add_event(jsonCalendar));
            }

        }
    }
}

