package ICSFiles;

import java.io.*;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DtStamp;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.Calendar;

public class ICSFilesReader {
    private Calendar icsCalendar;
    private List<JSONObject> listOfEvents = new ArrayList<JSONObject>();


    public void icsReader(String icsFilePath) throws IOException, ParserException {
        icsCalendar = new CalendarBuilder().build(new FileReader(icsFilePath));

    }

    public void creatingJSONList() throws JSONException {
        for (final Object object : icsCalendar.getComponents()) {
            Component component = (Component) object;
            JSONObject jsonCalendar = new JSONObject();
            String event_name = String.valueOf(component.getProperty("SUMMARY").getValue());
            String start = String.valueOf(component.getProperty("DTSTART").getValue());
            String end = String.valueOf(component.getProperty("DTEND").getValue());
            String description = String.valueOf(component.getProperty("DESCRIPTION").getValue()) + String.valueOf(component.getProperty("LOCATION").getValue());
            String colour = "#F0FFF0";
            jsonCalendar.put("event_name", event_name);
            jsonCalendar.put("start", start);
            jsonCalendar.put("end", end);
            jsonCalendar.put("description", description);
            jsonCalendar.put("colour", colour);
            listOfEvents.add(jsonCalendar);



        }
    }

    public void icsToJSON(String icsFilePath, String jsonFilePath) throws ParserException, IOException, JSONException {
        icsReader(icsFilePath);
        creatingJSONList();
        Iterator<JSONObject> iter = listOfEvents.iterator();
        FileWriter file = new FileWriter(jsonFilePath);

        try {
            while (iter.hasNext())
            {
                JSONObject json = iter.next();
                file.write(json.toString());
                file.write("\n");
            }


        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            file.flush();
            file.close();
        }

    }

}
