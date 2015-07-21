package org.yottabase.yottaquake.db.mongodb;

import java.util.Arrays;

import org.yottabase.yottaquake.db.AbstractDBFacade;
import org.yottabase.yottaquake.db.DBFacadeFactory;
import org.yottabase.yottaquake.db.PropertyFile;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class MongoDBAdapterFactory implements DBFacadeFactory {

	@Override
	public AbstractDBFacade createService(PropertyFile properties) {
		AbstractDBFacade driver = null;

		String host = properties.get("db.host");
		String username = properties.get("db.username");
		String password = properties.get("db.password");
		String database = properties.get("db.database");
		int port = Integer.parseInt(properties.get("db.port"));

		MongoCredential credential = MongoCredential.createCredential(username, database, password.toCharArray());

		MongoClient client = new MongoClient( new ServerAddress(host, port), Arrays.asList(credential)  );
		MongoDatabase db = client.getDatabase(database);

		driver = new MongoDBAdapter(client, db);

		return driver;
	}

}
