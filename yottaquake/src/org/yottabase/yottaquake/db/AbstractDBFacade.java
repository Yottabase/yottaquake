package org.yottabase.yottaquake.db;

import org.bson.Document;

import com.mongodb.client.AggregateIterable;

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
	 * 
	 */
	public abstract Iterable<Document> countByYearMonth();
}
