package org.yottabase.yottaquake.db.mongodb;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import org.bson.Document;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.yottabase.yottaquake.db.AbstractDBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;
import org.yottabase.yottaquake.db.PropertyFile;

import diva.util.java2d.Polygon2D;

public class InsertEventMain {

	private static final String CONFIG_FILE_PATH = "db.properties";

	
	public static void main(String[] args) throws FileNotFoundException {
		
		AbstractDBFacade facade = getFacade();
		facade.initializeCollectionEarthquake();

		String path = args[0];
		InputStream inputStream = new FileInputStream(new File(path + "/dataset.json"));
		JSONTokener json = new JSONTokener(inputStream);
		int count = 0 ;
		while (json.more()) {
			count++;
			
			JSONObject event = (JSONObject) json.nextValue();

			event = convertDate(event);
			
			event = trimRegion(event);
			
			event = provideCountry(event, facade.getCountries("low"));
			
			facade.insertEvent(event);
			
			if(count % 1000 == 0)
				System.out.println(count);
			
		}
		
		
	}
	
	public static AbstractDBFacade getFacade(){
		PropertyFile propertyFile = new PropertyFile(DBAdapterManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH));
		DBAdapterManager adapterManager = new DBAdapterManager(propertyFile);
		return adapterManager.getAdapter();
	}
	
	
	public static JSONObject convertDate(JSONObject event) {
		
		JSONObject properties = (JSONObject) event.get("properties");
		
		String dateUTC = (String) properties.get("time");
												
		String[] split1 = dateUTC.split("-");
		
		String year = split1[0];
		
		String month = split1[1];
		
		String[] split2 = split1[2].split("T");
		
		String day = split2[0];
		
		String time = split2[1];
				
		String[] split3 = time.split(":");
		
		String hh = split3[0];
		
		String mm = split3[1];
		
//		String[] split4 = split3[2].split("\\.");
//				
//		String ss = split4[0];
//		System.out.println("year = " +year + "month " + month + "day " + day + "hh " + hh + "mm " + mm + "ss " +ss );
		
		int minutes = Integer.parseInt(hh) * 60 + Integer.parseInt(mm);
		String minutesTime = Integer.toString(minutes);
		
		event.put("year",year);
		event.put("month",month);
		event.put("day",day);
		event.put("time",minutesTime);

								
		return event;		
	
		
	}

	public static JSONObject trimRegion(JSONObject event) {
		
		JSONObject properties = (JSONObject) event.get("properties");
		
		Object region = properties.get("flynn_region");
		if(region != null){
			String regionTrim = region.toString().trim();
			properties.put("flynn_region", regionTrim);
		}
		return event;

		
	}
	
	@SuppressWarnings({ "unchecked" })
	private static JSONObject provideCountry(JSONObject event, Iterable<Document> countries) {
		JSONObject prop = (JSONObject) event.get("properties");
		double lon = Double.parseDouble( prop.get("lon").toString() );
		double lat = Double.parseDouble( prop.get("lat").toString() );
		Point2D point = new Point2D.Double(lon, lat);
		
		for (Document country : countries) {
			Document properties = (Document) country.get("properties");
			Document geometry = (Document) country.get("geometry");
			String geom_type = geometry.getString("type");
			
			if (geom_type.equals("Polygon")) {
				ArrayList<ArrayList<String>> poly = ((ArrayList<ArrayList<ArrayList<String>>>) geometry.get("coordinates")).get(0);
				
				double[] flatten_coords = coordsList2FlattenArray(poly);
				Polygon2D polygon = new Polygon2D.Double(flatten_coords);
				
				if (polygon.contains(point)) {
					String country_name = properties.getString("name");
					String country_code = properties.getString("iso_a3");
					String continent = properties.getString("continent");
					
					JSONObject geolocation = new JSONObject();
					geolocation.put("name", country_name);
					geolocation.put("iso_a3", country_code);
					geolocation.put("continent", continent);
					
					event.put("geolocation", geolocation);
					return event;
				}
				
			} 
			else {	
				ArrayList<ArrayList<ArrayList<ArrayList<String>>>> multiPoly = (ArrayList<ArrayList<ArrayList<ArrayList<String>>>>) geometry.get("coordinates");
				for (int i = 0; i < multiPoly.size(); i++) {
					ArrayList<ArrayList<ArrayList<String>>> c = multiPoly.get(i);
					ArrayList<ArrayList<String>> poly = c.get(0);
					
					double[] flatten_coords = coordsList2FlattenArray(poly);
					Polygon2D polygon = new Polygon2D.Double(flatten_coords);
					
					if (polygon.contains(point)) {
						String country_name = properties.getString("name");
						String country_code = properties.getString("iso_a3");
						String continent = properties.getString("continent");
						
						JSONObject geolocation = new JSONObject();
						geolocation.put("name", country_name);
						geolocation.put("iso_a3", country_code);
						geolocation.put("continent", continent);
						
						event.put("geolocation", geolocation);
						return event;
					}
				}
			}
			
		}
		
		return event;
	}
	
	
	private static double[] coordsList2FlattenArray(ArrayList<ArrayList<String>> coordsList) {
		double[] flatten_array = new double[2 * coordsList.size()];
		
		for (int j = 0; j < coordsList.size(); j++) {				
		    ArrayList<String> coords = coordsList.get(j);
		    Object x = coords.get(0);
		    Object y = coords.get(1);
		    
		    flatten_array[j*2] = Double.parseDouble(x.toString());
		    flatten_array[(j*2)+1] = Double.parseDouble(y.toString());	
		}
		
		return flatten_array;
	}

}
