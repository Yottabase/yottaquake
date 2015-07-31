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
		insertEarthquakes(facade,inputStreamEarthquake);

		/*
		 * inserisci country
		 */
		facade.initializeCountriesCollection();
		for (CountryDetailLevel level : detail2fileName.keySet()) {
			String filePath = args[0] + "/" + detail2fileName.get(level) + ".json";
			InputStream inputStreamCountry = new FileInputStream(new File(filePath));
			System.out.println(filePath);

			insertCountry(facade, inputStreamCountry, level);			
		}
		
		/*
		 * Insert continents
		 */
		File continentFile = new File(path + "/continents.json");
		InputStream continentsIS = new FileInputStream( continentFile );
		insertContinents(facade, continentsIS);
		
		/*
		 * Insert flinn regions
		 */
		File flinnRegionsFile = new File(path + "/flinn_regions.json");
		InputStream flinnRegionsIS = new FileInputStream( flinnRegionsFile );
		insertFlinnRegions(facade, flinnRegionsIS);
		
		/*
		 * Insert tectonic plates
		 */
		InputStream inputStreamTectonic = new FileInputStream(new File(path + "/tectonic_plates.json"));
		insertTectonicPlates(facade,inputStreamTectonic);
		
		/*
		 * Map countries, continents and plates to events
		 */
		mapCountriesToEvents(facade);
		mapContinentsToEvents(facade);
		mapFlinnRegionsToEvents(facade);
		mapTectonicPlatesToEvents(facade);

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

	
	private static void insertEarthquakes(DBFacade facade, InputStream inputStream) throws ParseException{
		facade.initializeEarthquakesCollection();
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
		System.out.println("EVENTS INSERTED");

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
		System.out.println("COUNTRIES INSERTED");

	}
	
	
	private static void insertContinents(DBFacade facade, InputStream inputStream) {
		facade.initializeContinentsCollection();
		JSONTokener json = new JSONTokener(inputStream);
		
		int count = 0 ;
		while (json.more()) {
			count++;
			
			JSONObject continent = (JSONObject) json.nextValue();
			
			facade.insertContinent(continent);
			
			if(count % 100 == 0)
				System.out.println(count);
			
		}
		System.out.println("CONTINENTS INSERTED");
		
	}
	
	
	private static void insertFlinnRegions(DBFacade facade, InputStream inputStream) {
		facade.initializeFlinnRegionsCollection();
		JSONTokener json = new JSONTokener(inputStream);
		
		int count = 0 ;
		while (json.more()) {
			count++;
			
			JSONObject flinnRegion = (JSONObject) json.nextValue();
			
			facade.insertFlinnRegion(flinnRegion);
			
			if(count % 100 == 0)
				System.out.println(count);
			
		}
		System.out.println("FLINN REGIONS INSERTED");
		
	}
	
	
	private static void insertTectonicPlates(DBFacade facade, InputStream inputStream) {
		facade.initializeTectonicPlatesCollection();
		JSONTokener json = new JSONTokener(inputStream);
		
		int count = 0 ;
		while (json.more()) {
			count++;
			
			JSONObject tectonicPlate = (JSONObject) json.nextValue();
			
			facade.insertTectonicPlate(tectonicPlate);
			
			if(count % 100 == 0)
				System.out.println(count);
			
		}
		System.out.println("TECTONIC PLATES INSERTED");
		
	}
	
	
	private static void mapCountriesToEvents(DBFacade facade){
		System.out.println("BEGINNING MAPPING OF COUNTRIES TO EVENTS");

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
					 System.out.println( "map country: " + count);
			}
		}
		System.out.println("COMPLETED MAP COUNTRIES TO EVENT");
	}
	
	
	private static void mapContinentsToEvents(DBFacade facade) {
		System.out.println("BEGINNING MAPPING OF CONTINENTS TO EVENTS");

		int count = 0;
		for (Document continent : facade.getContinents()) {
			Document properties = (Document) continent.get("properties");
			Document geometry = (Document) continent.get("geometry");
			Iterable<Document> eventsInContinent = facade.getEventsInPolygon(geometry);
			
			for (Document event : eventsInContinent) {
				count ++;
				
				String name = properties.getString("CONTINENT");
				
				Document newValue = new Document();
				newValue.append("CONTINENT", name);
				
				facade.updateDocument(event, newValue);
				
				if( (count % 100000) == 0 )
					 System.out.println("\tEvents mapped: " + count);
			}
		}
		System.out.println("MAPPING OF CONTINENTS EVENTS COMPLETED");

	}
	
	
	private static void mapFlinnRegionsToEvents(DBFacade facade) {
		System.out.println("BEGINNING MAPPING OF FLINN REGIONS TO EVENTS");

		int count = 0;
		for (Document region : facade.getFlinnRegions(null)) {
			Document properties = (Document) region.get("properties");
			Document geometry = (Document) region.get("geometry");
			Iterable<Document> eventsInRegion = facade.getEventsInPolygon(geometry);
			
			for (Document event : eventsInRegion) {
				count ++;
				
				String name_l = properties.getString("name_l");
				
				Document regionValues = new Document();
				regionValues.append("name_l", name_l);
				
				Document newValue = new Document();
				newValue.append("flinnRegion", regionValues);
				
				facade.updateDocument(event, newValue);
				
				if( (count % 100000) == 0 )
					 System.out.println("\tEvents mapped: " + count);
			}
		}
		System.out.println("MAPPING OF FLINN REGIONS TO EVENTS COMPLETED");
	}
	
	
	private static void mapTectonicPlatesToEvents(DBFacade facade){
		System.out.println("BEGINNING MAPPING OF TECTONIC PLATES TO EVENTS");

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
		System.out.println("MAPPING OF TECTONIC PLATES TO EVENTS COMPLETED");

	}	
	
}
