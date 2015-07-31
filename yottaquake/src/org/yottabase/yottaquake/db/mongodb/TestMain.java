package org.yottabase.yottaquake.db.mongodb;

import java.text.ParseException;
import java.util.Date;

import org.yottabase.yottaquake.core.BoundingBox;
import org.yottabase.yottaquake.core.LatLng;
import org.yottabase.yottaquake.db.DBAdapterManager;
import org.yottabase.yottaquake.db.DBFacade;

public class TestMain {

	public static void main(String[] args) throws ParseException {
		
		DBFacade facade = DBAdapterManager.getFacade();
		
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
		
		BoundingBox box = new BoundingBox(new LatLng(0, 10),new LatLng(89, 0));
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
//		String dateInString = "1999-03-27T18:04:43.5Z";
//		Date from = format.parse(dateInString);
//		System.out.println(from.getTime());
		
//		dateInString = "2092-03-27T18:04:43.5Z";
//		Date to = format.parse(dateInString);
//		Integer minMagnitude = new Integer(5);
//		Integer maxMagnitude = new Integer(7);
//		Integer minDepth = new Integer(10);
		Date from = null;
		Date to = null;
		Integer minMagnitude = null;
		Integer maxMagnitude = null;
		Integer minDepth = null;
		Integer maxDepth = null;
		
//		Iterable<Document> regions = facade.getCountriesWithEventsCount(CountryDetailLevel.HIGH, box);
//		Iterable<Document> regions = facade.getFlinnRegionsWithEventsCount(FlinnRegionDetailLevel.MICRO, box);
//		Iterable<Document> regions = facade.getEvents(box, from, to, minMagnitude, maxMagnitude, minDepth, maxDepth);
//		
//		System.out.println(facade.getCountryEventsCount("Italy",from, to, minMagnitude, maxMagnitude, minDepth, maxDepth));
//		for (Document document : regions) {
//			System.out.println(document.toJson());
//		}
		
//		facade.getDistinctContinents();
		
//		facade.getMagnitude();
//		facade.getDepth();
		
//		LatLng topLeft = new LatLng(3.0, 1.0);
//		LatLng bottomRight = new LatLng(1.0, 4.0);
//		BoundingBox b = new BoundingBox(topLeft, bottomRight);
//		System.out.println(b.toPolygon());
	}
	
}
