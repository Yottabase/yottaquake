package org.yottabase.yottaquake.db.mongodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.yottabase.yottaquake.core.CountryDetailLevel;
import org.yottabase.yottaquake.db.DBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;

public class InsertCountryMain {
	
	private static final Map<CountryDetailLevel, String> detail2fileName;
	
	static {
		detail2fileName = new HashMap<CountryDetailLevel, String>();
			detail2fileName.put(CountryDetailLevel.HIGH, "country_high");
			detail2fileName.put(CountryDetailLevel.MEDIUM, "country_medium");
			detail2fileName.put(CountryDetailLevel.LOW, "country_low");
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		DBFacade facade = DBAdapterManager.getFacade();
		facade.initializeCountriesCollection();
		
		for (CountryDetailLevel level : detail2fileName.keySet()) {
			String filePath = args[0] + "/" + detail2fileName.get(level) + ".json";
			InputStream inputStream = new FileInputStream(new File(filePath));
			
			System.out.println(filePath);
			
			JSONTokener json = new JSONTokener(inputStream);
			JSONObject countries = (JSONObject) json.nextValue();
			JSONArray country = (JSONArray) countries.get("features");
				
			int count = 0 ;
			for (int i = 0; i < country.length(); i++) {
				count++;
				
				facade.insertCountry((JSONObject) country.get(i), level);
			
				if( (count % 100) == 0 )
					System.out.println(count);
			}
		}
	}

}
