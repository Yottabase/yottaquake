package org.yottabase.yottaquake.db.mongodb;

import java.util.Arrays;

import org.yottabase.yottaquake.db.AbstractDBFacade;
import org.yottabase.yottaquake.db.DBFacadeFactory;
import org.yottabase.yottaquake.db.PropertyFile;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoDBAdapterFactory implements DBFacadeFactory {

	@Override
	public AbstractDBFacade createService(PropertyFile properties) {
		AbstractDBFacade driver = null;

		String host = properties.get("db.host");
		String username = properties.get("db.username");
		String password = properties.get("db.password");

		MongoCredential credential = MongoCredential.createCredential(username,
				"lastfm", password.toCharArray());

		MongoClient mongoClient = new MongoClient(
				new ServerAddress(host, 27017), Arrays.asList(credential));

		driver = new MongoDBAdapter(mongoClient);

		return driver;
	}

}
