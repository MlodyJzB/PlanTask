package com.app.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Calendar {

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

    private double OldStagex;
    private double OldStagey;

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

    @FXML
    private void initialize() {
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
                changeTextColour(day, "#000000", "  #e8e8e8");
                if(isSpecialDay(getButtonDate(day))) changeTextColour(day, "#d66813", "  #e8e8e8");
                if (isToday(getButtonDate(day))) changeTextColour(day, "#3516ff", "  #e8e8e8");

            } else {
                day.setText("");
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
        if (!((Button) actionEvent.getSource()).getText().equals("")) {
            LocalDate dayDate = LocalDate.of(currentdate.getYear(), currentdate.getMonth(), Integer.parseInt(((Button) actionEvent.getSource()).getText()) );
            System.out.println(dayDate);
        }
    }

    public boolean isSpecialDay(LocalDate date){
        /* checking if day is special */
        if(date.getDayOfWeek().getValue() == 7) return true;
        if(date.getDayOfWeek().getValue() == 6) return true;
        if(date.getMonthValue() == 1 && date.getDayOfMonth()==1) return true;
        if(date.getMonthValue() == 5 && date.getDayOfMonth()==1) return true;
        if(date.getMonthValue() == 5 && date.getDayOfMonth()==3) return true;
        if(date.getMonthValue() == 8 && date.getDayOfMonth()==15) return true;
        if(date.getMonthValue() == 11 && date.getDayOfMonth()==1) return true;
        if(date.getMonthValue() == 11 && date.getDayOfMonth()==11) return true;
        if(date.getMonthValue() == 12 && date.getDayOfMonth()==25) return true;
        if(date.getMonthValue() == 12 && date.getDayOfMonth()==26) return true;
        int variable1 = date.getYear() % 19;
        int variable2 = date.getYear() % 4;
        int variable3 = date.getYear() % 7;
        int variable4 = (variable1 * 19 + 24) % 30;
        int variable5 = (2 * variable2 +4 * variable3 + 6 * variable4 +5) % 7;
        if(variable4 == 29 && variable5 == 6) variable4 -= 7;
        if(variable4 == 28 && variable5 == 6 && variable1 > 10) variable4 -= 7;
        LocalDate easter = LocalDate.of(date.getYear(), 3, 22).plusDays(variable4 + variable5);

        if (date.minusDays(1).equals(easter)) return true;
        if (date.minusDays(60).equals(easter)) return true;
        return false;

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

    LocalDate getButtonDate(@NotNull Button button){
        LocalDate dayDate = LocalDate.of(currentdate.getYear(), currentdate.getMonth(), Integer.parseInt(button.getText()));
        return dayDate;
    }

    void drawMonth(){
        /*  updating buttons and labels */
        drawNewCall();
        refreschLabel();
    }

    private void settingButtonLayouts(Stage stage, Button button){
        button.setLayoutX((stage.getWidth()-OldStagex)/2+button.getLayoutX());
        button.setLayoutY((stage.getHeight()-OldStagey)/2+button.getLayoutY());
    }

    private void settingLabelsLayouts(Stage stage, Label label){
        label.setLayoutX((stage.getWidth()-OldStagex)/2+label.getLayoutX());
        label.setLayoutY((stage.getHeight()-OldStagey)/2+label.getLayoutY());
    }

    private void resizeCallendar(Stage stage){
        for (var day : buttonList){
            settingButtonLayouts(stage, day);
        }

        settingButtonLayouts(stage, previousb);
        settingButtonLayouts(stage, nextb);
        settingLabelsLayouts(stage, monLabel);
        settingLabelsLayouts(stage, tueLabel);
        settingLabelsLayouts(stage, wedLabel);
        settingLabelsLayouts(stage, thuLabel);
        settingLabelsLayouts(stage, friLabel);
        settingLabelsLayouts(stage,satLabel);
        settingLabelsLayouts(stage,sunLabel);
        settingLabelsLayouts(stage,monthLabel);

    }

    public void Exit() {
        System.exit(0);
    }
    @FXML
    private ImageView minimalize_button;

    @FXML
    private void Minimize_clicked() {
        Stage stage = (Stage) minimalize_button.getScene().getWindow();
        OldStagex = stage.getWidth();
        OldStagey = stage.getHeight();
        //stage.setIconified(true);
        stage.setMaximized(!stage.isMaximized());
        //Restore down
        stage.setMaximized(stage.isMaximized());
        resizeCallendar(stage);
    }

}
