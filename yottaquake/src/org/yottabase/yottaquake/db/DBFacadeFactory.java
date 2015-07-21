package org.yottabase.yottaquake.db;

public interface DBFacadeFactory {

	/**
	 * Crea un oggetto che interagisce con uno specifico db
	 * @param properties
	 * @return
	 */
	public AbstractDBFacade createService(PropertyFile properties);
	
}
