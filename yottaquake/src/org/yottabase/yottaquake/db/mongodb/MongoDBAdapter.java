package org.yottabase.yottaquake.db.mongodb;

import static java.util.Arrays.asList;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.yottabase.yottaquake.db.AbstractDBFacade;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

public class MongoDBAdapter extends AbstractDBFacade {
	
	private MongoDatabase db;
	private MongoClient client;

	private final static String COLL_EARTHQUAKES = "earthquake";
	
	private final static String COLL_COUNTRIES_LOW = "countryLow";
	private final static String COLL_COUNTRIES_MEDIUM = "countryMedium";
	private final static String COLL_COUNTRIES_HIGH = "countryHigh";
	
	private final static String COLL_FLINNREGION = "flinnRegion";
	
	
	public MongoDBAdapter(MongoClient client, MongoDatabase db) {
		this.client = client;
		this.db = db;
	}
	
	
	@Override
	public void initializeCollectionEarthquake() {
		System.out.println("initialize earthquake");
		db.getCollection(COLL_EARTHQUAKES).drop();
		db.getCollection(COLL_EARTHQUAKES).dropIndex("geometry_2dsphere");

		/*
		 * index options
		 */
		
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("geometry","2dsphere"));
	}
	

	public void initializeCollectionCountries() {
		System.out.println("initialize countries");
		db.getCollection(COLL_COUNTRIES_LOW).drop();
		db.getCollection(COLL_COUNTRIES_MEDIUM).drop();
		db.getCollection(COLL_COUNTRIES_HIGH).drop();

	}
	
	
	@Override
	public void close() {
		this.client.close();
	}
	
	
	@Override
	public void insertEvent(JSONObject event) {
		Document doc = Document.parse(event.toString());
		db.getCollection(COLL_EARTHQUAKES).insertOne(doc);
	}

	
	public void insertCountry(JSONObject event, String detail) {
		String collection = null;
		
		switch(detail) {
		  case "country_high":  collection = COLL_COUNTRIES_HIGH;  break;
		  case "country_medium": collection = COLL_COUNTRIES_MEDIUM; break;
		  case "country_low": collection = COLL_COUNTRIES_LOW; break;

		}

		Document doc = Document.parse(event.toString());
		db.getCollection(collection).insertOne(doc);	
	}

	
	@Override
	public long countEvents() {
		return db.getCollection(COLL_EARTHQUAKES).count();
	}
	
	
	@Override
	public Iterable<Document> countByYearMonth() {
		//db.earthquake.aggregate( [ { $group: { _id: {"year": "$year","month": "$month"}, count: { $sum: 1 } } },{ $project: { "_id":0, "year": "$_id.year", "month": "$_id.month", "count" : 1}} ])
		
		Document groupByYearMonth = new Document("$group", new Document("_id", new Document("year","$year").append("month", "$month")).append("count", new Document("$sum", 1)));
		Document project = new Document("$project", new Document("_id",0).append("year", "$_id.year").append("month", "$_id.month").append("count", 1));
		Document sort = new Document("$sort", new Document("year",1).append("month", 1));

		AggregateIterable<Document> iterable = db.getCollection(COLL_EARTHQUAKES).aggregate(asList(groupByYearMonth,project,sort));
				
		return iterable;
	}
	
	
	@Override
	public Iterable<Document> countByYear() {		
		Document groupByYearMonth = new Document("$group", new Document("_id", new Document("year","$year")).append("count", new Document("$sum", 1)));
		Document project = new Document("$project", new Document("_id",0).append("year", "$_id.year").append("count", 1));
		Document sort = new Document("$sort", new Document("year",1));

		AggregateIterable<Document> iterable = db.getCollection(COLL_EARTHQUAKES).aggregate(asList(groupByYearMonth,project,sort));
			
		return iterable;
	}
	
	
	@Override
	public Iterable<Document> countByMonth() {		
		Document groupByYearMonth = new Document("$group", new Document("_id", new Document("month", "$month")).append("count", new Document("$sum", 1)));
		Document project = new Document("$project", new Document("_id",0).append("month", "$_id.month").append("count", 1));
		Document sort = new Document("$sort", new Document("month",1));

		AggregateIterable<Document> iterable = db.getCollection(COLL_EARTHQUAKES).aggregate(asList(groupByYearMonth,project,sort));
			
		return iterable;
	}

	
	public Iterable<Document> countByMonthInYear(int year) {
		Document match = new Document("$match", new Document("year", Integer.toString(year)));
		Document groupByMonth = new Document("$group", new Document("_id", new Document("month", "$month")).append("count", new Document("$sum", 1)));
		Document project = new Document("$project", new Document("_id",0).append("month", "$_id.month").append("count", 1));
		Document sort = new Document("$sort", new Document("month",1));

		AggregateIterable<Document> iterable = db.getCollection(COLL_EARTHQUAKES).aggregate(asList(match,groupByMonth,project,sort));

		return iterable;
	}
	
	
	@Override
	public Iterable<Document> bigEarthQuake(int magnitude) {
		Document project = new Document(new Document("_id",0).append("properties.mag",1).append("properties.lon",1).append("properties.lat",1));

		Document query = new Document("properties.mag", new Document("$gt", magnitude));
		FindIterable<Document> iterable = db.getCollection(COLL_EARTHQUAKES).find(query).projection(project);
		
		return iterable;
	}

	
	@Override
	public Iterable<Document> distinctRegion() {
		Document groupByRegion = new Document("$group", new Document("_id","$properties.flynn_region"));
//		Document project = new Document("$project", new Document("_id",0).append("year", "$_id.year").append("count", 1));
//		Document sort = new Document("$sort", new Document("total",1));

		AggregateIterable<Document> iterable = db.getCollection(COLL_EARTHQUAKES).aggregate(asList(groupByRegion));
		
		iterable.forEach(new Block<Document>() {
			public void apply(final Document document) {
				System.out.println(document.toJson());
			}
		});
			
		return iterable;
	}
	

	@Override
	public Iterable<Document> getCountries(String levelQuality) {
		String collection = null;
		switch(levelQuality) {
		  case "high":  collection = COLL_COUNTRIES_HIGH;  break;
		  case "medium": collection = COLL_COUNTRIES_MEDIUM; break;
		  case "low": collection = COLL_COUNTRIES_LOW; break;

		}
		
		FindIterable<Document> iterable = db.getCollection(collection).find();
	
		return iterable;
	}
	

	@Override
	public Iterable<Document> getCountriesWithEventCount(String levelQuality) {
		String collection = null;
		switch(levelQuality) {
		  case "high":  collection = COLL_COUNTRIES_HIGH;  break;
		  case "medium": collection = COLL_COUNTRIES_MEDIUM; break;
		  case "low": collection = COLL_COUNTRIES_LOW; break;

		}
		
		Document groupByIsoA3 = new Document("$group", new Document("_id", new Document("iso_a3", "$geolocation.iso_a3")).append("count", new Document("$sum", 1)));
		Document sort = new Document("$sort", new Document("count",1));

		AggregateIterable<Document> eventsCount = db.getCollection(COLL_EARTHQUAKES).aggregate(asList(groupByIsoA3,sort));
		
		FindIterable<Document> countries = null;
		for (Document document : eventsCount) {
			Document properties =(Document)document.get("_id");
			
			Document project = new Document(new Document("_id",1).append("geometry", 1).append("type", 1).append("properties.NAME",1).append("properties.NAME_LONG",1).append("properties.ISO_A3",1).append("properties.CONTINENT",1));
			countries = db.getCollection(collection).find(new Document("properties.ISO_A3",properties.get("properties"))).projection(project);
			
			for (Document country : countries)
				country.put("count",document.get("count"));
		}
		
		return countries;	
	}
	
	
	@Override
	public Iterable<Document> getEventsInPolygon(Document geometry) {
		//db.earthquake.find({geometry: {$geoWithin: {$geometry: {type : "Polygon", coordinates: [ [ [ 3, 50 ], [ 17, 50 ], [ 19, 36 ], [ 2, 39 ], [3, 50] ] ]}}}}).pretty()
		return db.getCollection(COLL_EARTHQUAKES).find(new Document("geometry", new Document("$geoWithin", new Document("$geometry", geometry))));
	}

	
	@Override
	public void initializeCollectionFlinnRegions() {
		System.out.println("initialize flinn regions");
		db.getCollection(COLL_EARTHQUAKES).drop();
	}

	
	@Override
	public void insertFlinnRegion(JSONObject flinnRegion) {
		Document doc = Document.parse(flinnRegion.toString());
		db.getCollection(COLL_FLINNREGION).insertOne(doc);
	}


	@Override
	public boolean updateDocument(Document document ,Document update) {
		Bson sq = new Document("_id",document.get("_id"));
		Bson updateQuery = new Document("$set", update);
		
		UpdateResult result = db.getCollection(COLL_EARTHQUAKES).updateOne(sq, updateQuery);

		return result.wasAcknowledged();
	}


	@Override
	public Iterable<Document> getFlinnRegions() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
