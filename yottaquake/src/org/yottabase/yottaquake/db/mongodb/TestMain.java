package org.yottabase.yottaquake.db.mongodb;

import org.yottabase.yottaquake.db.AbstractDBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;
import org.yottabase.yottaquake.db.PropertyFile;

public class TestMain {

	private static final String CONFIG_FILE_PATH = "db.properties";
	
	public static void main(String[] args) {
		
		AbstractDBFacade facade = getFacade();
		
		System.out.println(facade.countEvents());
		
	}
	
	public static AbstractDBFacade getFacade(){
		PropertyFile propertyFile = new PropertyFile(DBAdapterManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH));
		DBAdapterManager adapterManager = new DBAdapterManager(propertyFile);
		return adapterManager.getAdapter();
	}
	
}
