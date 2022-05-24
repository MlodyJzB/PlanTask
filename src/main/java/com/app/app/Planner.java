package com.app.app;

import com.app.loginapp.Database;
import com.app.loginapp.User;
import com.calendarfx.view.DetailedDayView;
import com.calendarfx.view.page.WeekPage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ResourceBundle;

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
        boolean InfoDayNight = Database.getAppearance(user.getUsername()).get(0);
        try {
            this.DayMode(InfoDayNight);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void bindWeekPage(DetailedDayView detailedDayView) {
        detailedDayView.bind(weekPage, true);
    }

    @FXML
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
