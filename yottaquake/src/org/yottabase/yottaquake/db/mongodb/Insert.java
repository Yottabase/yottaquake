package org.yottabase.yottaquake.db.mongodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.yottabase.yottaquake.core.CountryDetailLevel;
import org.yottabase.yottaquake.db.DBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;

public class Insert {

	private static final Map<CountryDetailLevel, String> detail2fileName;
	
	static {
		detail2fileName = new HashMap<CountryDetailLevel, String>();
			detail2fileName.put(CountryDetailLevel.HIGH, "country_high");
			detail2fileName.put(CountryDetailLevel.MEDIUM, "country_medium");
			detail2fileName.put(CountryDetailLevel.LOW, "country_low");
	}

	public static void main(String[] args) throws FileNotFoundException, ParseException {
		DBFacade facade = DBAdapterManager.getFacade();
		
		String path = args[0];
		
		/*
		 * inserisci eventi
		 */
		InputStream inputStreamEarthquake = new FileInputStream(new File(path + "/earthquakes.json"));
		insertEarthquake(facade,inputStreamEarthquake);

		/*
		 * inserisci country
		 */
		facade.initializeCollectionCountries();

		for (CountryDetailLevel level : detail2fileName.keySet()) {
			String filePath = args[0] + "/" + detail2fileName.get(level) + ".json";
			InputStream inputStreamCountry = new FileInputStream(new File(filePath));
			System.out.println(filePath);

			insertCountry(facade,inputStreamCountry,level);			
		}
		
		/*
		 * inserisci le placche tettoniche
		 */
		InputStream inputStreamTectonic = new FileInputStream(new File(path + "/tectonic_plates.json"));
		insertTectonicPlates(facade,inputStreamTectonic);
		
		/*
		 * map caountry e placche
		 */
		
		mapCountryToEvent(facade);
		mapPlatesToEvent(facade);

	}
	
	
	private static JSONObject convertDate(JSONObject event) throws ParseException {
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
		
		int minutes = Integer.parseInt(hh) * 60 + Integer.parseInt(mm);
		String minutesTime = Integer.toString(minutes);
		
		Document timeInformation = new Document();
		timeInformation.append("year", year);
		timeInformation.append("month", month);
		timeInformation.append("day", day);
		timeInformation.append("hour", minutesTime);
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
		Date from = format.parse(dateUTC);
		
		timeInformation.append("milliseconds", from.getTime());
			
		event.put("time", timeInformation);
	
		return event;		
	}

	
	private static JSONObject trimRegion(JSONObject event) {
		JSONObject properties = (JSONObject) event.get("properties");
		
		Object region = properties.get("flynn_region");
		if(region != null){
			String regionTrim = region.toString().trim();
			properties.put("flynn_region", regionTrim);
		}
		
		return event;
	}

	
	private static void insertEarthquake(DBFacade facade, InputStream inputStream) throws ParseException{
		facade.initializeCollectionEarthquake();
		JSONTokener json = new JSONTokener(inputStream);
		
		int count = 0 ;
		while (json.more()) {
			count++;
			
			JSONObject event = (JSONObject) json.nextValue();

			event = convertDate(event);
			
			event = trimRegion(event);
			
			facade.insertEvent(event);
			
			if(count % 100000 == 0)
				System.out.println(count);
			
		}
		System.out.println("INSERTED EVENTS");

	}
	
	
	private static void insertCountry(DBFacade facade, InputStream inputStream, CountryDetailLevel level){
			
		JSONTokener json = new JSONTokener(inputStream);
		JSONObject countries = (JSONObject) json.nextValue();
		JSONArray country = (JSONArray) countries.get("features");
			
		int count = 0 ;
		for (int i = 0; i < country.length(); i++) {
			count++;
			
			facade.insertCountry((JSONObject) country.get(i), level);
		
			if( (count % 100) == 0 )
				System.out.println(count);
		}
		System.out.println("INSERTED COUNTRIES");

	}
	
	
	private static void insertTectonicPlates(DBFacade facade, InputStream inputStream){
		facade.initializeCollectionTectonicPlates();
		JSONTokener json = new JSONTokener(inputStream);
		
		int count = 0 ;
		while (json.more()) {
			count++;
			
			JSONObject event = (JSONObject) json.nextValue();
			
			facade.insertTectonicPLates(event);
			
			if(count % 100 == 0)
				System.out.println(count);
			
		}
		System.out.println("INSERTED TECTONIC PLATES");
		
	}
	

	private static void mapPlatesToEvent(DBFacade facade){
		System.out.println("START MAP TECTONIC PLATES TO EVENT");

		int count = 0;
		for (Document plates : facade.getTectonicPlates(null)) {
			Document properties = (Document) plates.get("properties");
			Document geometry = (Document) plates.get("geometry");
			Iterable<Document> events = facade.getEventsInPolygon(geometry);
			
			for (Document event : events) {
				count ++;
				String plateName = properties.getString("PlateName");
				String layer = properties.getString("LAYER");
				String code = properties.getString("Code");
				
				Document plate_values = new Document();
				plate_values.append("PlateName", plateName);
				plate_values.append("LAYER", layer);
				plate_values.append("Code", code);
				
				Document platelocation = new Document();
				platelocation.append("plate_location", plate_values);
				
				facade.updateDocument(event, platelocation);
				
				if( (count % 100000) == 0 )
					 System.out.println("map plates: " + count);
			}
		}
		System.out.println("COMPLETED MAP TECTONIC PLATES TO EVENT");

	}
	
	
	private static void mapCountryToEvent(DBFacade facade){
		System.out.println("START MAP COUNTRIES TO EVENT");

		int count = 0;
		for (Document country : facade.getCountries(CountryDetailLevel.HIGH,null)) {
			Document properties = (Document) country.get("properties");
			Document geometry = (Document) country.get("geometry");
			Iterable<Document> events = DBAdapterManager.getFacade().getEventsInPolygon(geometry);
			
			for (Document event : events) {
				count ++;
				String country_name = properties.getString("name");
				String country_code = properties.getString("iso_a3");
				String continent = properties.getString("continent");
				
				Document geo_values = new Document();
				geo_values.append("name", country_name);
				geo_values.append("iso_a3", country_code);
				geo_values.append("continent", continent);
				
				Document geolocation = new Document();
				geolocation.append("geolocation", geo_values);
				
				facade.updateDocument(event, geolocation);
				
				if( (count % 100000) == 0 )
					 System.out.println( "map country: " +count);
			}
		}
		System.out.println("COMPLETED MAP COUNTRIES TO EVENT");
	}	
	
	
}
