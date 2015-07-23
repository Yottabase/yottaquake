package org.yottabase.yottaquake.db.mongodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.yottabase.yottaquake.db.AbstractDBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;
import org.yottabase.yottaquake.db.PropertyFile;

public class InsertStatesMain {

	private static final String CONFIG_FILE_PATH = "db.properties";

	
	public static void main(String[] args) throws FileNotFoundException {
		
		AbstractDBFacade facade = getFacade();
		String [] dataset = {"country_low","country_medium","country_high"};
		
		for (String fileName : dataset) {
			System.out.println(fileName);
			String path = args[0];
			InputStream inputStream = new FileInputStream(new File(path + "/" + fileName + ".json"));
			JSONTokener json = new JSONTokener(inputStream);
			
			int count = 0 ;
			while (json.more()) {
				count++;
				
				try{
				JSONObject event = (JSONObject) json.nextValue();
				facade.insertCountry(event, fileName);
				
				if(count % 10000 == 0)
					System.out.println(count);
				
				}catch(JSONException e){
					 break;
				}
	
			}
		}
			
		
		
	}
	
	public static AbstractDBFacade getFacade(){
		PropertyFile propertyFile = new PropertyFile(DBAdapterManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH));
		DBAdapterManager adapterManager = new DBAdapterManager(propertyFile);
		return adapterManager.getAdapter();
	}
	
	
	
}
