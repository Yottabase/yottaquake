package org.yottabase.yottaquake.db;

import java.io.InputStream;

import org.yottabase.yottaquake.db.mongodb.MongoDBAdapterFactory;

public class DBAdapterManager {
	
	private static final String CONFIG_FILE_PATH = "db.properties";
	
	public static synchronized DBFacade getFacade() {
		
		InputStream is = DBAdapterManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH);
		PropertyFile properties = new PropertyFile(is);
		
		DBFacadeFactory adapterFactory = new MongoDBAdapterFactory();
		DBFacade instance = adapterFactory.createService(properties);
		
		return instance;
	}

}
