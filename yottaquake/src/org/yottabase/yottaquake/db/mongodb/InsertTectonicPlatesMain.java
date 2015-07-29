package org.yottabase.yottaquake.db.mongodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.yottabase.yottaquake.db.DBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;

public class InsertTectonicPlatesMain {

	public static void main(String[] args) throws FileNotFoundException, ParseException {
		DBFacade facade = DBAdapterManager.getFacade();
		facade.initializeCollectionTectonicPlates();
		String path = args[0];
		InputStream inputStream = new FileInputStream(new File(path + "/tectonic_plates.json"));
		JSONTokener json = new JSONTokener(inputStream);
		
		int count = 0 ;
		while (json.more()) {
			count++;
			
			JSONObject event = (JSONObject) json.nextValue();
			
			facade.insertTectonicPLates(event);
			
			if(count % 10000 == 0)
				System.out.println(count);
			
		}
	}
}
