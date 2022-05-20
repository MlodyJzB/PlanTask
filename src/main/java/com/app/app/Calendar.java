package com.app.app;

import com.app.loginapp.LoginPanelController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Calendar implements Initializable {

    /* adding buttons and labels */
    @FXML
    private Button mon1;
    @FXML
    private Button mon2;
    @FXML
    private Button mon3;
    @FXML
    private Button mon4;
    @FXML
    private Button mon5;
    @FXML
    private Button mon6;
    @FXML
    private Button tue6;
    @FXML
    private Button tue5;
    @FXML
    private Button tue4;
    @FXML
    private Button tue3;
    @FXML
    private Button tue2;
    @FXML
    private Button tue1;
    @FXML
    private Button wed1;
    @FXML
    private Button wed2;
    @FXML
    private Button wed3;
    @FXML
    private Button wed4;
    @FXML
    private Button wed5;
    @FXML
    private Button thu1;
    @FXML
    private Button thu2;
    @FXML
    private Button thu3;
    @FXML
    private Button thu4;
    @FXML
    private Button thu5;
    @FXML
    private Button fri1;
    @FXML
    private Button fri2;
    @FXML
    private Button fri3;
    @FXML
    private Button fri4;
    @FXML
    private Button fri5;
    @FXML
    private Button sat1;
    @FXML
    private Button sat2;
    @FXML
    private Button sat3;
    @FXML
    private Button sat4;
    @FXML
    private Button sat5;
    @FXML
    private Button sun1;
    @FXML
    private Button sun2;
    @FXML
    private Button sun3;
    @FXML
    private Button sun4;
    @FXML
    private Button sun5;
    @FXML
    private Button nextb;
    @FXML
    private Button previousb;
    @FXML
    private AnchorPane pane3;
    private boolean darkmode=false;

    private List<Button> buttonList = new ArrayList<>();

    @FXML
    private Label monthLabel;
    @FXML
    private Label monLabel;
    @FXML
    private Label tueLabel;
    @FXML
    private Label wedLabel;
    @FXML
    private Label thuLabel;
    @FXML
    private Label friLabel;
    @FXML
    private Label satLabel;
    @FXML
    private Label sunLabel;
    private LocalDate currentdate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonList.add(mon1);
        buttonList.add(tue1);
        buttonList.add(wed1);
        buttonList.add(thu1);
        buttonList.add(fri1);
        buttonList.add(sat1);
        buttonList.add(sun1);
        buttonList.add(mon2);
        buttonList.add(tue2);
        buttonList.add(wed2);
        buttonList.add(thu2);
        buttonList.add(fri2);
        buttonList.add(sat2);
        buttonList.add(sun2);
        buttonList.add(mon3);
        buttonList.add(tue3);
        buttonList.add(wed3);
        buttonList.add(thu3);
        buttonList.add(fri3);
        buttonList.add(sat3);
        buttonList.add(sun3);
        buttonList.add(mon4);
        buttonList.add(tue4);
        buttonList.add(wed4);
        buttonList.add(thu4);
        buttonList.add(fri4);
        buttonList.add(sat4);
        buttonList.add(sun4);
        buttonList.add(mon5);
        buttonList.add(tue5);
        buttonList.add(wed5);
        buttonList.add(thu5);
        buttonList.add(fri5);
        buttonList.add(sat5);
        buttonList.add(sun5);
        buttonList.add(mon6);
        buttonList.add(tue6);
        currentdate = LocalDate.now();
        resizeCalendar(650.0,1045.0,-110, 20,120,80,18);

        try {
            boolean a = (boolean) new LoginPanelController().getInfo(new AppPanel().whichUserClicked()).get(4);
            this.DayMode(a);
            darkmode = !a;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (com.app.WeatherInfo.NonexistentZipCodeException e) {
            e.printStackTrace();
        }
        drawMonth();
    }

    public void drawNewCall() {
        LocalDate beginnDay = currentdate.withDayOfMonth(1);
        DayOfWeek weekDay = beginnDay.getDayOfWeek();
        int first = weekDay.getValue();
        int monthLenght = currentdate.lengthOfMonth();
        int days = 1;
        int weekDays = 1;
        for (var day : buttonList) {
            if (weekDays >= first && days <= monthLenght) {
                day.setText(String.valueOf(days));
                days++;
                if(darkmode){
                    changeTextColour(day, "#1c1c1c", "  #424242");
                    if (!isSpecialDay(getButtonDate(day)).equals("")) changeTextColour(day, "#d66813", "  #424242");
                    if (isToday(getButtonDate(day))) changeTextColour(day, "#3516ff", "  #424242");
                }
                else {
                    changeTextColour(day, "#000000", "  white");
                    if (!isSpecialDay(getButtonDate(day)).equals("")) changeTextColour(day, "#d66813", "  white");
                    if (isToday(getButtonDate(day))) changeTextColour(day, "#3516ff", "  white");
                }
            } else {
                day.setText("");
                if(darkmode){
                    changeTextColour(day, "#1c1c1c", "  #424242");
                }
                else {
                    changeTextColour(day, "#000000", "  white");
                }
            }
            weekDays++;

        }
    }

    public void previousMonth(ActionEvent actionEvent) {
        /* changing month to previous, updating buttons and labels */
        currentdate = currentdate.minusMonths(1);
        drawMonth();

    }


    public void nextMonth(ActionEvent actionEvent) {
        /* changing month to previous and updating buttons */
        currentdate = currentdate.plusMonths(1);
        drawMonth();

    }

    public void getDay(ActionEvent actionEvent) {
        Stage stage = (Stage) minimalize_button.getScene().getWindow();
        if (!((Button) actionEvent.getSource()).getText().equals("")) {
            LocalDate dayDate =getButtonDate((Button) actionEvent.getSource());
            System.out.println(dayDate);
            Popup popup = new Popup();
            String nameOfday = isSpecialDay(dayDate);
            if  (nameOfday.equals("") || nameOfday.equals("1") ) {
                nameOfday = "";
            }
            Label noEvents = new Label("no events");
            noEvents.setStyle("-fx-text-fill: grey;");
            Label dateLabel = new Label(String.valueOf(dayDate));
            Label background = new Label();
            Label specialDay = new Label(nameOfday);
            background.setMinSize(500,350);
            background.setLayoutX(background.getLayoutX()+50);
            background.setLayoutY(background.getLayoutY()+55);
            noEvents.setLayoutX(noEvents.getLayoutX()+230+50);
            noEvents.setLayoutY(noEvents.getLayoutY()+160+55);
            dateLabel.setFont(Font.font("System", FontWeight.BOLD, 25));
            dateLabel.setLayoutX(dateLabel.getLayoutX()+185+50);
            dateLabel.setLayoutY(dateLabel.getLayoutY()+55);
            background.setStyle(" -fx-background-color: #e8e8e8; -fx-background-radius: 10;");
            specialDay.setTextAlignment(TextAlignment.CENTER);
            specialDay.setLayoutX(specialDay.getLayoutX()+260+50- nameOfday.length()*3);
            specialDay.setLayoutY(specialDay.getLayoutY()+90);
            popup.getContent().add(background);
            popup.getContent().add(dateLabel);
            popup.getContent().add(noEvents);
            popup.getContent().add(specialDay);
            popup.setAutoHide(true);
            if (! popup.isShowing()) {
                popup.show(stage);

            }
        }
    }

    public String isSpecialDay(LocalDate date){
        /* checking if day is special */
        if(date.getMonthValue() == 1 && date.getDayOfMonth()==1) return "New Year";
        if(date.getMonthValue() == 5 && date.getDayOfMonth()==1) return "Święto Pracy";
        if(date.getMonthValue() == 5 && date.getDayOfMonth()==3) return "Święto Konstytucji";
        if(date.getMonthValue() == 8 && date.getDayOfMonth()==15) return "Wniebowzięcie Najświętszej Marii Panny";
        if(date.getMonthValue() == 11 && date.getDayOfMonth()==1) return "Dzień Wszystkich Świętych";
        if(date.getMonthValue() == 11 && date.getDayOfMonth()==11) return "Dzień Niepodległości";
        if(date.getMonthValue() == 12 && date.getDayOfMonth()==25) return "Christmas";
        if(date.getMonthValue() == 12 && date.getDayOfMonth()==26) return "Christmas";
        int variable1 = date.getYear() % 19;
        int variable2 = date.getYear() % 4;
        int variable3 = date.getYear() % 7;
        int variable4 = (variable1 * 19 + 24) % 30;
        int variable5 = (2 * variable2 +4 * variable3 + 6 * variable4 +5) % 7;
        if(variable4 == 29 && variable5 == 6) variable4 -= 7;
        if(variable4 == 28 && variable5 == 6 && variable1 > 10) variable4 -= 7;
        LocalDate easter = LocalDate.of(date.getYear(), 3, 22).plusDays(variable4 + variable5);

        if (date.minusDays(1).equals(easter)) return "Easter";
        if (date.minusDays(60).equals(easter)) return "Boże Ciało";
        if(date.getDayOfWeek().getValue() == 7) return "1";
        if(date.getDayOfWeek().getValue() == 6) return "1";
        return "";

    }

    private void refreschLabel(){
        Month currentMonth = currentdate.getMonth();
        monthLabel.setText(String.valueOf(currentMonth) + "  " + String.valueOf(currentdate.getYear()));
    }

    boolean isToday(@NotNull LocalDate localDate){

        if (localDate.equals(LocalDate.now())) return true;
        return false;
    }

    void changeTextColour(@NotNull Button button, String colour, String backColour){
        button.setStyle("-fx-text-fill: "+colour+";"+" -fx-background-color: "+backColour+";");
    }

    void changeLabelTextColour(@NotNull Label label, String colour){
        label.setStyle("-fx-text-fill: "+colour+";");
    }

    LocalDate getButtonDate(@NotNull Button button){
        LocalDate dayDate = LocalDate.of(currentdate.getYear(), currentdate.getMonth(), Integer.parseInt(button.getText()));
        return dayDate;
    }

    void drawMonth(){
        /*  updating buttons and labels */
        drawNewCall();
        refreschLabel();
        if (darkmode) {
            //pane3.setStyle(" -fx-background-color: #1c1c1c;");
            changeTextColour(nextb," #1c1c1c"," #424242");
            changeTextColour(previousb," #1c1c1c"," #424242");
            changeLabelTextColour(monLabel," #1c1c1c");
            changeLabelTextColour(thuLabel," #1c1c1c");
            changeLabelTextColour(tueLabel," #1c1c1c");
            changeLabelTextColour(wedLabel," #1c1c1c");
            changeLabelTextColour(friLabel," #1c1c1c");
            changeLabelTextColour(satLabel," #1c1c1c");
            changeLabelTextColour(sunLabel," #1c1c1c");
            changeLabelTextColour(monthLabel," #1c1c1c");

        }
        else
        {
            //pane3.setStyle(" -fx-background-color: #e8e8e8;");
            changeTextColour(nextb," #000000"," white");
            changeTextColour(previousb," #000000"," white");
            changeLabelTextColour(monLabel," #000000");
            changeLabelTextColour(thuLabel," #000000");
            changeLabelTextColour(tueLabel," #000000");
            changeLabelTextColour(wedLabel," #000000");
            changeLabelTextColour(friLabel," #000000");
            changeLabelTextColour(satLabel," #000000");
            changeLabelTextColour(sunLabel," #000000");
            changeLabelTextColour(monthLabel," #000000");
        }
    }

    private void settingButtonLayouts(Button button, double spacex, double spacey, double Width, double Height, int offx, int offy){
        button.setLayoutX((Width-710)/2+offx+spacex);
        button.setLayoutY((Height-417)/2+offy+spacey);
    }

    private void settingLabelsLayouts( Label label, double spacex, double spacey, double Width, double Height, int offx, int offy){

        label.setLayoutX((Width-710)/2+offx+spacex);
        label.setLayoutY((Height-417)/2+offy+spacey);
    }

    private void resizeCalendar(double Height, double Width, int offx, int offy, int spacesx, int spacesy, int font ){
        int i = 0;
        int j = 0;

        for (var day : buttonList){
            settingButtonLayouts(day,spacesx*i,spacesy*j, Width,Height, offx, offy);
            day.setFont(new Font(font));

            i++;
            if (i >=7){
                i = 0;
                j++;
            }

        }


        settingButtonLayouts(previousb, spacesx*1, spacesy*0,Width,Height,offx+10,offy-90);
        settingButtonLayouts(nextb, spacesx*5, spacesy*0,Width,Height,offx+10,offy-90);

        previousb.setFont(Font.font("System", FontWeight.BOLD, font));

        nextb.setFont(Font.font("System", FontWeight.BOLD, font));

        settingLabelsLayouts(monthLabel, spacesx*3, spacesy*0,Width,Height,offx-60,offy-90);
        monthLabel.setFont(new Font(font-2));
        settingLabelsLayouts(monLabel, spacesx*0, spacesy*0,Width,Height,offx+20,offy-20);
        settingLabelsLayouts(tueLabel, spacesx*1, spacesy*0,Width,Height,offx+20,offy-20);
        settingLabelsLayouts(wedLabel, spacesx*2, spacesy*0,Width,Height,offx+20,offy-20);
        settingLabelsLayouts(thuLabel, spacesx*3, spacesy*0,Width,Height,offx+20,offy-20);
        settingLabelsLayouts(friLabel, spacesx*4, spacesy*0,Width,Height,offx+20,offy-20);
        settingLabelsLayouts(satLabel, spacesx*5, spacesy*0,Width,Height,offx+20,offy-20);
        settingLabelsLayouts(sunLabel, spacesx*6, spacesy*0,Width,Height,offx+20,offy-20);
    }

    public void Exit() {
        System.exit(0);
    }
    @FXML
    private ImageView minimalize_button;

    @FXML
    private void Minimize_clicked() {
        Stage stage = (Stage) minimalize_button.getScene().getWindow();
        //stage.setIconified(true);
        stage.setMaximized(!stage.isMaximized());
        //Restore down
        stage.setMaximized(stage.isMaximized());

        if(stage.isMaximized()) {

            resizeCalendar(pane3.getHeight(),pane3.getWidth(),-210,20,180,90,21);
        }
        else
        {
            resizeCalendar(650.0,1045.0,-110, 20,120,80,18);
        }
    }
    @FXML
    private AnchorPane diffColor1, normalColor;

    private String NormCol, DiffCol, BackCol;

    public void ColourFromDataJson(boolean DayMode) throws IOException, JSONException {
        String contents = new String((Files.readAllBytes(Paths.get("colors.json"))));
        JSONObject o = new JSONObject(contents);
        if(DayMode) {
            NormCol = (String) o.get(("BrightColorNormal"));
            DiffCol = (String) o.get(("BrightColorDifferent"));
            BackCol = (String) o.get(("BrightColorBackground"));
        }
        else{
            NormCol = (String) o.get(("DarkColorNormal"));
            DiffCol = (String) o.get(("DarkColorDifferent"));
            BackCol = (String) o.get(("DarkColorBackground"));
        }
    };

    private void DayMode(boolean DayMode) throws JSONException, IOException {
        this.ColourFromDataJson(DayMode);
        pane3.setStyle("-fx-background-color: " + BackCol + "; -fx-background-radius: 0 15 15 0;");
        diffColor1.setStyle("-fx-background-color: " + DiffCol + "; -fx-background-radius: 10;");
        normalColor.setStyle("-fx-background-color: " + NormCol + "; -fx-background-radius: 10;");
    }
}
