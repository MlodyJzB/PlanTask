package com.app.ICSFiles;

/*
public class ICSFilesReader {
    private Calendar icsCalendar;
    private List<JSONObject> listOfEvents = new ArrayList();

    public ICSFilesReader() {
    }

    public void icsReader(String icsFilePath) throws IOException, ParserException {
        this.icsCalendar = (new CalendarBuilder()).build(new FileReader(icsFilePath));
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
}*/