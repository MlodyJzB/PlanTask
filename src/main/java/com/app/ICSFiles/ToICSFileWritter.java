package com.app.ICSFiles;


import com.app.app.Event;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.validate.ValidationException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
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

    public void CalendarToICS(File file, List<Event> list) throws ParseException, IOException {
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//planTask//PL"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        for (var calendarEvent:list)
        {
            VEvent events = new VEvent();
            PropertyList eventProps = events.getProperties();
            java.time.ZonedDateTime now = java.time.ZonedDateTime.now();
            String uidTimestamp = java.time.format.DateTimeFormatter
                    .ofPattern("uuuuMMdd'T'hhmmssXX")
                    .format(now);
            String uidSequence = "/" + (int) Math.ceil(Math.random() * 1000);
            String uidDomain = "@notour.domain.com";
            eventProps.add(new Uid(uidTimestamp + uidSequence + uidDomain));
            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyyMMdd");
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HHmmss");
            if (calendarEvent.isFullDay())
            {
                eventProps.add(new DtStart(formatterDate.format(calendarEvent.getStartDateTime()) + "T" + "000001"));
                eventProps.add(new DtEnd(formatterDate.format(calendarEvent.getEndDateTime()) + "T" + "235900"));
            }
            else {
                eventProps.add(new DtStart(formatterDate.format(calendarEvent.getStartDateTime()) + "T" + formatterTime.format(calendarEvent.getStartDateTime())));
                eventProps.add(new DtEnd(formatterDate.format(calendarEvent.getEndDateTime()) + "T" + formatterTime.format(calendarEvent.getEndDateTime())));
            }
            eventProps.add(new Summary(calendarEvent.getTitle()));
            //System.out.println(calendarEvent.toEntry().getZoneId().getId());
            TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
            TimeZone timezone = registry.getTimeZone(calendarEvent.toEntry().getZoneId().getId());
            VTimeZone tz = timezone.getVTimeZone();
            events.getProperties().add(tz.getTimeZoneId());
            calendar.getComponents().add(events);

        }
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(calendar,new FileOutputStream(file));
    }
}
