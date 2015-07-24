package org.yottabase.yottaquake.db.mongodb;

import java.io.FileNotFoundException;
import java.util.Iterator;

import org.bson.Document;
import org.yottabase.yottaquake.db.AbstractDBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;

public class MapCountryToEvents {
	
	public static void main(String[] args) throws FileNotFoundException {
		AbstractDBFacade facade = DBAdapterManager.getFacade();	
		
		for (Document country : facade.getCountries("low")) {
			Document properties = (Document) country.get("properties");
			Document geometry = (Document) country.get("geometry");
			System.out.println(country.get("_id").toString());
			Iterable<Document> events = DBAdapterManager.getFacade().getEventsInPolygon(geometry);
//			System.out.println(country.get("_id").toString());

			for (Document event : events) {
//				System.out.println(event.toJson());
				String country_name = properties.getString("NAME");
				String country_code = properties.getString("ISO_A3");
				String continent = properties.getString("CONTINENT");
				
				Document geo_values = new Document();
				geo_values.append("name", country_name);
				geo_values.append("iso_a3", country_code);
				geo_values.append("continent", continent);
				
				Document geolocation = new Document();
				geolocation.append("geolocation", geo_values);
				
				facade.updateDocument(event, geolocation);
			}
			
//			Document event = null;
//			try {
//				Iterator<Document> iter = events.iterator();
//				while (iter.hasNext()) {
//					event = iter.next();
//					
//					String country_name = properties.getString("NAME");
//					String country_code = properties.getString("ISO_A3");
//					String continent = properties.getString("CONTINENT");
//					
//					Document geo_values = new Document();
//					geo_values.append("name", country_name);
//					geo_values.append("iso_a3", country_code);
//					geo_values.append("continent", continent);
//					
//					Document geolocation = new Document();
//					geolocation.append("geolocation", geo_values);
//					
//					facade.updateDocument(event, geolocation);
//				}
//			} catch (Exception e) {
//				System.out.println(e.getMessage());
//				System.out.println( event.toJson() );
//			}
		}
	}
	
}
