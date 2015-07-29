package org.yottabase.yottaquake.ui.web.action.api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yottabase.yottaquake.core.BoundingBox;
import org.yottabase.yottaquake.core.CountryDetailLevel;
import org.yottabase.yottaquake.core.EventFilter;
import org.yottabase.yottaquake.db.DBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;
import org.yottabase.yottaquake.ui.web.core.AbstractAction;
import org.yottabase.yottaquake.ui.web.utils.ParamsUtils;

public class CountriesAction extends AbstractAction{

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// params
		BoundingBox box = ParamsUtils.extractBoundingBox(request);
		EventFilter eventFilter = ParamsUtils.extractEventFilter(request);
			
		String mapDetailParam =  ParamsUtils.cleanParam(request.getParameter("levelQuality"));
		if(mapDetailParam == null){
			response.getWriter().write("Parametro levelQuality richiesto");
			return;
		}
		CountryDetailLevel mapDetailLevel = CountryDetailLevel.valueOf(mapDetailParam);
		
		
		// data
		DBFacade facade = DBAdapterManager.getFacade();
		
		JSONArray items = new JSONArray();
		for(Document doc : facade.getCountries(mapDetailLevel, box)){
			Document properties = (Document) doc.get("properties");
			Integer counts = facade.getCountryEventsCount(properties.get("name").toString(), eventFilter);
			
			JSONObject obj = new JSONObject(doc.toJson());
			obj.put("count", counts);
			items.put(obj);
			
		}
		
		response.setContentType(this.CONTENT_TYPE_JSON);
		response.getWriter().write(items.toString());

	}

}
