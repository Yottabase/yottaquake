package org.yottabase.yottaquake.db.mongodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.bson.Document;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.yottabase.yottaquake.db.AbstractDBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;

public class InsertEventMain {

	public static void main(String[] args) throws FileNotFoundException {
		AbstractDBFacade facade = DBAdapterManager.getFacade();
		facade.initializeCollectionEarthquake();

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
			
			if(count % 1000 == 0)
				System.out.println(count);
			
		}
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
		
		int minutes = Integer.parseInt(hh) * 60 + Integer.parseInt(mm);
		String minutesTime = Integer.toString(minutes);
		
		Document timeInformation = new Document();
		timeInformation.append("year", year);
		timeInformation.append("month", month);
		timeInformation.append("day", day);
		timeInformation.append("hour", minutesTime);
			
		event.put("time", timeInformation);
	
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
