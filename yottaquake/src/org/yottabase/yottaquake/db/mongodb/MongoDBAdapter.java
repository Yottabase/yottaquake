package org.yottabase.yottaquake.db.mongodb;

import static java.util.Arrays.asList;


import org.bson.Document;
import org.yottabase.yottaquake.db.AbstractDBFacade;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

public class MongoDBAdapter extends AbstractDBFacade {
	private MongoClient client;
	private MongoDatabase db;
	
	private final static String COLLECTION = "earthquake";
	
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
		return db.getCollection(COLLECTION).count();
	}
	
	public void convertDate() {
		FindIterable<Document> iterable =db.getCollection(COLLECTION).find().projection(new Document("properties.time",1).append("_id", 1));;
		
		iterable.forEach(new Block<Document>() {
			public void apply(final Document document) {
				String dateUTC = document.get("properties").toString();
								
				String[] onlyDate = dateUTC.split("=");
				
				String[] split1 = onlyDate[1].split("-");
				
				String year = split1[0];
				
				String month = split1[1];
				
				String[] split2 = split1[2].split("T");
				
				String day = split2[0];
				
				String time = split2[1];
						
				String[] split3 = time.split(":");
				
				String hh = split3[0];
				
				String mm = split3[1];
				
//				String[] split4 = split3[2].split("\\.");
//				
//				String ss = split4[0];
//				System.out.println("year = " +year + "month " + month + "day " + day + "hh " + hh + "mm " + mm + "ss " +ss );
				
				int minutes = Integer.parseInt(hh) * 60 + Integer.parseInt(mm);
				String minutesTime = Integer.toString(minutes);
								
				db.getCollection(COLLECTION).updateOne(new BasicDBObject("_id", document.get("_id")),
                        new BasicDBObject("$set", new BasicDBObject("year", year).append("month", month).append("day", day).append("time", minutesTime)));

			}
		});			
	
		
	}

	@Override
	public Iterable<Document> countByYearMonth() {
		//db.earthquake.aggregate( [ { $group: { _id: {"year": "$year","month": "$month"}, count: { $sum: 1 } } },{ $project: { "_id":0, "year": "$_id.year", "month": "$_id.month", "count" : 1}} ])
		
		Document groupByYearMonth = new Document("$group", new Document("_id", new Document("year","$year").append("month", "$month")).append("count", new Document("$sum", 1)));
		Document project = new Document("$project", new Document("_id",0).append("year", "$_id.year").append("month", "$_id.month").append("count", 1));
		Document sort = new Document("$sort", new Document("total",1));

		AggregateIterable<Document> iterable = db.getCollection(COLLECTION).aggregate(asList(groupByYearMonth,project,sort));
			

		return iterable;
	}
	
	
	

}
