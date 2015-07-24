package org.yottabase.yottaquake.db.mongodb;

import static java.util.Arrays.asList;

import org.bson.Document;
import org.json.JSONObject;
import org.yottabase.yottaquake.db.AbstractDBFacade;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

public class MongoDBAdapter extends AbstractDBFacade {
	private MongoClient client;
	private MongoDatabase db;

	private final static String COLLECTION = "earthquake";
	
	private final static String COLLECTIONLOW = "countryLow";
	
	private final static String COLLECTIONMEDIUM = "countryMedium";
	
	private final static String COLLECTIONHIGH = "countryHigh";
	
	public MongoDBAdapter(MongoClient client, MongoDatabase db) {
		this.client = client;
		this.db = db;
	}
	
	@Override
	public void initializeCollectionEarthquake() {
		System.out.println("initialize earthquake");
		db.getCollection(COLLECTION).drop();

		

		/*
		 * index options
		 

		IndexOptions uniqueCostraint = new IndexOptions();
		uniqueCostraint.unique(true);

		IndexOptions noUniqueCostraint = new IndexOptions();
		noUniqueCostraint.unique(false);

		db.getCollection(COLLECTIONARTISTS).createIndex(
				(new Document("artistId", 1)), uniqueCostraint);

		db.getCollection(COLLECTIONUSERS).createIndex(
				(new Document("code", 1)), uniqueCostraint);

		db.getCollection(COLLECTIONLISTENED).createIndex(
				(new Document("code", 1)), noUniqueCostraint);

		db.getCollection(COLLECTIONLISTENED).createIndex(
				(new Document("trackId", 1)), noUniqueCostraint);
		 
		 */
	}

	public void initializeCollectionCountries() {
		System.out.println("initialize countries");
		db.getCollection(COLLECTIONLOW).drop();
		db.getCollection(COLLECTIONMEDIUM).drop();
		db.getCollection(COLLECTIONHIGH).drop();

	}
	
	@Override
	public void close() {
		this.client.close();
	}
	
	@Override
	public void insertEvent(JSONObject event) {

		Document doc = Document.parse(event.toString());
		
		db.getCollection(COLLECTION).insertOne(doc);
		
	}

	public void insertCountry(JSONObject event, String detail) {
		String collection = null;
		
		switch(detail) {
		  case "country_high":  collection = COLLECTIONHIGH;  break;
		  case "country_medium": collection = COLLECTIONMEDIUM; break;
		  case "country_low": collection = COLLECTIONLOW; break;

		}

		Document doc = Document.parse(event.toString());
		db.getCollection(collection).insertOne(doc);
		
	}

	@Override
	public long countEvents() {
		return db.getCollection(COLLECTION).count();
	}
	
	@Override
	public Iterable<Document> countByYearMonth() {
		//db.earthquake.aggregate( [ { $group: { _id: {"year": "$year","month": "$month"}, count: { $sum: 1 } } },{ $project: { "_id":0, "year": "$_id.year", "month": "$_id.month", "count" : 1}} ])
		
		Document groupByYearMonth = new Document("$group", new Document("_id", new Document("year","$year").append("month", "$month")).append("count", new Document("$sum", 1)));
		Document project = new Document("$project", new Document("_id",0).append("year", "$_id.year").append("month", "$_id.month").append("count", 1));
		Document sort = new Document("$sort", new Document("year",1).append("month", 1));

		AggregateIterable<Document> iterable = db.getCollection(COLLECTION).aggregate(asList(groupByYearMonth,project,sort));
				
		return iterable;
	}
	
	@Override
	public Iterable<Document> countByYear() {		
		Document groupByYearMonth = new Document("$group", new Document("_id", new Document("year","$year")).append("count", new Document("$sum", 1)));
		Document project = new Document("$project", new Document("_id",0).append("year", "$_id.year").append("count", 1));
		Document sort = new Document("$sort", new Document("year",1));

		AggregateIterable<Document> iterable = db.getCollection(COLLECTION).aggregate(asList(groupByYearMonth,project,sort));
			
		return iterable;
	}
	
	@Override
	public Iterable<Document> countByMonth() {		
		Document groupByYearMonth = new Document("$group", new Document("_id", new Document("month", "$month")).append("count", new Document("$sum", 1)));
		Document project = new Document("$project", new Document("_id",0).append("month", "$_id.month").append("count", 1));
		Document sort = new Document("$sort", new Document("month",1));

		AggregateIterable<Document> iterable = db.getCollection(COLLECTION).aggregate(asList(groupByYearMonth,project,sort));
			
		return iterable;
	}

	public Iterable<Document> countByMonthInYear(int year) {	
		
		Document match = new Document("$match", new Document("year", Integer.toString(year)));
		Document groupByMonth = new Document("$group", new Document("_id", new Document("month", "$month")).append("count", new Document("$sum", 1)));
		Document project = new Document("$project", new Document("_id",0).append("month", "$_id.month").append("count", 1));
		Document sort = new Document("$sort", new Document("month",1));

		AggregateIterable<Document> iterable = db.getCollection(COLLECTION).aggregate(asList(match,groupByMonth,project,sort));

		return iterable;
	}
	
	@Override
	public Iterable<Document> bigEarthQuake(int magnitude) {
		Document project = new Document(new Document("_id",0).append("properties.mag",1).append("properties.lon",1).append("properties.lat",1));

		Document query = new Document("properties.mag", new Document("$gt", magnitude));
		FindIterable<Document> iterable = db.getCollection(COLLECTION).find(query).projection(project);

		//christian
		iterable.forEach(new Block<Document>() {
			public void apply(final Document document) {
				System.out.println(document.toJson());
//				Document documentOutput = (Document) document.get("properties");				
				
//				System.out.println(documentOutput.get("mag") + "	latitudine = " + documentOutput.get("lat") + "	longitudine = " + documentOutput.get("lon"));
			}
		});	
				
		return iterable;
	}

	@Override
	public Iterable<Document> distinctRegion() {
		Document groupByRegion = new Document("$group", new Document("_id","$properties.flynn_region"));
//		Document project = new Document("$project", new Document("_id",0).append("year", "$_id.year").append("count", 1));
//		Document sort = new Document("$sort", new Document("total",1));

		AggregateIterable<Document> iterable = db.getCollection(COLLECTION).aggregate(asList(groupByRegion));
		
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
		  case "high":  collection = COLLECTIONHIGH;  break;
		  case "medium": collection = COLLECTIONMEDIUM; break;
		  case "low": collection = COLLECTIONLOW; break;

		}
		
		FindIterable<Document> iterable = db.getCollection(collection).find();
	
		return iterable;
	}

	@Override
	public Iterable<Document> getCountriesWithEventCount(String levelQuality) {
		String collection = null;
		switch(levelQuality) {
		  case "high":  collection = COLLECTIONHIGH;  break;
		  case "medium": collection = COLLECTIONMEDIUM; break;
		  case "low": collection = COLLECTIONLOW; break;

		}
		
		Document groupByIsoA3 = new Document("$group", new Document("_id", new Document("iso_a3", "$geolocation.iso_a3")).append("count", new Document("$sum", 1)));
		Document sort = new Document("$sort", new Document("count",1));

		AggregateIterable<Document> eventsCount = db.getCollection(COLLECTION).aggregate(asList(groupByIsoA3,sort));
		
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
}
