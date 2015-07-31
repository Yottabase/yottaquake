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
	
	private final static String COLL_FLINN_REGIONS = "flinn_regions";
	
	private final static String COLL_TECT_PLATES = "tectonic_plates";
	
	
	public MongoDBAdapter(MongoClient client, MongoDatabase db) {
		this.client = client;
		this.db = db;
	}
	
	
	private MongoCollection<Document> getEarthquakesCollection() {
		return db.getCollection(COLL_EARTHQUAKES);
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
	
	
	private MongoCollection<Document> getFlinnRegionsCollection() {
		return db.getCollection(COLL_FLINN_REGIONS);
	}
	
	
	private MongoCollection<Document> getTectonicPlatesCollection() {
		return db.getCollection(COLL_TECT_PLATES);
	}
	
	
	@Override
	public void initializeEarthquakesCollection() {	
		db.getCollection(COLL_EARTHQUAKES).drop();
		db.getCollection(COLL_EARTHQUAKES).dropIndex("geometry_2dsphere");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("geolocation.name");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("properties.lon");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("properties.lat");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("properties.mag");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("properties.magtype");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("properties.depth");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("time.millisecond");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("plate_location.PlateName");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("flinnRegion.name_l");
		db.getCollection(COLL_EARTHQUAKES).dropIndex("CONTINENT");
		
		
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("geometry","2dsphere"));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("geolocation.name",1));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("properties.lon",1));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("properties.lat",1));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("properties.mag",1));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("properties.magtype",1));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("properties.depth",1));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("time.millisecond",1));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("plate_location.PlateName",1));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("flinnRegion.name_l",1));
		db.getCollection(COLL_EARTHQUAKES).createIndex(new Document("CONTINENT",1));

		
		System.out.println("Collection \'Earthquakes\' initialized");
	}
	
	
	@Override
	public void initializeCountriesCollection() {
		db.getCollection(COLL_COUNTRIES_LOW).drop();
		db.getCollection(COLL_COUNTRIES_MEDIUM).drop();
		db.getCollection(COLL_COUNTRIES_HIGH).drop();
		System.out.println("Collection \'Countries\' initialized");
	}
	
	
	@Override
	public void initializeContinentsCollection() {
		db.getCollection(COLL_CONTINENTS).drop();
		System.out.println("Collection \'Continents\' initialized");
	}
	
	
	@Override
	public void initializeFlinnRegionsCollection() {
		db.getCollection(COLL_FLINN_REGIONS).drop();
		System.out.println("Collection \'Flinn Regions\' initialized");
	}
	
	
	@Override
	public void initializeTectonicPlatesCollection() {
		db.getCollection(COLL_TECT_PLATES).drop();
		System.out.println("Collection \'Tectonic Plates\' initialized");
	}
	
	
	@Override
	public void insertEvent(JSONObject event) {
		MongoCollection<Document> earthquakesCollection = getEarthquakesCollection();
		Document doc = Document.parse(event.toString());
		
		earthquakesCollection.insertOne(doc);
	}

	
	@Override
	public void insertCountry(JSONObject country, CountryDetailLevel level) {
		MongoCollection<Document> countriesCollection = getCountriesCollection(level);
		Document doc = Document.parse(country.toString());
		
		countriesCollection.insertOne(doc);	
	}
	
	
	@Override
	public void insertContinent(JSONObject continent) {
		MongoCollection<Document> continentCollection = getContinentsCollection();
		Document doc = Document.parse(continent.toString());
		
		continentCollection.insertOne(doc);
	}
	
	
	@Override
	public void insertFlinnRegion(JSONObject flinnRegion) {
		MongoCollection<Document> flinnRegionsCollection = getFlinnRegionsCollection();
		Document doc = Document.parse(flinnRegion.toString());
		
		flinnRegionsCollection.insertOne(doc);
	}


	@Override
	public void insertTectonicPlate(JSONObject tectonicPlate) {
		MongoCollection<Document> tectonicPlatesCollection = getTectonicPlatesCollection();
		Document doc = Document.parse(tectonicPlate.toString());
		
		tectonicPlatesCollection.insertOne(doc);
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
	
	// *********************** CONVEX HULL - START ************************** //

	@Override
	public Set<String> getDistinctMacroRegions() {
		FindIterable<Document> regions = db.getCollection(COLL_FLINN_REGIONS).find();
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
		return db.getCollection(COLL_FLINN_REGIONS).find(new Document("properties.name_h", prefix));
	}
	
	
	@Override
	public Set<String> getDistinctRegionsAggregates() {
		FindIterable<Document> regionsNames = db.getCollection(COLL_FLINN_REGIONS).find()
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
		return db.getCollection(COLL_FLINN_REGIONS).find(new Document("properties.name_h", prefix));
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
	public Iterable<Document> getMagnitudeTypes() {
		Document project = new Document("$project", new Document("properties.mag",1).append("properties.magtype", new Document("$toUpper", "$properties.magtype")));
		Document groupByMagType = new Document("$group", new Document("_id","$properties.magtype").append("max", new Document("$max","$properties.mag")).append("min", new Document("$min","$properties.mag")).append("total", new Document("$sum", 1)));
		Document sort = new Document("$sort", new Document("total", -1));

		AggregateIterable<Document> distinctMagTypes = db.getCollection(COLL_EARTHQUAKES).aggregate(asList(project,groupByMagType,sort));

		return distinctMagTypes;
		
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
		
		if(eventFilter.getMagnitudeType() != null)
			queries.add(new Document("properties.magtype", new Document("$regex", eventFilter.getMagnitudeType()).append("$options", "i")));

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
	public Integer getTectonicPlatesEventsCount(String name,EventFilter eventFilter){
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


	@Override
	public Iterable<Document> getContinents() {
		return getContinentsCollection().find();
	}


	@Override
	public Iterable<Document> getFlinnRegions(BoundingBox box) {
		MongoCollection<Document> collection = getFlinnRegionsCollection();
		
		FindIterable<Document> flinnRegions;
		if(box != null ){
			Document boxDoc = new Document("$geometry",new Document("type","Polygon").append("coordinates", box.toPolygon()));
			flinnRegions = collection.find(new Document("geometry", new Document("$geoIntersects", boxDoc)));
		}
		else 
			flinnRegions = collection.find();
			
		return flinnRegions;		
	}


	@Override
	public Integer getFlinnRegionEventsCount(String name, EventFilter eventFilter) {
		Document matchFlinnRegion;
		ArrayList<Document> queries = this.getEventsFiltersQuery(eventFilter);
		
		if(queries.isEmpty())
			matchFlinnRegion = new Document("$match", new Document("flinnRegion.name_l", name));
		else
			matchFlinnRegion = new Document("$match", new Document("flinnRegion.name_l", name).append("$and", queries));
				
		Document groupByFlinnRegion = new Document("$group", new Document("_id", "$flinnRegion.name_l").append("total", new Document("$sum", 1)));
		AggregateIterable<Document> flinnRegionCounts = db.getCollection(COLL_EARTHQUAKES).aggregate(Arrays.asList(matchFlinnRegion, groupByFlinnRegion));
		
		int counts = 0;
		if (flinnRegionCounts.first() != null)
			counts = Integer.valueOf( flinnRegionCounts.first().get("total").toString());
		
		return counts;
	}


	@Override
	public Integer geContinentEventCount(String name, EventFilter eventFilter) {
		Document matchContinent;
		ArrayList<Document> queries = this.getEventsFiltersQuery(eventFilter);
		
		if(queries.isEmpty())
			matchContinent = new Document("$match", new Document("CONTINENT", name));
		else
			matchContinent = new Document("$match", new Document("CONTINENT", name).append("$and", queries));
				
		Document groupByContinent = new Document("$group", new Document("_id", "$CONTINENT").append("total", new Document("$sum", 1)));
		AggregateIterable<Document> continentCounts = db.getCollection(COLL_EARTHQUAKES).aggregate(Arrays.asList(matchContinent, groupByContinent));
		
		int counts = 0;
		if (continentCounts.first() != null)
			counts = Integer.valueOf( continentCounts.first().get("total").toString());
		
		return counts;
	}

}
