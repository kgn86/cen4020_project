// Bus_Routes class 
// Uses org.json package to parse JSON Strings

package edu.fsu.cs.fsutranz;
import android.util.Log;

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
	private String[] stopNames = null;
	private double[] predTimes = null;
	private List<Integer> arrivalTimestamps;
	private List<Integer> stopIDs;
	private List<Integer> arrivalIDs;

	public Bus_Route(int routeID) {

		//Finish URLs
		String a_url_end = "&schedules=true&include_out_of_service=true";
		this.arrivals_url += Integer.toString(routeID);
		this.arrivals_url += a_url_end;
		this.stops_url += Integer.toString(routeID);

	}

	//Finds predicted time given the name of a stop
	public void PredictedTimes() {
		this.GetArrivals();
		this.GetStops();
		this.predTimes = new double[this.stopNames.length];
		Log.d("STOPS_LENGTH", Integer.toString(this.stopNames.length));
		Log.d("PREDTIMES_LENGTH", Integer.toString(this.predTimes.length));
		if(arrivalTimestamps.size() > 0) {
			for (int i = 0; i < this.stopNames.length; i++) {
				long timestamp = arrivalTimestamps.get(i);
				long currTime = System.currentTimeMillis() / 1000;
				double predTime = (double) timestamp - (double) currTime;
				predTime = predTime / 60;
				this.predTimes[i] = predTime;
			}
		}
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
		List<String> s_names = new ArrayList<>();
		List<Integer> s_IDs = new ArrayList<>();
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
				if (!s_IDs.contains(sID)) {
					s_names.add(sN);
					s_IDs.add(sID);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		this.stopNames = new String[s_names.size()];

		// List<String> to String[]
		for (int i = 0; i < s_names.size(); i++){
			this.stopNames[i] = s_names.get(i);
		}
		this.stopIDs = s_IDs;
	}

	public void GetArrivals() {

		refreshJSON(this.arrivals_url);

		//full body of response
		JSONObject json = null;
		List<Integer> timestamps = new ArrayList<>();
		List<Integer> a_IDs = new ArrayList<>();

		try {
			json = new JSONObject(this.fullJSON);
			//JSON array of arrivals
			JSONArray arrivals = (JSONArray) json.get("arrivals");
			JSONObject arrival;

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

		this.arrivalTimestamps = timestamps;
		this.arrivalIDs = a_IDs;

	}

	public double[] GetPredTimes(){ return this.predTimes; }

	public String[] GetStopNames(){ return this.stopNames; }

}

