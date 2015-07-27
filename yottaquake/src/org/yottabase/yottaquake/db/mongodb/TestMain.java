package org.yottabase.yottaquake.db.mongodb;

import java.util.Set;

import org.bson.Document;
import org.yottabase.yottaquake.core.CountryDetailLevel;
import org.yottabase.yottaquake.db.AbstractDBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;

public class TestMain {

	public static void main(String[] args) {
		
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
		Iterable<Document> regions = facade.getRegionsByMacroRegion("CENTRAL AMERICA");
		for (Document document : regions) {
			System.out.println(document.toJson());
		}

	}
	
}
