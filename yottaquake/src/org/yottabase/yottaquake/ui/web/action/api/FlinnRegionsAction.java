package org.yottabase.yottaquake.ui.web.action.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yottabase.yottaquake.core.BoundingBox;
import org.yottabase.yottaquake.core.EventFilter;
import org.yottabase.yottaquake.db.DBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;
import org.yottabase.yottaquake.ui.web.core.AbstractAction;
import org.yottabase.yottaquake.ui.web.utils.ParamsUtils;

public class FlinnRegionsAction extends AbstractAction{

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		// params
		BoundingBox box = ParamsUtils.extractBoundingBox(request);
		EventFilter eventFilter = ParamsUtils.extractEventFilter(request);
		
		// data
		DBFacade facade = DBAdapterManager.getFacade();
		
		JSONArray items = new JSONArray();
		int min =  Integer.MAX_VALUE;
		int max = 0;
		for(Document doc : facade.getFlinnRegions(box)){
			String name = ((Document) doc.get("properties")).getString("name_l") ;
			Integer counts = facade.getFlinnRegionEventsCount(name, eventFilter);
			
			Integer density = 0;
			Integer surface = doc.getInteger("surface");
			if(surface != 0)
				density = (int) (((double) counts.intValue() / (double) surface.intValue()) * 100000);
			
			min = Math.min(min, density);
			max = Math.max(max, density);
			
			JSONObject obj = new JSONObject(doc.toJson());
			obj.put("density", density);
			obj.put("count", counts);
			obj.put("name", name);
			items.put(obj);
		}
		
		JSONObject result = new JSONObject();
		result.put("minDensity", min);
		result.put("maxDensity", max);
		result.put("items", items);
		
		response.setContentType(this.CONTENT_TYPE_JSON);
		response.getWriter().write(result.toString());

		facade.close();

	}

}
