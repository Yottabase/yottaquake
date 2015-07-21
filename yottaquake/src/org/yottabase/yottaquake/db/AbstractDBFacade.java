package org.yottabase.yottaquake.db;

public abstract class AbstractDBFacade {
		
	/**
	 * Chiude il database
	 */
	public abstract void close();
	
	/**
	 * Conta il numero di di eventi
	 */
	public abstract long countEvents();
	
}
