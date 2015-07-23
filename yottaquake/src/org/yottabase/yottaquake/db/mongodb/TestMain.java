package org.yottabase.yottaquake.db.mongodb;

import org.yottabase.yottaquake.db.AbstractDBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;
import org.yottabase.yottaquake.db.PropertyFile;

public class TestMain {

	private static final String CONFIG_FILE_PATH = "db.properties";
	
	public static void main(String[] args) {
		
		AbstractDBFacade facade = getFacade();
		
//		System.out.println(facade.countEvents());
//		facade.convertDate();
//		facade.trimRegion();
//		facade.bigEarthQuake(5);
//		facade.distinctRegion();
//		facade.countByMonth();
//		facade.countByYear();
//		facade.countByYearMonth();
//		facade.countByMonthInYear(2014);
//		facade.getCountries("high");
		facade.getCountriesWithEventCount("high");
		
	}
	
	public static AbstractDBFacade getFacade(){
		PropertyFile propertyFile = new PropertyFile(DBAdapterManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH));
		DBAdapterManager adapterManager = new DBAdapterManager(propertyFile);
		return adapterManager.getAdapter();
	}
	
}
