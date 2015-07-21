package org.yottabase.yottaquake.db.mongodb;

import org.yottabase.yottaquake.db.AbstractDBFacade;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBAdapter extends AbstractDBFacade {
	private MongoClient mongoClient;
	private MongoDatabase db;

	private final static String DATABASE = "earthquake";
	private final static String COLLECTION_EVENTS = "events";
	
	public MongoDBAdapter(MongoClient client) {
		this.mongoClient = client;
		db = mongoClient.getDatabase(DATABASE);
	}

	@Override
	public void close() {
		this.mongoClient.close();
	}
	
	@Override
	public long countEvents() {
		return db.getCollection(COLLECTION_EVENTS).count();
	}

}
