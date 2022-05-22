package com.app.ICSFiles;


import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.validate.ValidationException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ToICSFileWritter {
    private List<JSONObject> listOfEvents = new ArrayList<JSONObject>();


    public void getJSONFile(String JSONFilepath) throws IOException, JSONException {
        FileReader fileReader = new FileReader(JSONFilepath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String newLine = null;
        while((newLine = bufferedReader.readLine()) !=null ){
            JSONObject jsonObject = new JSONObject(newLine);
            listOfEvents.add(jsonObject);
            //System.out.println(jsonObject.get("event_name"));
        }


    }

    public void JSONToICS (String JSONFilepath, String ICSFilePath) throws JSONException, IOException, ParseException, ValidationException {
        getJSONFile(JSONFilepath);
        Iterator<JSONObject> iter = listOfEvents.iterator();
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//planTask//PL"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        while (iter.hasNext())
        {
            JSONObject json = iter.next();
            VEvent event = new VEvent();
            PropertyList eventProps = event.getProperties();
            java.time.ZonedDateTime now = java.time.ZonedDateTime.now();
            String uidTimestamp = java.time.format.DateTimeFormatter
                    .ofPattern("uuuuMMdd'T'hhmmssXX")
                    .format(now);
            String uidSequence = "/" + (int) Math.ceil(Math.random() * 1000);
            String uidDomain = "@notour.domain.com";
            eventProps.add(new Uid(uidTimestamp + uidSequence + uidDomain));
            eventProps.add(new DtStart(String.valueOf(json.get("start"))));
            eventProps.add(new DtEnd(String.valueOf(json.get("end"))));
            eventProps.add(new Summary(String.valueOf(json.get("event_name"))));
            eventProps.add(new Description(String.valueOf(json.get("description"))));
            calendar.getComponents().add(event);
        }
        FileOutputStream fout = new FileOutputStream(ICSFilePath);

        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(calendar, fout);
    }
}
