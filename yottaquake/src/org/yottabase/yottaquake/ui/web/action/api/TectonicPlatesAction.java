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
import org.yottabase.yottaquake.db.DBAdapterManager;
import org.yottabase.yottaquake.db.DBFacade;
import org.yottabase.yottaquake.ui.web.core.AbstractAction;
import org.yottabase.yottaquake.ui.web.utils.ParamsUtils;

public class TectonicPlatesAction extends AbstractAction{

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
		for(Document doc : facade.getTectonicPlates(box)){
			String name = ((Document) doc.get("properties")).getString("PlateName");
			Integer counts = facade.getTectonicPlatesEventsCount(name, eventFilter);
			min = Math.min(min, counts);
			max = Math.max(max, counts);
			
			JSONObject obj = new JSONObject(doc.toJson());
			obj.put("count", counts);
			obj.put("name", name);
			items.put(obj);
		}
		
		JSONObject result = new JSONObject();
		result.put("minCount", min);
		result.put("maxCount", max);
		result.put("items", items);
		
		response.setContentType(this.CONTENT_TYPE_JSON);
		response.getWriter().write(result.toString());

		facade.close();
	}

}
