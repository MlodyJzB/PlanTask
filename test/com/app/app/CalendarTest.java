package com.app.app;

import javafx.scene.control.Button;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalendarTest {

    @Test
    void isSpecialDay() {
        Calendar cal = new Calendar();
        assertEquals("New Year", cal.isSpecialDay(LocalDate.of(2034,1,1)));
        assertEquals("Christmas", cal.isSpecialDay(LocalDate.of(2014,12,25)));
        assertEquals("Dzień Niepodległości", cal.isSpecialDay(LocalDate.of(2022,11,11)));
        assertEquals("Easter Monday", cal.isSpecialDay(LocalDate.of(2022,4,18)));
        assertEquals("Easter", cal.isSpecialDay(LocalDate.of(2022,4,17)));
        assertEquals("Boże Ciało", cal.isSpecialDay(LocalDate.of(2022,6,16)));
    }

    @Test
    void isSpecialDayWeekend() {
        Calendar cal = new Calendar();
        assertEquals("1", cal.isSpecialDay(LocalDate.of(2022,4,2)));
        assertEquals("1", cal.isSpecialDay(LocalDate.of(2022,7,10)));
        assertEquals("1", cal.isSpecialDay(LocalDate.of(2022,5,29)));
        assertEquals("1", cal.isSpecialDay(LocalDate.of(2022,5,28)));

    }

    @Test
    void isSpecialDayButNo() {
        Calendar cal = new Calendar();

        assertEquals("", cal.isSpecialDay(LocalDate.of(2022,7,11)));
        assertEquals("", cal.isSpecialDay(LocalDate.of(2022,6,29)));
        assertEquals("", cal.isSpecialDay(LocalDate.of(2022,5,27)));

    }

    @Test
    void isToday() {
        Calendar cal = new Calendar();
        assertEquals(true,cal.isToday(LocalDate.now()));
    }

    @Test
    void isTodayButNot() {
        Calendar cal = new Calendar();
        assertEquals(false,cal.isToday(LocalDate.now().minusMonths(1)));
    }

    @Test
    void getAmountOfEvens() {
        Calendar cal = new Calendar();
        List<Event> list = new ArrayList<>();
        Event event = new Event("test1", LocalDateTime.now(),LocalDateTime.now().plusHours(2), false);
        list.add(event);
        assertEquals(1, cal.getAmountOfEvens(list));
    }

    @Test
    void getAmountOfEvens0Events() {
        Calendar cal = new Calendar();
        List<Event> list = new ArrayList<>();

        assertEquals(0, cal.getAmountOfEvens(list));
    }

    @Test
    void getAmountOfEvensWithFullDay() {
        Calendar cal = new Calendar();
        List<Event> list = new ArrayList<>();
        Event event = new Event("test1", LocalDateTime.now(),LocalDateTime.now().plusHours(2), false);
        Event event2 = new Event("test2", LocalDateTime.now(),LocalDateTime.now().plusHours(2), true);
        list.add(event);
        list.add(event2);
        assertEquals(2, cal.getAmountOfEvens(list));
    }

}