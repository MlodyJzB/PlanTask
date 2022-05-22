package com.app.app.settings;

import com.app.ICSFiles.ToICSFileWritter;
import com.app.app.Event;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DetailedDayView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.page.MonthPage;
import com.calendarfx.view.page.WeekPage;
import com.calendarfx.view.page.YearPage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

public class Ics implements Initializable {
    @FXML
    public Button SaveButton;
    @FXML
    public Button ImportButton;

    public List<Event> eventList1 = new ArrayList<>();

    public void setUserEventsList(DetailedDayView detailedDayView, LocalDate startRangeDate, LocalDate endRangeDate) {
        com.calendarfx.model.Calendar calendar = detailedDayView.getCalendarSources().get(0).getCalendars().get(0);
        Map<LocalDate, List<Entry<?>>> entryMap = calendar.findEntries(
                startRangeDate,
                endRangeDate,
                ZonedDateTime.now().getZone()
        );

        List<List<Entry<?>>> entryLists = new ArrayList<>(entryMap.values());
        Collections.reverse(entryLists);
        for (List<Entry<?>> entryList : entryLists) {
            //Different entryList for every day
            for (Entry<?> entry : entryList) {
                //May be multiple entries in one day
                eventList1.add(Event.toEvent(entry));
                System.out.println(entry.toString());

            }
        }

    }



    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public void importIcs(ActionEvent actionEvent) {
        System.out.println("import");
        Stage stage = (Stage) ImportButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ICS Files","*.ics"));
        File file = fileChooser.showOpenDialog(stage);
        System.out.println(file.getName());

    }

    public void saveIcs(ActionEvent actionEvent) throws IOException, ParseException {
        System.out.println("save");
        Stage stage = (Stage) SaveButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ICS Files", "*.ics"));
        File savefile = fileChooser.showSaveDialog(stage);
        ToICSFileWritter toIcs = new ToICSFileWritter();
        toIcs.CalendarToICS(savefile, eventList1);

    }
}