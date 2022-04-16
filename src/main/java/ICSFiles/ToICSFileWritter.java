package ICSFiles;

import java.io.*;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.property.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.*;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.component.VEvent;

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
            eventProps.add(new DtStart(new DateTime(String.valueOf(json.get("start")))));
            eventProps.add(new DtEnd(new DateTime(String.valueOf(json.get("end")))));
            eventProps.add(new Summary(String.valueOf(json.get("event_name"))));
            eventProps.add(new Description(String.valueOf(json.get("description"))));
            calendar.getComponents().add(event);
        }
        FileOutputStream fout = new FileOutputStream(ICSFilePath);

        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(calendar, fout);
    }
}
