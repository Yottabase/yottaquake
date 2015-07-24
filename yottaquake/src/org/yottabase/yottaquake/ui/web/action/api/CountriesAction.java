package org.yottabase.yottaquake.ui.web.action.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yottabase.yottaquake.db.AbstractDBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;
import org.yottabase.yottaquake.ui.web.core.AbstractAction;

public class CountriesAction extends AbstractAction{

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String levelQuality =  this.cleanParam(request.getParameter("levelQuality"));
		if(levelQuality == null){
			response.getWriter().write("Parametro levelQuality richiesto");
			return;
		}
		
		AbstractDBFacade facade = DBAdapterManager.getFacade();
		
		JSONArray items = new JSONArray();
		
		for(Document doc : facade.getCountries(levelQuality)){
			
			JSONObject obj = new JSONObject(doc.toJson());
			items.put(obj);
			
		}
		
		response.setContentType(this.CONTENT_TYPE_JSON);
		response.getWriter().write(items.toString());
		
//		facade.close();
	}

}
