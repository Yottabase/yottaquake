package org.yottabase.yottaquake.db.mongodb;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.yottabase.yottaquake.core.BoundingBox;
import org.yottabase.yottaquake.core.CountryDetailLevel;
import org.yottabase.yottaquake.core.EventFilter;
import org.yottabase.yottaquake.core.FlinnRegionDetailLevel;
import org.yottabase.yottaquake.db.DBFacade;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

public class MongoDBAdapter implements DBFacade {
	
	private MongoDatabase db;
	private MongoClient client;

	private final static String COLL_EARTHQUAKES = "earthquake";
	
	private final static String COLL_COUNTRIES_LOW = "countryLow";
	private final static String COLL_COUNTRIES_MEDIUM = "countryMedium";
	private final static String COLL_COUNTRIES_HIGH = "countryHigh";
	private final static String COLL_CONTINENTS = "continents";
	
	private final static String COLL_FLINN_MICRO = "flinn_micro";
	private final static String COLL_FLINN_AGRGT = "flinn_agrgt";
	private final static String COLL_FLINN_MACRO = "flinn_macro";
	
	private final static String COLL_TECT_PLATES = "tectonic_plates";
	
	
	public MongoDBAdapter(MongoClient client, MongoDatabase db) {
		this.client = client;
		this.db = db;
	}
	
	
	private MongoCollection<Document> getCountriesCollection(CountryDetailLevel level) {
		String collection = null;
		switch(level) {
		  case HIGH:  
			  collection = COLL_COUNTRIES_HIGH; break;
		  case MEDIUM: 
			  collection = COLL_COUNTRIES_MEDIUM; break;
		  default:
			  collection = COLL_COUNTRIES_LOW; break;
		}
		return db.getCollection(collection);
	}
	
	
	private MongoCollection<Document> getContinentsCollection() {
		return db.getCollection(COLL_CONTINENTS);
	}
	
	
	private MongoCollection<Document> getFlinnRegionsCollection(FlinnRegionDetailLevel level) {
		String collection = null;
		switch(level) {
		  case MACRO:  
			  collection = COLL_FLINN_MACRO; break;
		  case AGGREGATE:
			  collection = COLL_FLINN_AGRGT; break;
		  default:
			  collection = COLL_FLINN_MICRO; break;
				  
		}
		return db.getCollection(collection);
	}
	
	
	private MongoCollection<Document> getTectonicPlatesCollection() {
		return db.getCollection(COLL_TECT_PLATES);
	}
	
	
	@Override
	public void initializeCollectionEarthquake() {
		System.out.println("initialize earthquake");
		
		db.getCollection(COLL_EARTHQUAKES).drop();
		db.getCollection(COLL_EARTHQUAKES).dropIndex("geometry_2dsphere");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("geolocation.name");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("properties.lon");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("properties.lat");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("properties.mag");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("properties.depth");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("time.millisecond");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("plate_location.PlateName");
		
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("geometry","2dsphere"));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("geolocation.name",1));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("properties.lon",1));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("properties.lat",1));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("properties.mag",1));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("properties.depth",1));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("time.millisecond",1));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("plate_location.PlateName",1));


	}
	
	
	@Override
	public void initializeCollectionCountries() {
		System.out.println("initialize countries");
		db.getCollection(COLL_COUNTRIES_LOW).drop();
		db.getCollection(COLL_COUNTRIES_MEDIUM).drop();
		db.getCollection(COLL_COUNTRIES_HIGH).drop();
	}
	
	
	@Override
	public void initializeCollectionFlinnRegions() {
		System.out.println("initialize flinn regions");
		db.getCollection(COLL_FLINN_MICRO).drop();
		db.getCollection(COLL_FLINN_MACRO).drop();
		db.getCollection(COLL_FLINN_AGRGT).drop();

	}
	
	
	@Override
	public void initializeCollectionTectonicPlates() {
		System.out.println("initialize tectonic plates");
		db.getCollection(COLL_TECT_PLATES).drop();
	}

	
	@Override
	public void insertFlinnRegion(JSONObject flinnRegion) {
		Document doc = Document.parse(flinnRegion.toString());
		db.getCollection(COLL_FLINN_MICRO).insertOne(doc);
	}
	
	
	@Override
	public void insertEvent(JSONObject event) {
		Document doc = Document.parse(event.toString());
		db.getCollection(COLL_EARTHQUAKES).insertOne(doc);
	}

	
	@Override
	public void insertCountry(JSONObject event, CountryDetailLevel level) {
		MongoCollection<Document> collection = getCountriesCollection(level);
		Document doc = Document.parse(event.toString());
		
		collection.insertOne(doc);	
	}


	@Override
	public void insertTectonicPLates(JSONObject event) {
		Document doc = Document.parse(event.toString());
		db.getCollection(COLL_TECT_PLATES).insertOne(doc);
	}
	
	
	@Override
	public void close() {
		this.client.close();
	}
		
	
	@Override
	public Iterable<Document> countByYearMonth() {
		//db.earthquake.aggregate( [ { $group: { _id: {"year": "$year","month": "$month"}, count: { $sum: 1 } } },{ $project: { "_id":0, "year": "$_id.year", "month": "$_id.month", "count" : 1}} ])
		Document groupByYearMonth = new Document("$group", new Document("_id", new Document("year", "$year").append("month", "$month")).append("count", new Document("$sum", 1)));
		Document project = new Document("$project", new Document("_id", 0).append("year", "$_id.year").append("month", "$_id.month").append("count", 1));
		Document sort = new Document("$sort", new Document("year", 1).append("month", 1));
				
		return db.getCollection(COLL_EARTHQUAKES).aggregate(asList(groupByYearMonth, project, sort));
	}
	
	
	@Override
	public Iterable<Document> countByYear() {		
		Document groupByYearMonth = new Document("$group", new Document("_id", new Document("year", "$year")).append("count", new Document("$sum", 1)));
		Document project = new Document("$project", new Document("_id", 0).append("year", "$_id.year").append("count", 1));
		Document sort = new Document("$sort", new Document("year", 1));
		
		return db.getCollection(COLL_EARTHQUAKES).aggregate(asList(groupByYearMonth, project, sort));
	}
	
	
	@Override
	public Iterable<Document> countByMonth() {		
		Document groupByYearMonth = new Document("$group", new Document("_id", new Document("month", "$month")).append("count", new Document("$sum", 1)));
		Document project = new Document("$project", new Document("_id", 0).append("month", "$_id.month").append("count", 1));
		Document sort = new Document("$sort", new Document("month", 1));
		
		return db.getCollection(COLL_EARTHQUAKES).aggregate(asList(groupByYearMonth, project, sort));
	}

	
	@Override
	public Iterable<Document> countByMonthInYear(int year) {
		Document match = new Document("$match", new Document("year", Integer.toString(year)));
		Document groupByMonth = new Document("$group", new Document("_id", new Document("month", "$month")).append("count", new Document("$sum", 1)));
		Document project = new Document("$project", new Document("_id", 0).append("month", "$_id.month").append("count", 1));
		Document sort = new Document("$sort", new Document("month", 1));
		
		return db.getCollection(COLL_EARTHQUAKES).aggregate(asList(match,groupByMonth,project,sort));
	}

	
	@Override
	public Iterable<Document> distinctRegion() {
		Document groupByRegion = new Document("$group", new Document("_id","$properties.name_s"));
//		Document project = new Document("$project", new Document("_id",0).append("year", "$_id.year").append("count", 1));
//		Document sort = new Document("$sort", new Document("total",1));
		AggregateIterable<Document> distinctMcroRegions = db.getCollection("flinn").aggregate(asList(groupByRegion));
		
		for (Document region : distinctMcroRegions) {
			 FindIterable<Document> regionByName = db.getCollection("flinn").find(new Document("properties.name_s",region.get("_id")));
			 System.out.println(regionByName.first().toJson());
		}
		return null;
	}
	
	
	@Override
	public Iterable<Document> getEventsInPolygon(Document geometry) {
		//db.earthquake.find({geometry: {$geoWithin: {$geometry: {type : "Polygon", coordinates: [ [ [ 3, 50 ], [ 17, 50 ], [ 19, 36 ], [ 2, 39 ], [3, 50] ] ]}}}}).pretty()
		return db.getCollection(COLL_EARTHQUAKES).find(new Document("geometry", new Document("$geoWithin", new Document("$geometry", geometry))));
	}


	@Override
	public boolean updateDocument(Document document, Document update) {
		Bson sq = new Document("_id",document.get("_id"));
		Bson updateQuery = new Document("$set", update);
		UpdateResult result = db.getCollection(COLL_EARTHQUAKES).updateOne(sq, updateQuery);

		return result.wasAcknowledged();
	}


	@Override
	public Iterable<Document> getEvents(BoundingBox box, EventFilter eventFilter) {
		ArrayList<Document> queries = this.getEventsFiltersQuery(eventFilter);
		
		queries.add(new Document("geometry", new Document("$geoWithin", new Document("$box",box.getCoordinatesPair()))));
		Document query = new Document("$and",queries);
		Document projection = new Document("_id",0).append("properties.lon", 1).append("properties.lat", 1).append("properties.mag", 1).append("properties.depth", 1).append("properties.magtype", 1);
		
		return db.getCollection(COLL_EARTHQUAKES).find(query).projection(projection);

	}
	

	public Iterable<Document> getCountries(CountryDetailLevel level, BoundingBox box){
		//db.countryHigh.find({geometry: {$geoIntersects: {$geometry: {type: "Polygon" ,coordinates: [[ [ 0, 0 ], [ 100, 0 ], [ 100, 89 ], [ 0,89 ], [0,0] ]]}}}})
		MongoCollection<Document> collection = getCountriesCollection(level);
		FindIterable<Document> countries;
		if(box != null && level != CountryDetailLevel.LOW){
			Document boxDoc = new Document("$geometry",new Document("type","Polygon").append("coordinates", box.toPolygon()));
			countries = collection.find(new Document("geometry", new Document("$geoIntersects", boxDoc)));
		}
		else 
			countries = collection.find();
			
		return countries;
		
	}
	
	
	@Override
	public Integer getCountryEventsCount(String name, EventFilter eventFilter) {
		Document matchCountry;
		ArrayList<Document> queries = this.getEventsFiltersQuery(eventFilter);
		
		if(queries.isEmpty())
			matchCountry = new Document("$match", new Document("geolocation.name", name));
		else
			matchCountry = new Document("$match", new Document("geolocation.name", name).append("$and", queries));
				
		Document groupByCountry = new Document("$group", new Document("_id", "$geolocation.name").append("total", new Document("$sum", 1)));
		AggregateIterable<Document> countryCounts = db.getCollection(COLL_EARTHQUAKES).aggregate(Arrays.asList(matchCountry, groupByCountry));
		
		int counts = 0;
		if (countryCounts.first() != null)
			counts = Integer.valueOf( countryCounts.first().get("total").toString());
		
		return counts;
	}


	@Override
	public Iterable<Document> getFlinnRegionsWithEventsCount(FlinnRegionDetailLevel level, BoundingBox box) {
		Document boxDoc = new Document("$geometry",new Document("type","Polygon").append("coordinates", box.toPolygon()));
		
		MongoCollection<Document> collection = getFlinnRegionsCollection(level);
		return collection.find(new Document("geometry", new Document("$geoIntersects", boxDoc)));
	}
	
	// *********************** CONVEX HULL - START ************************** //

	@Override
	public Set<String> getDistinctMacroRegions() {
		FindIterable<Document> regions = db.getCollection(COLL_FLINN_MICRO).find();
		Set<String> distinctMacroRegions = new HashSet<String>();
		
		for (Document region : regions) {
			Document properties = (Document) region.get("properties");
			String name_h = properties.getString("name_h");
			distinctMacroRegions.add(name_h.split(";")[0]);
		}
		
		return distinctMacroRegions;
	}


	@Override
	public Iterable<Document> getRegionsByMacroRegion(String macroRegion) {
		Pattern prefix = Pattern.compile("^"+macroRegion+";");
		return db.getCollection(COLL_FLINN_MICRO).find(new Document("properties.name_h", prefix));
	}
	
	
	@Override
	public Set<String> getDistinctRegionsAggregates() {
		FindIterable<Document> regionsNames = db.getCollection(COLL_FLINN_MICRO).find()
				.projection(new Document("properties.name_h", 1))
				.sort(new Document("properties.name_h", 1));
		
		Set<String> regionsAggregates = new HashSet<String>();
		for (Document doc : regionsNames) {
			Document properties = (Document) doc.get("properties");
			String name = properties.getString("name_h");
			String[] nameSplit = name.split(";");
			
			if (nameSplit.length == 3) {
				String aggregate = nameSplit[1];
				
				if (!aggregate.contains("["))
					regionsAggregates.add(aggregate);
			}	
		}

		return regionsAggregates;
	}
	
	
	@Override
	public Iterable<Document> getRegionsByAggregate(String aggregate) {
		Pattern prefix = Pattern.compile(".*;" + aggregate);
		return db.getCollection(COLL_FLINN_MICRO).find(new Document("properties.name_h", prefix));
	}
	
	
	@Override
	public Set<String> getDistinctContinents() {
		FindIterable<Document> countries = db.getCollection(COLL_COUNTRIES_HIGH).find();
		Set<String> distinctContinents = new HashSet<String>();
		
		for (Document country : countries) {
			Document properties = (Document) country.get("properties");
			String continent = properties.getString("continent");
			distinctContinents.add(continent);
		}
		
		return distinctContinents;
	}


	@Override
	public Iterable<Document> getCountriesByContinet(String continent, CountryDetailLevel level) {
		MongoCollection<Document> collection = getCountriesCollection(level);
		return collection.find(new Document("properties.continent", continent));
	}
	
	// *********************** CONVEX HULL - END ************************** //
	
	@Override
	public void getMagnitude() {
		Document groupByMagType = new Document("$group", new Document("_id","$properties.magtype").append("max", new Document("$max","$properties.mag")).append("min", new Document("$min","$properties.mag")).append("total", new Document("$sum", 1)));
		Document sort = new Document("$sort", new Document("total", -1));

		AggregateIterable<Document> distinctMagType = db.getCollection(COLL_EARTHQUAKES).aggregate(asList(groupByMagType,sort));

		for (Document magType : distinctMagType) 
			System.out.println(magType.toJson());
		
	}
	
	
	@Override
	public void getDepth() {
		FindIterable<Document> a = db.getCollection(COLL_EARTHQUAKES).find(new Document("properties.depth", new Document("$gte", 50))).sort(new Document("properties.depth",-1));
		for (Document document : a) {
			System.out.println(document.toJson());	
		}	
	}
	
	
	private ArrayList<Document> getEventsFiltersQuery(EventFilter eventFilter){
		ArrayList<Document> queries = new ArrayList<Document>();
		
		if(eventFilter.getMinMagnitude() != null)
			queries.add(new Document("properties.mag", new Document("$gte", eventFilter.getMinMagnitude())));
		
		if(eventFilter.getMaxMagnitude() != null)
			queries.add(new Document("properties.mag", new Document("$lte", eventFilter.getMaxMagnitude())));
		
		if(eventFilter.getMinDepth() != null)
			queries.add(new Document("properties.depth", new Document("$gte", eventFilter.getMinDepth())));
		
		if(eventFilter.getMaxDepth() != null)
			queries.add(new Document("properties.depth", new Document("$lte", eventFilter.getMaxDepth())));
		
		if(eventFilter.getFrom() != null)
			queries.add(new Document("time.milliseconds", new Document("$gte", eventFilter.getFrom().getTime())));
		
		if(eventFilter.getTo() != null)
			queries.add(new Document("time.milliseconds", new Document("$lte", eventFilter.getTo().getTime())));
		
		return queries;
	}
	
	/*
	 * query placche tettoniche
	 */
	public Integer PlatesEventsCount(String name,EventFilter eventFilter){
		Document matchPlate;
		ArrayList<Document> queries = this.getEventsFiltersQuery(eventFilter);
		
		if(queries.isEmpty())
			matchPlate = new Document("$match", new Document("plate_location.PlateName", name));
		else
			matchPlate = new Document("$match", new Document("plate_location.PlateName", name).append("$and", queries));
		
		Document groupByPlate = new Document("$group", new Document("_id", "$plate_location.PlateName").append("total", new Document("$sum", 1)));
		AggregateIterable<Document> plateCounts = db.getCollection(COLL_EARTHQUAKES).aggregate(Arrays.asList(matchPlate,groupByPlate));
		
		Integer counts =0;
		if(plateCounts.first() != null)
			counts = Integer.valueOf( plateCounts.first().get("total").toString());
		
		return counts;
	}


	@Override
	public Iterable<Document> getTectonicPlates(BoundingBox box){
		MongoCollection<Document> collection = getTectonicPlatesCollection();
		
		FindIterable<Document> plates;
		if(box != null ){
			Document boxDoc = new Document("$geometry",new Document("type","Polygon").append("coordinates", box.toPolygon()));
			plates = collection.find(new Document("geometry", new Document("$geoIntersects", boxDoc)));
		}
		else 
			plates = collection.find();
			
		return plates;		
	}

}
