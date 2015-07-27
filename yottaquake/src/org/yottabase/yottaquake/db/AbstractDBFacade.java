package org.yottabase.yottaquake.db;

import java.util.Date;
import java.util.Set;

import org.bson.Document;
import org.json.JSONObject;
import org.yottabase.yottaquake.core.BoundingBox;
import org.yottabase.yottaquake.core.CountryDetailLevel;
import org.yottabase.yottaquake.core.FlinnRegionDetailLevel;

public abstract class AbstractDBFacade {

	public abstract void initializeCollectionEarthquake();

	public abstract void initializeCollectionCountries();
	
	public abstract void initializeCollectionFlinnRegions();
	
	/**
	 * Chiude il database
	 */
	public abstract void close();
	
	/**
	 * insert
	 */
	
	public abstract void insertEvent(JSONObject event);
	
	public abstract void insertCountry(JSONObject event, CountryDetailLevel level);
	
	/**
	 * raggruppa per anno e mese 
	 */
	public abstract Iterable<Document> countByYearMonth();

	public abstract Iterable<Document> countByYear();
	
	public abstract Iterable<Document> countByMonth();
	
	public abstract Iterable<Document> countByMonthInYear(int year);
	
	/*
	 * nome delle flynn region
	 */
	public abstract Iterable<Document> distinctRegion();
		
	public abstract Iterable<Document> getCountries(CountryDetailLevel level );
	
	public abstract Iterable<Document> getEventsInPolygon(Document geometry);
	
	public abstract void insertFlinnRegion(JSONObject flinnRegion);
	
	public abstract boolean updateDocument(Document document, Document update);
	
	//API 
	
	public abstract Iterable<Document> getCountriesWithEventsCount(CountryDetailLevel level, BoundingBox box);

	public abstract Iterable<Document> getFlinnRegionsWithEventsCount(FlinnRegionDetailLevel level, BoundingBox box);
	
	public abstract Iterable<Document> getEvents(
		BoundingBox box, 
		Date from, Date to, 
		Integer minMagnitude, Integer maxMagnitude, 
		Integer minDepth, Integer maxDepth
	);
	
	public abstract Set<String> getDistinctMacroRegions(); 
	
	public abstract Iterable<Document> getRegionsByMacroRegion(String macroRegions); 

	

}
