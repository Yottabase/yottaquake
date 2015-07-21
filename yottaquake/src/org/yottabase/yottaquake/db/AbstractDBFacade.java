package org.yottabase.yottaquake.db;

import org.bson.Document;

public abstract class AbstractDBFacade {
		
	/**
	 * Chiude il database
	 */
	public abstract void close();
	
	/**
	 * Conta il numero di di eventi
	 */
	public abstract long countEvents();

	
	/**
	 * converti le date da UTC
	 */
	public abstract void convertDate();
	
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

}
