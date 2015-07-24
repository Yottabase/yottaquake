package org.yottabase.yottaquake.db;

import java.io.InputStream;

import org.yottabase.yottaquake.db.mongodb.MongoDBAdapterFactory;

public class DBAdapterManager {
	
	private static final String CONFIG_FILE_PATH = "db.properties";
	
	private static AbstractDBFacade instance = null;

	private DBAdapterManager() {
		super();
	}
	
	public static synchronized AbstractDBFacade getFacade() {
		if (instance == null) {
			InputStream is = DBAdapterManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH);
			PropertyFile properties = new PropertyFile(is);
			
			DBFacadeFactory adapterFactory = new MongoDBAdapterFactory();
			instance = adapterFactory.createService(properties);
		}
		return instance;
	}

}
