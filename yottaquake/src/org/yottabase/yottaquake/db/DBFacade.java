package org.yottabase.yottaquake.db;

import java.util.Set;

import org.bson.Document;
import org.json.JSONObject;
import org.yottabase.yottaquake.core.BoundingBox;
import org.yottabase.yottaquake.core.CountryDetailLevel;
import org.yottabase.yottaquake.core.EventFilter;
import org.yottabase.yottaquake.core.FlinnRegionDetailLevel;

public interface DBFacade {

	public void initializeCollectionEarthquake();

	public void initializeCollectionCountries();
	
	public void initializeCollectionFlinnRegions();

	public void initializeCollectionTectonicPlates();

	
	/**
	 * Chiude il database
	 */
	public void close();
	
	/**
	 * insert
	 */
	
	public void insertEvent(JSONObject event);
	
	public void insertTectonicPLates(JSONObject event);
	
	public void insertCountry(JSONObject event, CountryDetailLevel level);
	
	/**
	 * raggruppa per anno e mese 
	 */
	public Iterable<Document> countByYearMonth();

	public Iterable<Document> countByYear();
	
	public Iterable<Document> countByMonth();
	
	public Iterable<Document> countByMonthInYear(int year);
	
	/*
	 * nome delle flynn region
	 */
	public Iterable<Document> distinctRegion();
		
	public Iterable<Document> getCountries(CountryDetailLevel level, BoundingBox box );
	
	public Iterable<Document> getEventsInPolygon(Document geometry);
	
	public void insertFlinnRegion(JSONObject flinnRegion);
	
	public boolean updateDocument(Document document, Document update);
	
	//API 
	
	public Iterable<Document> getFlinnRegionsWithEventsCount(FlinnRegionDetailLevel level, BoundingBox box);
	
	public Iterable<Document> getEvents(BoundingBox box, EventFilter eventFilter);
	
	public Set<String> getDistinctMacroRegions(); 
	
	public Iterable<Document> getRegionsByMacroRegion(String macroRegions); 

	public Set<String> getDistinctRegionsAggregates();
	
	public Iterable<Document> getRegionsByAggregate(String aggregate);
	
	public Set<String> getDistinctContinents();
	
	public Iterable<Document> getCountriesByContinet(String continent, CountryDetailLevel level);

	public void getMagnitude();
	
	public void getDepth();

	public Integer getCountryEventsCount(String name, EventFilter eventFilter);

	public Iterable<Document> getTectonicPlates(BoundingBox box);

	public Integer PlatesEventsCount(String string, EventFilter eventFilter);

}
