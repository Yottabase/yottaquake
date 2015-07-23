package org.yottabase.yottaquake.db.mongodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.yottabase.yottaquake.db.AbstractDBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;
import org.yottabase.yottaquake.db.PropertyFile;

public class InsertEventMain {

	private static final String CONFIG_FILE_PATH = "db.properties";

	
	public static void main(String[] args) throws FileNotFoundException {
		
		AbstractDBFacade facade = getFacade();
		
		String path = args[0];
		InputStream inputStream = new FileInputStream(new File(path + "/dataset.json"));
		JSONTokener json = new JSONTokener(inputStream);
		int count = 0 ;
		while (json.more()) {
			count++;
			
			JSONObject event = (JSONObject) json.nextValue();

			event = convertDate(event);
			
			event = trimRegion(event);
			
			facade.insertEvent(event);
			
			if(count % 10000 == 0)
				System.out.println(count);
			
		}
		
		
	}
	
	public static AbstractDBFacade getFacade(){
		PropertyFile propertyFile = new PropertyFile(DBAdapterManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH));
		DBAdapterManager adapterManager = new DBAdapterManager(propertyFile);
		return adapterManager.getAdapter();
	}
	
	
	public static JSONObject convertDate(JSONObject event) {
		
		JSONObject properties = (JSONObject) event.get("properties");
		
		String dateUTC = (String) properties.get("time");
												
		String[] split1 = dateUTC.split("-");
		
		String year = split1[0];
		
		String month = split1[1];
		
		String[] split2 = split1[2].split("T");
		
		String day = split2[0];
		
		String time = split2[1];
				
		String[] split3 = time.split(":");
		
		String hh = split3[0];
		
		String mm = split3[1];
		
//		String[] split4 = split3[2].split("\\.");
//				
//		String ss = split4[0];
//		System.out.println("year = " +year + "month " + month + "day " + day + "hh " + hh + "mm " + mm + "ss " +ss );
		
		int minutes = Integer.parseInt(hh) * 60 + Integer.parseInt(mm);
		String minutesTime = Integer.toString(minutes);
		
		event.put("year",year);
		event.put("month",month);
		event.put("day",day);
		event.put("time",minutesTime);

								
		return event;		
	
		
	}

	public static JSONObject trimRegion(JSONObject event) {
		
		JSONObject properties = (JSONObject) event.get("properties");
		
		Object region = properties.get("flynn_region");
		if(region != null){
			String regionTrim = region.toString().trim();
			properties.put("flynn_region", regionTrim);
		}
		return event;

		
	}

}
