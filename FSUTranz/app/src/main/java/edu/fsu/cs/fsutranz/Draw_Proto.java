package edu.fsu.cs.fsutranz;

/* garage names returned as List<String>
   garage values returned as List<List<String>>*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Draw_Proto {

    private String link = "http://198.100.154.139";
    private String jsonString;
    private List<String> names = new ArrayList<>();
    private List<List<String>> values = new ArrayList<>();

    Draw_Proto(){
        getNamesAndValues();
    }

    public List<String> getNames(){ return names; }
    public List<List<String>> getValues(){ return values; }

    public void getNamesAndValues() {
        //refresh data
        refreshJSON(link);
        JSONObject json = null;
        JSONArray garageNames = null;
        try {
            json = new JSONObject(jsonString);
            garageNames = (JSONArray) json.names();
            for (int i = 0; i < garageNames.length(); i++) {
            }
            for (int i = 0; i < garageNames.length(); i++) {
                String data = json.get(garageNames.getString(i)).toString();
                String name = garageNames.getString(i);
                JSONArray dataArr = new JSONArray(data);
                //Each list in values
                List<String> dataList = new ArrayList<>();

                //Tidy up data to send
                for (int j = 0; j < dataArr.length(); j++) {
                    String obj = dataArr.get(j).toString();
                    obj = obj.replace('[', ' ');
                    obj = obj.replace(']', ' ');
                    obj = obj.replaceAll(" ", "");
                    obj = obj.replaceAll("\"", "");
                    dataList.add(obj);
                }
                values.add(dataList);
                names.add(name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //convert the generic occupancy information to an rgb code or /we
    public String convert_color(String s) {
        if (s == "low") {
            return "blue";
        } else if (s == "med") {
            return "green";
        } else if (s == "high") {
            return "orange";
        } else if (s == "max") {
            return "red";
        } else {
            return "UNHANDLED STATUS: " + s;
        }
    }
    //refresh data
    private void refreshJSON(String link){

        URL url;
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try {
            url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            InputStream stream = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            String buffer = "";
            String line = "";
            while ((line = reader.readLine()) != null){
                buffer += line;
            }

            this.jsonString = buffer;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }

}
