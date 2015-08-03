package org.yottabase.yottaquake.ui.web.action.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yottabase.yottaquake.db.DBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;
import org.yottabase.yottaquake.ui.web.core.AbstractAction;

public class MagTypesAction extends AbstractAction{

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// data
		DBFacade facade = DBAdapterManager.getFacade();
		
		JSONArray result = new JSONArray();
		
		int i = 1;
		for(Document magType : facade.getMagnitudeTypes()){
			JSONObject json = new JSONObject(magType.toJson());
			if(json.get("_id").equals("")) continue;
			if(i > 10) break;
			
			result.put(json);
			i++;
		}
		
		response.setContentType(this.CONTENT_TYPE_JSON);
		response.getWriter().write(result.toString());
		
		facade.close();
	}

}
