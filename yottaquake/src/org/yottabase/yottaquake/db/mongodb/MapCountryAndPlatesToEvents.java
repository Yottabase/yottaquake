package org.yottabase.yottaquake.db.mongodb;

import java.io.FileNotFoundException;

import org.bson.Document;
import org.yottabase.yottaquake.core.CountryDetailLevel;
import org.yottabase.yottaquake.db.DBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;

public class MapCountryAndPlatesToEvents {
	
	public static void main(String[] args) throws FileNotFoundException {
		DBFacade facade = DBAdapterManager.getFacade();	
		
		mapCountryToEvent(facade);
//		mapPlatesToEvent(facade);
	}
	
	private static void mapPlatesToEvent(DBFacade facade){
		int count = 0;
		for (Document plates : facade.getTectonicPlates()) {
			Document properties = (Document) plates.get("properties");
			Document geometry = (Document) plates.get("geometry");
			System.out.println(plates.toJson());
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
				
				if( (count % 10000) == 0 )
					 System.out.println(count);
			}
			
		}
		
	}
	
	private static void mapCountryToEvent(DBFacade facade){
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
				
				if( (count % 10000) == 0 )
					 System.out.println(count);
			}
			
		}
	}
	
}
