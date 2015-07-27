package org.yottabase.yottaquake.db;

import org.bson.Document;
import org.json.JSONObject;
import org.yottabase.yottaquake.db.mongodb.CountryDetailLevel;

public abstract class AbstractDBFacade {

	public abstract void initializeCollectionEarthquake();

	public abstract void initializeCollectionCountries();
	
	public abstract void initializeCollectionFlinnRegions();
	
	/**
	 * Chiude il database
	 */
	public abstract void close();
	
	/**
	 * Conta il numero di di eventi
	 */
	public abstract long countEvents();
	
	/**
	 * raggruppa per anno e mese 
	 */
	public abstract Iterable<Document> countByYearMonth();
	
	
	/**
	 * i terremoti principali
	 */
	
	public abstract Iterable<Document> bigEarthQuake(int magnitude);

	public abstract Iterable<Document> countByYear();
	
	public abstract Iterable<Document> countByMonth();
	
	public abstract Iterable<Document> countByMonthInYear(int year);
	
	public abstract Iterable<Document> distinctRegion();

	public abstract void insertEvent(JSONObject event);
	
	public abstract void insertCountry(JSONObject event, CountryDetailLevel level);
		
	public abstract Iterable<Document> getCountries(CountryDetailLevel level);
	
	public abstract Iterable<Document> getCountriesWithEventCount(CountryDetailLevel level);

	public abstract Iterable<Document> getFlinnRegions();

	public abstract Iterable<Document> getEventsInPolygon(Document geometry);
	
	public abstract void insertFlinnRegion(JSONObject flinnRegion);
	
	public abstract boolean updateDocument(Document document, Document update);

}
