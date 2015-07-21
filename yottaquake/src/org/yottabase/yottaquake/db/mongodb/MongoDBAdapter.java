package org.yottabase.yottaquake.db.mongodb;

import org.yottabase.yottaquake.db.AbstractDBFacade;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBAdapter extends AbstractDBFacade {
	private MongoClient client;
	private MongoDatabase db;
	
	private final static String COLLECTION_EVENTS = "earthquake";
	
	public MongoDBAdapter(MongoClient client, MongoDatabase db) {
		this.client = client;
		this.db = db;
	}

	@Override
	public void close() {
		this.client.close();
	}
	
	@Override
	public long countEvents() {
		return db.getCollection(COLLECTION_EVENTS).count();
	}

}
