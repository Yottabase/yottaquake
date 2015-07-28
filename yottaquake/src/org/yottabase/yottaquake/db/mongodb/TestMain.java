package org.yottabase.yottaquake.db.mongodb;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.Document;
import org.yottabase.yottaquake.core.BoundingBox;
import org.yottabase.yottaquake.core.CountryDetailLevel;
import org.yottabase.yottaquake.core.FlinnRegionDetailLevel;
import org.yottabase.yottaquake.core.LatLng;
import org.yottabase.yottaquake.db.AbstractDBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;

public class TestMain {

	public static void main(String[] args) throws ParseException {
		
		AbstractDBFacade facade = DBAdapterManager.getFacade();
		
//		System.out.println(facade.countEvents());
//		facade.bigEarthQuake(5);
//		facade.distinctRegion();
//		facade.countByMonth();
//		facade.countByYear();
//		facade.countByYearMonth();
//		facade.countByMonthInYear(2014);
//		facade.getCountries("high");
//		facade.getCountriesWithEventCount(CountryDetailLevel.HIGH);	
//		Set<String> regions = facade.getDistinctMacroRegions();
		
		BoundingBox box = new BoundingBox(new LatLng(0, 89),new LatLng(10, 0));
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
//		String dateInString = "1999-03-27T18:04:43.5Z";
//		Date from = format.parse(dateInString);
//		System.out.println(from.getTime());
//		Date from = null;
//		dateInString = "2092-03-27T18:04:43.5Z";
//		Date to = format.parse(dateInString);
//		Date to = null;
//		Integer minMagnitude = new Integer(1);
//		Integer maxMagnitude = new Integer(5);
//		Integer minDepth = new Integer(10);
//		Integer minMagnitude = null;
//		Integer maxMagnitude = null;
//		Integer minDepth = null;
//		Integer maxDepth = null;
		
//		Iterable<Document> regions = facade.getCountriesWithEventsCount(CountryDetailLevel.HIGH, box);
		Iterable<Document> regions = facade.getFlinnRegionsWithEventsCount(FlinnRegionDetailLevel.MICRO, box);
//		Iterable<Document> regions = facade.getEvents(box, from, to, minMagnitude, maxMagnitude, minDepth, maxDepth);
		
		for (Document document : regions) {
			System.out.println(document.toJson());
		}

	}
	
}
