package org.yottabase.yottaquake.db;

import org.bson.Document;
import org.json.JSONObject;

public abstract class AbstractDBFacade {

	public abstract void initializeCollectionEarthquake();

	public abstract void initializeCollectionCountries();

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
	
	public abstract void insertCountry(JSONObject event, String detail);
	
	public abstract Iterable<Document> getCountries(String levelQuality);
	
	public abstract Iterable<Document> getCountriesWithEventCount(String levelQuality);


}
