// Bus_Routes class 
// Uses org.json package to parse JSON Strings

package edu.fsu.cs.fsutranz;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

import java.nio.Buffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Bus_Route {

	private String arrivals_url = "https://api.transloc.com/feeds/3/vehicle_statuses?include_arrivals=true&agencies=649&routes=";
	private	String stops_url = "https://api.transloc.com/feeds/3/stops?include_routes=true&agencies=649&routes=";
	private String fullJSON;
	private List<String> stopNames = new ArrayList<String>();
	private List<Integer> stopIDs = new ArrayList<Integer>();
	private List<Integer> arrivalTimestamps = new ArrayList<Integer>();
	private List<Integer> arrivalIDs = new ArrayList<Integer>();
	
	public Bus_Route(int routeID) {
		
		//Finish URLs
		String a_url_end = "&schedules=true&include_out_of_service=true";
		this.arrivals_url += Integer.toString(routeID);
		this.arrivals_url += a_url_end;
		this.stops_url += Integer.toString(routeID);

	}
	
	//Finds predicted time given the name of a stop
	//Returns -1 if stop name is not found in list of stops
	public double GetPredictedTime(String stopName) {
		this.GetArrivals();
		this.GetStops();

		int i = stopNames.indexOf(stopName);
		if(i == -1) {
			System.out.println("Stop name: " + stopName + " not found...");
			return -1;
		}
		int stopID = stopIDs.get(i);
		i = arrivalIDs.indexOf(stopID);
		long timestamp = arrivalTimestamps.get(i);


 		long time = System.currentTimeMillis() / 1000;
 		
 		double predTime = (double)timestamp - time;
 		predTime = predTime / 60;
 		
 		DecimalFormat df = new DecimalFormat("0.00");
 		df.format(predTime);
 		
		return predTime;
	}

	private void refreshJSON(String link){
		//Connecting to Transloc API
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

			this.fullJSON = buffer;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}
	}
	
	public void GetStops(){

		refreshJSON(this.stops_url);
		//full body of response
		JSONObject json = null;
		List<String> s_names = null;
		List<Integer> s_IDs = null;
		try {
			json = new JSONObject(this.fullJSON);
			//JSON array of stops
			JSONArray stops = (JSONArray) json.get("stops");
			JSONObject stop;

			//List of stop names/IDs
			s_names = new ArrayList();
			s_IDs = new ArrayList();

			//store names/IDs
			for (int i = 0; i < stops.length(); i++) {
				stop = (JSONObject) stops.get(i);
				String sN = (String) stop.get("name");
				Integer sID = (Integer) stop.get("id");
				s_names.add(sN);
				s_IDs.add(sID);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		s_names.add("");
		s_IDs.add(0);
		this.stopNames = s_names;
		this.stopIDs = s_IDs;
	}
	
	public void GetArrivals() {
		
		refreshJSON(this.arrivals_url);


		//full body of response
		JSONObject json = null;
		List<Integer> timestamps = null;
		List<Integer> a_IDs = null;

		try {
			json = new JSONObject(this.fullJSON);
			//JSON array of arrivals
			JSONArray arrivals = (JSONArray) json.get("arrivals");
			JSONObject arrival;

			//List of arrival timestamps/IDs
			timestamps = new ArrayList();
			a_IDs = new ArrayList();

			//arrival names/IDs
			for (int i = 0; i < arrivals.length(); i++) {
				arrival = (JSONObject) arrivals.get(i);
				Integer sID = (Integer) arrival.get("stop_id");
				Integer tS = (Integer) arrival.get("timestamp");
				if(arrival.get("vehicle_id") != null && !a_IDs.contains(sID)) {
					timestamps.add(tS);
					a_IDs.add(sID);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		//assign member data
		this.arrivalTimestamps = timestamps;
		this.arrivalIDs = a_IDs;
		
	}
	
	public List<String> GetStopNames(){
		return this.stopNames;
	}
		
}

