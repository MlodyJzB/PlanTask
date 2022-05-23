package com.app.ICSFiles;


import com.app.app.Event;
import com.app.loginapp.Database;
import com.app.loginapp.User;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ICSFilesReader {
    private Calendar icsCalendar;
    private List<JSONObject> listOfEvents = new ArrayList();

    public ICSFilesReader() {
    }

    public void icsReader(String icsFilePath) throws IOException, ParserException {
        this.icsCalendar = (new CalendarBuilder()).build(new FileReader(icsFilePath));
    }
    public void icsReader(File file) throws IOException, ParserException {
        this.icsCalendar = (new CalendarBuilder()).build(new FileReader(file));
    }

    public void creatingJSONList() throws JSONException {
        Iterator var1 = this.icsCalendar.getComponents().iterator();

        while(var1.hasNext()) {
            Object object = var1.next();
            Component component = (Component)object;
            JSONObject jsonCalendar = new JSONObject();
            String event_name = String.valueOf(component.getProperty("SUMMARY").getValue());
            String start = String.valueOf(component.getProperty("DTSTART").getValue());
            String end = String.valueOf(component.getProperty("DTEND").getValue());
            String var10000 = String.valueOf(component.getProperty("DESCRIPTION").getValue());
            String description = var10000 + String.valueOf(component.getProperty("LOCATION").getValue());
            String colour = "#F0FFF0";
            jsonCalendar.put("event_name", event_name);
            jsonCalendar.put("start", start);
            jsonCalendar.put("end", end);
            jsonCalendar.put("description", description);
            jsonCalendar.put("colour", colour);
            this.listOfEvents.add(jsonCalendar);
        }

    }

    public void icsToJSON(String icsFilePath, String jsonFilePath) throws ParserException, IOException, JSONException {
        this.icsReader(icsFilePath);
        this.creatingJSONList();
        Iterator<JSONObject> iter = this.listOfEvents.iterator();
        FileWriter file = new FileWriter(jsonFilePath);

        try {
            while(iter.hasNext()) {
                JSONObject json = (JSONObject)iter.next();
                file.write(json.toString());
                file.write("\n");
            }
        } catch (IOException var9) {
            var9.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }

    }

    private LocalDateTime dateFormat(String date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
        LocalDateTime formatedDate = LocalDateTime.parse(date.replace("T"," "), formatter);
        return formatedDate;
    }

    private boolean isInEventList(List<Event> list, Component component){
        for (var event:list){
            LocalDateTime startd = dateFormat(String.valueOf(component.getProperty("DTSTART").getValue()));
            LocalDateTime endd = dateFormat(String.valueOf(component.getProperty("DTEND").getValue()));
            boolean isSameEndTime =event.getEndDateTimeString().equals(String.valueOf(endd));
            boolean isSameStartTime =event.getEndDateTimeString().equals(String.valueOf(startd));
            boolean isSameName =event.getTitle().equals(String.valueOf(component.getProperty("SUMMARY").getValue()));
            if(isSameEndTime && isSameName && isSameStartTime){return true;}
        }

        return false;
    }

    public void icsToOurCall(File file, List<Event> list) throws ParserException, IOException {
        this.icsReader(file);
        Iterator var1 = this.icsCalendar.getComponents().iterator();

        while(var1.hasNext()) {
            Object object = var1.next();
            Component component = (Component) object;
            if(!isInEventList(list, component))
            {
                LocalDateTime startd = dateFormat(String.valueOf(component.getProperty("DTSTART").getValue()));
                LocalDateTime endd = dateFormat(String.valueOf(component.getProperty("DTEND").getValue()));
                Duration duration = Duration.between(startd, endd);
                boolean isFull = false;
                if (duration.toHours() >21 && duration.toHours() <25){isFull = true;}
                Event event = new Event(String.valueOf(component.getProperty("SUMMARY").getValue()), startd,endd, isFull);
                User user = User.getInstance();
                Database.addEvent(event, user.getUsername());
            }
        }
    }
}