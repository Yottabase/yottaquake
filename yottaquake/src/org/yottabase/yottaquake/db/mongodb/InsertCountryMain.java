package org.yottabase.yottaquake.db.mongodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.yottabase.yottaquake.db.AbstractDBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;
import org.yottabase.yottaquake.db.PropertyFile;

public class InsertCountryMain {

	private static final String CONFIG_FILE_PATH = "db.properties";

	
	public static void main(String[] args) throws FileNotFoundException {
		AbstractDBFacade facade = getFacade();
		String [] dataset = {"country_low","country_medium","country_high"};
		for (String fileName : dataset) {
			System.out.println(fileName);
			String path = args[0];
			InputStream inputStream = new FileInputStream(new File(path + "/" + fileName + ".json"));
			JSONTokener json = new JSONTokener(inputStream);
			JSONObject countries = (JSONObject) json.nextValue();
			JSONArray country = (JSONArray) countries.get("features");
				
			int count = 0 ;
			for (int i = 0; i < country.length(); i++) {
				count++;
				
				facade.insertCountry((JSONObject)country.get(i), fileName);
			
				if(count % 100 == 0)
					System.out.println(count);
			}
		}

	}
	
	public static AbstractDBFacade getFacade(){
		PropertyFile propertyFile = new PropertyFile(DBAdapterManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH));
		DBAdapterManager adapterManager = new DBAdapterManager(propertyFile);
		return adapterManager.getAdapter();
	}

}
