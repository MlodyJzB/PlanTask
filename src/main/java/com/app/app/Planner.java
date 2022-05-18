package com.app.app;

import com.app.loginapp.LoginPanelController;
import com.app.loginapp.User;
import com.calendarfx.model.*;
import com.calendarfx.model.Calendar;
import com.calendarfx.view.*;
import com.calendarfx.view.page.WeekPage;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.SetChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.*;

public class Planner implements Initializable {
    User user;
    @FXML
    private ImageView minimalize_button;
    @FXML
    private AnchorPane diffcolor, pane3;
    @FXML
    private WeekPage weekPage;
    private String NormCol, DiffCol, BackCol;
    @FXML
    private void Minimize_clicked() {
        Stage stage = (Stage) minimalize_button.getScene().getWindow();
        //stage.setIconified(true);
        stage.setMaximized(!stage.isMaximized());
        //Restore down
        stage.setMaximized(stage.isMaximized());
    }

//    private void onClick(ActionEvent event) {
//        DetailedWeekView detailedWeekView = weekPage.getDetailedWeekView();
//
//        List<Calendar> calendarList = detailedWeekView.getCalendars();
//        Calendar calendar = calendarList.get(0);
//        int j=0;
//            Map<LocalDate, List<Entry<?>>> entryMap = calendar.findEntries(
//                    LocalDate.now().with(DayOfWeek.MONDAY),
//                    LocalDate.now().with(DayOfWeek.SUNDAY),
//                    ZonedDateTime.now().getZone()
//            );
//            List<List<Entry<?>>> entryLists = new ArrayList<>(entryMap.values());
//            Collections.reverse(entryLists);
//            for (List<Entry<?>> entryList : entryLists) {
//                //Different entryList for every day
//                System.out.print("\n\t" + j++);
//                int k=0;
//                for (Entry<?> entry : entryList) {
//                    //May be multiple entries in one day
//                    System.out.print("\t" + k++);
//                    System.out.print("\t" + entry.toString());
//                }
//            }
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = User.getInstance();
        System.out.println("User name: \"" + user.getUsername() + "\"");
        boolean a;
        try {
            a = (boolean) new LoginPanelController().getInfo(new AppPanel().whichUserClicked()).get(4);
            this.DayMode(a);
        } catch (JSONException | IOException e) {e.printStackTrace();}


        weekPage.getDetailedWeekView().getWeekView().getCalendars().addListener((ListChangeListener<Calendar>) c -> {
            c.next();
            if (c.getAddedSize()>0) {
                Calendar calendar = weekPage.getDetailedWeekView().getWeekView().getCalendars().get(0);
                calendar.addEventHandler(calendarEvent -> {
                    if (calendarEvent.isEntryAdded()) {
                        ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);
                        Task<Void> task = Event.addEntryToDatabase(
                                calendarEvent.getEntry(), user.getUsername());
                        executor.schedule(task, 5, TimeUnit.SECONDS);
                        executor.shutdown();
                    }

                    else if (calendarEvent.isEntryRemoved()) {
                        ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);
                        Task<Void> task = Event.removeEntryFromDatabase(
                                calendarEvent.getEntry(), user.getUsername());
                        executor.schedule(task, 5, TimeUnit.SECONDS);
                        executor.shutdown();
                    }

                    else {
                        if (calendarEvent.getOldInterval() != null) {
                            ScheduledExecutorService  executor = new ScheduledThreadPoolExecutor(1);
                            Task<Void> task =Event.changeEntryIntervalInDatabase(calendarEvent.getOldInterval(),
                                    calendarEvent.getEntry(), user.getUsername());
                            executor.schedule(task, 5, TimeUnit.SECONDS);
                            executor.shutdown();
                        }
                        else if (calendarEvent.getOldText() != null) {
                            ScheduledExecutorService  executor = new ScheduledThreadPoolExecutor(1);
                            Task<Void> task = Event.changeEntryTitleInDatabase(calendarEvent.getOldText(),
                                    calendarEvent.getEntry(), user.getUsername());
                            executor.schedule(task, 5, TimeUnit.SECONDS);
                            executor.shutdown();
                        }

                    }
                });
            }

        });
    }


    public void Exit() {
        System.exit(0);
    }

    public void ColourFromDataJson(boolean DayMode) throws IOException, JSONException {
        String contents = new String((Files.readAllBytes(Paths.get("colors.json"))));
        JSONObject o = new JSONObject(contents);
        if(DayMode) {
            NormCol = (String) o.get(("BrightColorNormal"));
            DiffCol = (String) o.get(("BrightColorDifferent"));
            BackCol = (String) o.get(("BrightColorBackground"));
            weekPage.getStylesheets().removeAll();
        }
        else{
            NormCol = (String) o.get(("DarkColorNormal"));
            DiffCol = (String) o.get(("DarkColorDifferent"));
            BackCol = (String) o.get(("DarkColorBackground"));
            weekPage.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Planer.css")).toExternalForm());
        }
    }

    private void DayMode(boolean DayMode) throws JSONException, IOException {
        this.ColourFromDataJson(DayMode);
        pane3.setStyle("-fx-background-color: " + BackCol + "; -fx-background-radius: 0 15 15 0;");
        diffcolor.setStyle("-fx-background-color: " + DiffCol + "; -fx-background-radius: 10;");
        weekPage.setStyle("-fx-background-color: " + NormCol + "; -fx-background-radius: 10;");
    }
}
