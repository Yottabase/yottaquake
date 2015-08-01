package org.yottabase.yottaquake.db;

import java.io.InputStream;

import org.yottabase.yottaquake.db.mongodb.MongoDBAdapterFactory;

public class DBAdapterManager {
	
	private static DBFacade instance = null;
	
	private static final String CONFIG_FILE_PATH = "db.properties";
	
	
	private DBAdapterManager() {
		
	}
	
	
	public static synchronized DBFacade getFacade() {
		if (instance == null) {
			InputStream is = DBAdapterManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH);
			PropertyFile properties = new PropertyFile(is);
			
			DBFacadeFactory adapterFactory = new MongoDBAdapterFactory();
			instance = adapterFactory.createService(properties);
		}
		
		return instance;
	}

}
