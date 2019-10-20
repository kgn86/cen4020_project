// Bus_Routes class 
// Uses org.json package to parse JSON Strings

package bus_routes;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Bus_Route {

	private String arrivals_url = "https://api.transloc.com/feeds/3/vehicle_statuses?include_arrivals=true&agencies=649&routes=";
	private	String stops_url = "https://api.transloc.com/feeds/3/stops?include_routes=true&agencies=649&routes=";
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
	
	public void GetStops(){

		//Connecting to Transloc API	
		URI uri;
		try {
			uri = new URI(this.stops_url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}
		HttpClient client = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder(uri).build();
			
		//Getting the response
		HttpResponse<String> response;
		try {
			response = client.send(request, BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return;
		}
		
		//full body of response
		String jsonString = response.body();
		JSONObject json = null;
		try {
			json = new JSONObject(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//JSON array of stops
		JSONArray stops = (JSONArray) json.get("stops");
		JSONObject stop;
		
		//List of stop names/IDs
		List<String> s_names = new ArrayList();
		List<Integer> s_IDs = new ArrayList();

		//store names/IDs
		for (int i = 0; i < stops.length(); i++) {
			stop = (JSONObject) stops.get(i);
			String sN = (String) stop.get("name");
			Integer sID = (Integer) stop.get("id");
			s_names.add(sN);
			s_IDs.add(sID);
		}
		this.stopNames = s_names;
		this.stopIDs = s_IDs;
	}
	
	public void GetArrivals() {
		
		//Connecting to Transloc API	
		URI uri;
		try {
			uri = new URI(this.arrivals_url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}
		HttpClient client = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder(uri).build();
					
		//Getting the response
		HttpResponse<String> response;
		try {
			response = client.send(request, BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return;
		}
		
		//full body of response
		String jsonString = response.body();
		JSONObject json = null;
		try {
			json = new JSONObject(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//JSON array of arrivals
		JSONArray arrivals = (JSONArray) json.get("arrivals");
		JSONObject arrival;
			
		//List of arrival timestamps/IDs
		List<Integer> timestamps = new ArrayList();
		List<Integer> a_IDs = new ArrayList();
		
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
		//assign member data
		this.arrivalTimestamps = timestamps;
		this.arrivalIDs = a_IDs;
		
	}
	
	public List<String> GetStopNames(){
		return this.stopNames;
	}
		
}

